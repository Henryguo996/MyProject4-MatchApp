package com.guohenry.matchapp.service;

import com.guohenry.matchapp.model.Member;

public interface MemberService {

    Member login(String email, String password);

    Member register(Member member);
}
