package com.sld.backend.modules.user.service;

import com.sld.backend.modules.user.dto.request.BusinessVerifyRequest;
import com.sld.backend.modules.user.dto.request.UpdateProfileRequest;
import com.sld.backend.modules.user.dto.response.UserProfileResponse;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 获取用户信息
     */
    UserProfileResponse getUserProfile(Long userId);

    /**
     * 更新用户信息
     */
    UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    /**
     * 企业认证
     */
    void businessVerify(Long userId, BusinessVerifyRequest request);

    /**
     * 获取收藏列表
     */
    List<Map<String, Object>> getFavorites(Long userId);

    /**
     * 添加收藏
     */
    void addFavorite(Long userId, Long productId);

    /**
     * 取消收藏
     */
    void removeFavorite(Long userId, Long productId);
}
