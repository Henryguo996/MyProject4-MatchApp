package com.guohenry.matchapp.service.Impl;

import com.guohenry.matchapp.dao.MemberDao;
import com.guohenry.matchapp.model.Member;
import com.guohenry.matchapp.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Override
    public Member login(String email, String password) {

        Member member = memberDao.findByEmail(email);
        if(member != null && encoder.matches(password, member.getPassword())) {
            // 密碼正確，回傳會員資料
            return member;
        }

        return null;
    }

    @Override
    public Member register(Member member){

        // 加密密碼後再儲存
        member.setPassword(encoder.encode(member.getPassword()));

        return memberDao.save(member);
    }
}
