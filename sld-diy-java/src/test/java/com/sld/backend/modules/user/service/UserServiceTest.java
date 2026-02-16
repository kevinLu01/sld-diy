package com.sld.backend.modules.user.service;

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
import com.sld.backend.modules.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 用户服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户服务测试")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private BusinessInfoMapper businessInfoMapper;

    @Mock
    private FavoriteMapper favoriteMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UpdateProfileRequest updateRequest;
    private BusinessVerifyRequest businessRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setNickname("Test User");
        testUser.setAvatar("http://example.com/avatar.jpg");
        testUser.setCompanyName("Test Company");
        testUser.setVerifyStatus("unverified");

        updateRequest = new UpdateProfileRequest();
        updateRequest.setNickname("Updated Name");
        updateRequest.setPhone("13900139000");
        updateRequest.setAvatar("http://example.com/new-avatar.jpg");
        updateRequest.setCompanyName("Updated Company");

        businessRequest = new BusinessVerifyRequest();
        businessRequest.setCompanyName("Business Corp");
        businessRequest.setBusinessLicense("BL123456");
        businessRequest.setCreditCode("CC987654");
        businessRequest.setIndustry("Manufacturing");
        businessRequest.setAddress("Business Address");
        businessRequest.setContactPerson("Contact Person");
        businessRequest.setContactPhone("13700137000");
    }

    @Test
    @DisplayName("获取用户信息 - 成功")
    void testGetUserProfile_Success() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // Act
        UserProfileResponse response = userService.getUserProfile(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");

        verify(userMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取用户信息 - 用户不存在")
    void testGetUserProfile_UserNotFound() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> userService.getUserProfile(1L));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.USER_NOT_FOUND.getCode());

        verify(userMapper).selectById(1L);
    }

    @Test
    @DisplayName("更新用户信息 - 成功")
    void testUpdateProfile_Success() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(userMapper.selectOne(any())).thenReturn(null);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // Act
        UserProfileResponse response = userService.updateProfile(1L, updateRequest);

        // Assert
        assertThat(response).isNotNull();

        verify(userMapper).selectById(1L);
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    @DisplayName("企业认证 - 成功（首次认证）")
    void testBusinessVerify_FirstTime() {
        // Arrange
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(businessInfoMapper.selectOne(any())).thenReturn(null);
        when(businessInfoMapper.insert(any(BusinessInfo.class))).thenReturn(1);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // Act
        userService.businessVerify(1L, businessRequest);

        // Assert
        verify(businessInfoMapper).insert(any(BusinessInfo.class));
        verify(userMapper).updateById(any(User.class));
    }

    @Test
    @DisplayName("企业认证 - 已认证过，更新认证信息")
    void testBusinessVerify_UpdateExisting() {
        // Arrange
        BusinessInfo existingInfo = new BusinessInfo();
        existingInfo.setId(1L);
        existingInfo.setUserId(1L);
        existingInfo.setVerified(false);

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(businessInfoMapper.selectOne(any())).thenReturn(existingInfo);
        when(businessInfoMapper.updateById(any(BusinessInfo.class))).thenReturn(1);
        when(userMapper.updateById(any(User.class))).thenReturn(1);

        // Act
        userService.businessVerify(1L, businessRequest);

        // Assert
        verify(businessInfoMapper).updateById(any(BusinessInfo.class));
    }

    @Test
    @DisplayName("企业认证 - 已认证完成，重复提交")
    void testBusinessVerify_AlreadyVerified() {
        // Arrange
        BusinessInfo existingInfo = new BusinessInfo();
        existingInfo.setId(1L);
        existingInfo.setUserId(1L);
        existingInfo.setVerified(true);

        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(businessInfoMapper.selectOne(any())).thenReturn(existingInfo);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> userService.businessVerify(1L, businessRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.BAD_REQUEST.getCode());
    }

    @Test
    @DisplayName("获取收藏列表 - 成功")
    void testGetFavorites_Success() {
        // Arrange
        List<Favorite> favorites = new ArrayList<>();
        Favorite fav = new Favorite();
        fav.setId(1L);
        fav.setUserId(1L);
        fav.setProductId(100L);
        favorites.add(fav);

        when(favoriteMapper.selectFavoritesWithProduct(1L)).thenReturn(favorites);

        // Act
        List<Map<String, Object>> result = userService.getFavorites(1L);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).get("productId")).isEqualTo(100L);

        verify(favoriteMapper).selectFavoritesWithProduct(1L);
    }

    @Test
    @DisplayName("添加收藏 - 成功")
    void testAddFavorite_Success() {
        // Arrange
        when(favoriteMapper.checkFavoriteExists(1L, 100L)).thenReturn(0);
        when(favoriteMapper.insert(any(Favorite.class))).thenReturn(1);

        // Act
        userService.addFavorite(1L, 100L);

        // Assert
        verify(favoriteMapper).insert(any(Favorite.class));
    }

    @Test
    @DisplayName("添加收藏 - 已收藏")
    void testAddFavorite_AlreadyExists() {
        // Arrange
        when(favoriteMapper.checkFavoriteExists(1L, 100L)).thenReturn(1);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> userService.addFavorite(1L, 100L));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.BAD_REQUEST.getCode());

        verify(favoriteMapper, never()).insert(any(Favorite.class));
    }

    @Test
    @DisplayName("取消收藏 - 成功")
    void testRemoveFavorite_Success() {
        // Arrange
        when(favoriteMapper.delete(any())).thenReturn(1);

        // Act
        userService.removeFavorite(1L, 100L);

        // Assert
        verify(favoriteMapper).delete(any());
    }
}
