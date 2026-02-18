package com.sld.backend.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.user.dto.request.BusinessVerifyRequest;
import com.sld.backend.modules.user.dto.request.UpdateProfileRequest;
import com.sld.backend.modules.user.dto.response.UserProfileResponse;
import com.sld.backend.modules.user.entity.BusinessInfo;
import com.sld.backend.modules.user.entity.Favorite;
import com.sld.backend.modules.user.entity.User;
import com.sld.backend.modules.user.mapper.BusinessInfoMapper;
import com.sld.backend.modules.user.mapper.FavoriteMapper;
import com.sld.backend.modules.user.mapper.UserMapper;
import com.sld.backend.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BusinessInfoMapper businessInfoMapper;
    private final FavoriteMapper favoriteMapper;

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return toResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 检查手机号是否被其他用户使用
        if (request.getPhone() != null) {
            Long existCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, request.getPhone())
                    .ne(User::getId, userId)
            );
            if (existCount != null && existCount > 0) {
                throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
            }
        }

        // 更新信息
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getCompanyName() != null) {
            user.setCompanyName(request.getCompanyName());
        }

        userMapper.updateById(user);
        return toResponse(user);
    }

    @Override
    @Transactional
    public void businessVerify(Long userId, BusinessVerifyRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 检查是否已提交过认证
        BusinessInfo existInfo = businessInfoMapper.selectOne(
            new LambdaQueryWrapper<BusinessInfo>().eq(BusinessInfo::getUserId, userId)
        );
        if (existInfo != null && Boolean.TRUE.equals(existInfo.getVerified())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "企业已认证，无需重复提交");
        }

        // 保存或更新认证信息
        BusinessInfo businessInfo = existInfo != null ? existInfo : new BusinessInfo();
        businessInfo.setUserId(userId);
        businessInfo.setCompanyName(request.getCompanyName());
        businessInfo.setBusinessLicense(request.getBusinessLicense());
        businessInfo.setCreditCode(request.getCreditCode());
        businessInfo.setIndustry(request.getIndustry());
        businessInfo.setAddress(request.getAddress());
        businessInfo.setContactPerson(request.getContactPerson());
        businessInfo.setContactPhone(request.getContactPhone());
        businessInfo.setVerified(false);

        if (existInfo != null) {
            businessInfoMapper.updateById(businessInfo);
        } else {
            businessInfo.setCreateTime(LocalDateTime.now());
            businessInfoMapper.insert(businessInfo);
        }

        // 更新用户认证状态
        user.setVerifyStatus("pending");
        userMapper.updateById(user);
    }

    @Override
    public List<Map<String, Object>> getFavorites(Long userId) {
        List<Favorite> favorites = favoriteMapper.selectFavoritesWithProduct(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Favorite favorite : favorites) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", favorite.getId());
            item.put("productId", favorite.getProductId());
            item.put("createTime", favorite.getCreateTime());
            // 这里可以添加更多产品信息
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional
    public void addFavorite(Long userId, Long productId) {
        // 检查是否已收藏
        int count = favoriteMapper.checkFavoriteExists(userId, productId);
        if (count > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "已收藏该商品");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favorite.setCreateTime(LocalDateTime.now());
        favoriteMapper.insert(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long productId) {
        favoriteMapper.delete(
            new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getProductId, productId)
        );
    }

    private UserProfileResponse toResponse(User user) {
        return UserProfileResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .phone(user.getPhone())
            .userType(user.getUserType() != null ? user.getUserType().getCode() : null)
            .status(user.getStatus() != null ? user.getStatus().getCode() : null)
            .avatar(user.getAvatar())
            .nickname(user.getNickname())
            .companyName(user.getCompanyName())
            .verifyStatus(user.getVerifyStatus())
            .lastLoginTime(user.getLastLoginTime())
            .createTime(user.getCreateTime())
            .build();
    }
}
