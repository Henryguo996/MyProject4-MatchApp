package com.guohenry.matchapp.dao;

import com.guohenry.matchapp.model.LikeRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRelationshipDao extends JpaRepository<LikeRelationship, Long> {

    Optional<LikeRelationship> findByLikerIdAndLikedId(Integer likerId, Integer likedId);


    boolean existsByLikerIdAndLikedId(Integer likerId, Integer likedId);

}
