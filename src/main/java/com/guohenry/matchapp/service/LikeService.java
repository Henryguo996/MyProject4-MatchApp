package com.guohenry.matchapp.service;

import com.guohenry.matchapp.model.Like;

import java.util.List;


public interface LikeService {

    boolean like(Long likerId, Long likedId);

    List<Like> findMyLikes(Long likerId);

    boolean likeMember(Integer likerId, Integer likedId);

}
