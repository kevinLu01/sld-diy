package com.sld.backend.modules.auth.controller;

import com.sld.backend.common.result.Result;
import com.sld.backend.modules.auth.dto.request.LoginRequest;
import com.sld.backend.modules.auth.dto.request.RegisterRequest;
import com.sld.backend.modules.auth.dto.response.AuthResponse;
import com.sld.backend.modules.auth.service.AuthService;
import com.sld.backend.modules.user.dto.response.UserProfileResponse;
import com.sld.backend.modules.user.service.UserService;
import com.sld.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<UserProfileResponse> getCurrentUser(
            @Parameter(description = "用户ID") @CurrentUserId Long userId
    ) {
        return Result.success(userService.getUserProfile(userId));
    }
}
