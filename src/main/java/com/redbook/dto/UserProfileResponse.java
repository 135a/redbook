package com.redbook.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private Integer followingCount;
    private Integer followerCount;
}