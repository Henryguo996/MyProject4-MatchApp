package com.guohenry.matchapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 喜歡的人（目標）
    @Column(nullable = false)
    private Long likedId;

    // 發起喜歡的會員
    @Column(nullable = false)
    private Long likerId;

    public Like() {
    }

    public Like(Long likerId, Long likedId) {
        this.likerId = likerId;
        this.likedId = likedId;
    }

    public Long getId() {
        return id;
    }

    public Long getLikerId() {
        return likerId;
    }

    public void setLikerId(Long likerId) {
        this.likerId = likerId;
    }

    public Long getLikedId() {
        return likedId;
    }

    public void setLikedId(Long likedId) {
        this.likedId = likedId;
    }
}
