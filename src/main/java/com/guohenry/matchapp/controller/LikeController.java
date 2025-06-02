package com.guohenry.matchapp.controller;

import com.guohenry.matchapp.model.Like;
import com.guohenry.matchapp.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Like API", description = "會員喜歡功能")
@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Operation(summary = "喜歡某個會員", description = "需要登入後才能使用")
    @PostMapping("/{likerId}/like/{likedId}")
    public ResponseEntity<?> like(@PathVariable Integer likerId, @PathVariable Integer likedId) {
        boolean success = likeService.likeMember(likerId, likedId);
        if (!success) {
            return ResponseEntity.badRequest().body("無法喜歡此會員（可能已重複或自喜歡）");
        }
        return ResponseEntity.ok("已成功送出喜歡！");
    }

    @PostMapping("/{likerId}/{likedId}")
    public ResponseEntity<?> like(@PathVariable Long likerId,@PathVariable Long likedId){

        boolean success = likeService.like(likerId,likedId);
        if(success){
            return ResponseEntity.ok("你已喜歡對方!");
        }else{
            return ResponseEntity.badRequest().body("你已喜歡過對方了!");
        }

    }

    @GetMapping("/{likerId}")
    public ResponseEntity<List<Like>> myLikes(@PathVariable Long likerId){
        return ResponseEntity.ok(likeService.findMyLikes(likerId));
    }






}
