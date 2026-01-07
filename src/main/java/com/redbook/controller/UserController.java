package com.redbook.controller;

import com.redbook.annotation.CurrentUser;
import com.redbook.dto.*;
import com.redbook.service.RelationService;
import com.redbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RelationService relationService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getUserProfile(@PathVariable Long userId) {
        ApiResponse<UserProfileResponse> response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @CurrentUser Long userId,
            @Valid @RequestBody UpdateProfileRequest request) {
        ApiResponse<UserProfileResponse> response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<AvatarUploadResponse>> uploadAvatar(
            @CurrentUser Long userId,
            @RequestParam("file") MultipartFile file) {
        // 这里需要实现文件上传逻辑，暂时使用模拟URL
        String avatarUrl = "https://example.com/avatars/" + userId + "/" + file.getOriginalFilename();
        
        ApiResponse<AvatarUploadResponse> response = userService.uploadAvatar(userId, avatarUrl);
        return ResponseEntity.ok(response);
    }


}