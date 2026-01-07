package com.redbook.controller;

import com.redbook.annotation.CurrentUser;
import com.redbook.dto.ApiResponse;
import com.redbook.dto.FollowResponse;
import com.redbook.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class RelationController {
    
    @Autowired
    private RelationService relationService;

    @PostMapping("/{userId}/follow")
    public ResponseEntity<ApiResponse<FollowResponse>> follow(
            @CurrentUser Long followerId,
            @PathVariable Long userId) {
        ApiResponse<FollowResponse> response = relationService.follow(followerId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<ApiResponse<FollowResponse>> unfollow(
            @CurrentUser Long followerId,
            @PathVariable Long userId) {
        ApiResponse<FollowResponse> response = relationService.follow(followerId, userId);
        return ResponseEntity.ok(response);
    }
}