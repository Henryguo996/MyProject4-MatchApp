package com.guohenry.matchapp.dao;

import com.guohenry.matchapp.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeDao extends JpaRepository<Like, Long> {

    // 查某人喜歡的所有人
    List<Like> findByLikerId(Long likerId);

    // 查是否已經喜歡過
    Optional<Like> findByLikerIdAndLikedId(Long likerId, Long likedId);
}
