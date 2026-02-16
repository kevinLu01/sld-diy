package com.sld.backend.modules.user.controller;

import com.sld.backend.common.result.Result;
import com.sld.backend.modules.user.dto.request.BusinessVerifyRequest;
import com.sld.backend.modules.user.dto.request.UpdateProfileRequest;
import com.sld.backend.modules.user.dto.response.UserProfileResponse;
import com.sld.backend.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取用户信息")
    public Result<UserProfileResponse> getProfile(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        return Result.success(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息")
    public Result<UserProfileResponse> updateProfile(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        return Result.success(userService.updateProfile(userId, request));
    }

    @PostMapping("/business-verify")
    @Operation(summary = "企业认证")
    public Result<Void> businessVerify(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId,
        @Valid @RequestBody BusinessVerifyRequest request
    ) {
        userService.businessVerify(userId, request);
        return Result.success();
    }

    @GetMapping("/favorites")
    @Operation(summary = "获取收藏列表")
    public Result<List<Map<String, Object>>> getFavorites(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        return Result.success(userService.getFavorites(userId));
    }

    @PostMapping("/favorites/{productId}")
    @Operation(summary = "添加收藏")
    public Result<Void> addFavorite(
        @PathVariable Long productId,
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        userService.addFavorite(userId, productId);
        return Result.success();
    }

    @DeleteMapping("/favorites/{productId}")
    @Operation(summary = "取消收藏")
    public Result<Void> removeFavorite(
        @PathVariable Long productId,
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        userService.removeFavorite(userId, productId);
        return Result.success();
    }
}
