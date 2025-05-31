package com.guohenry.matchapp.controller;


import com.guohenry.matchapp.dao.MemberDao;
import com.guohenry.matchapp.dto.LoginRequest;
import com.guohenry.matchapp.model.Member;
import com.guohenry.matchapp.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest ){

        Member member = memberService.login(loginRequest.getEmail(),loginRequest .getPassword());

        if(member == null){
            return ResponseEntity.status(401).body("帳號或密碼錯誤!");
        }

        // 不回傳密碼給前端
        member.setPassword(null);
        return ResponseEntity.ok(member);

    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Member member){

        if(memberDao.existsByEmail(member.getEmail())){
            return ResponseEntity.badRequest().body("Email 已被註冊!");
        }
        Member saved = memberService.register(member);
        return ResponseEntity.ok(saved);
    }

}
