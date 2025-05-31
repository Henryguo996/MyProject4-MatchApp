package com.guohenry.matchapp.service;

import com.guohenry.matchapp.model.Member;

public interface MemberService {

    Member login(String emial, String password);

    Member register(Member member);
}
