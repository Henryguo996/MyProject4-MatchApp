package com.guohenry.matchapp.service.Impl;

import com.guohenry.matchapp.dao.LikeDao;
import com.guohenry.matchapp.dao.LikeRelationshipDao;
import com.guohenry.matchapp.model.Like;
import com.guohenry.matchapp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guohenry.matchapp.model.LikeRelationship;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeDao likeDao;

    @Autowired
    private LikeRelationshipDao likeRelationshipDao;

    @Override
    public boolean like(Long likerId, Long likedId) {
        // 檢查是否已按過喜歡
        boolean alreadyLiked = likeDao.findByLikerIdAndLikedId(likerId, likedId).isPresent();

        if(alreadyLiked) {
            return false;
        }

        likeDao.save(new Like(likerId, likedId));

        return true;
    }

    @Override
    public List<Like> findMyLikes(Long likerId) {

        return likeDao.findByLikerId(likerId);

    }

    @Override
    public boolean likeMember(Integer likerId, Integer likedId) {
        if (likerId.equals(likedId)) return false; // 不可自喜歡
        if (likeRelationshipDao.existsByLikerIdAndLikedId(likerId, likedId)) return false;

        LikeRelationship relationship = new LikeRelationship();
        relationship.setLikerId(likerId);
        relationship.setLikedId(likedId);
        likeRelationshipDao.save(relationship);

        return true;
    }
}
