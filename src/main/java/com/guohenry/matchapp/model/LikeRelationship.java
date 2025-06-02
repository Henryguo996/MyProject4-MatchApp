package com.guohenry.matchapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "like_relationships", uniqueConstraints = @UniqueConstraint(columnNames = {"liker_id", "liked_id"}))
public class LikeRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liker_id", nullable = false)
    private Integer likerId;

    @Column(name = "liked_id", nullable = false)
    private Integer likedId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLikerId() {
        return likerId;
    }

    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }

    public Integer getLikedId() {
        return likedId;
    }

    public void setLikedId(Integer likedId) {
        this.likedId = likedId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
