package com.guohenry.matchapp.dao;

import com.guohenry.matchapp.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDao extends JpaRepository<Member, Long> {

    // 用 email 查詢會員
    Member findByEmail(String email);

    // 檢查 email 是否重複
    boolean existsByEmail(String email);
}
