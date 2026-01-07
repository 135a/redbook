package com.redbook.controller;

import com.redbook.dto.*;
import com.redbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * 
     * @param request 包含手机号和验证码的注册请求对象，经过参数校验
     * @return 返回包含用户信息和JWT token的响应实体，状态码为201 CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserService.RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse<UserService.RegisterResponse> response = userService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<UserService.CommonResponse>> sendCode(@Valid @RequestBody SendCodeRequest request) {
        ApiResponse<UserService.CommonResponse> response = userService.sendCode(request);
        return ResponseEntity.ok(response);
    }
}