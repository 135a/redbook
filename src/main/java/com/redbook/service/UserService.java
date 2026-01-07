package com.redbook.service;

import com.redbook.dto.*;
import com.redbook.entity.User;
import com.redbook.repository.UserRepository;
import com.redbook.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private RelationService relationService;

    @Transactional
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
        // 验证验证码
        boolean verified = verificationCodeService.verifyCode(request.getPhone(), request.getCode(), "register");
        if (!verified) {
            return ApiResponse.error(1003, "验证码错误或过期");
        }

        // 检查手机号是否已存在
        if (userRepository.existsByPhone(request.getPhone())) {
            return ApiResponse.error(400, "手机号已存在");
        }

        // 创建用户
        User user = new User();
        user.setPhone(request.getPhone());
        user.setNickname("用户" + System.currentTimeMillis() % 1000000); // 默认昵称
        user.setAvatarUrl("https://default-avatar.example.com/avatar.png"); // 默认头像
        userRepository.save(user);

        // 生成JWT token
        String token = jwtUtil.generateToken(user.getPhone());

        // 构建响应
        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getId());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setToken(token);

        return ApiResponse.success(response);
    }

    public ApiResponse<CommonResponse> sendCode(SendCodeRequest request) {
        // 生成验证码
        String code = verificationCodeService.generateCode();
        
        // 保存验证码
        verificationCodeService.saveCode(request.getPhone(), code, "register");
        
        // 在实际应用中，这里应该调用短信服务发送验证码
        // 模拟发送成功
        return ApiResponse.success(new CommonResponse());
    }

    public ApiResponse<UserProfileResponse> getUserProfile(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error(404, "用户不存在");
        }

        User user = userOpt.get();
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        response.setUserId(user.getId());

        // 设置关注数和粉丝数
        response.setFollowingCount(relationService.getFollowingCount(userId));
        response.setFollowerCount(relationService.getFollowerCount(userId));

        return ApiResponse.success(response);
    }

    public ApiResponse<UserProfileResponse> updateProfile(Long userId, UpdateProfileRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error(404, "用户不存在");
        }

        User user = userOpt.get();
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        
        userRepository.save(user);

        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        response.setUserId(user.getId());

        // 设置关注数和粉丝数
        response.setFollowingCount(relationService.getFollowingCount(userId));
        response.setFollowerCount(relationService.getFollowerCount(userId));

        return ApiResponse.success(response);
    }

    public ApiResponse<AvatarUploadResponse> uploadAvatar(Long userId, String avatarUrl) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ApiResponse.error(404, "用户不存在");
        }

        User user = userOpt.get();
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        AvatarUploadResponse response = new AvatarUploadResponse();
        response.setAvatarUrl(avatarUrl);

        return ApiResponse.success(response);
    }
    
    // 内部类用于注册响应
    public static class RegisterResponse {
        private Long userId;
        private String nickname;
        private String avatarUrl;
        private String token;

        // getters and setters
        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    
    public static class CommonResponse {
        private Integer code = 200;
        private String message = "success";

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}