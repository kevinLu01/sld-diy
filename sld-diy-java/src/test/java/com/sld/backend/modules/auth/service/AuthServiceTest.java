package com.sld.backend.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.enums.UserStatus;
import com.sld.backend.common.enums.UserType;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.util.JwtUtil;
import com.sld.backend.common.util.PasswordUtil;
import com.sld.backend.modules.auth.dto.request.LoginRequest;
import com.sld.backend.modules.auth.dto.request.RegisterRequest;
import com.sld.backend.modules.auth.dto.response.AuthResponse;
import com.sld.backend.modules.user.mapper.UserMapper;
import com.sld.backend.modules.auth.service.impl.AuthServiceImpl;
import com.sld.backend.modules.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 认证服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("认证服务测试")
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 初始化注册请求
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Test@123456");
        registerRequest.setPhone("13800138000");
        registerRequest.setUserType("personal");

        // 初始化登录请求
        loginRequest = new LoginRequest();
        loginRequest.setAccount("testuser");
        loginRequest.setPassword("Test@123456");

        // 初始化测试用户
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(PasswordUtil.encode("Test@123456"));
        testUser.setPhone("13800138000");
        testUser.setUserType(UserType.PERSONAL);
        testUser.setStatus(UserStatus.ACTIVE);
        testUser.setLastLoginTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("用户注册 - 成功")
    void testRegister_Success() {
        // Arrange
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getUserType()).isEqualTo("personal");
        assertThat(response.getToken()).isEqualTo("test-jwt-token");

        verify(userMapper, times(2)).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper).insert(any(User.class));
        verify(jwtUtil).generateToken(1L, "personal");
    }

    @Test
    @DisplayName("用户注册 - 用户名已存在")
    void testRegister_UsernameExists() {
        // Arrange
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> authService.register(registerRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.USER_ALREADY_EXISTS.getCode());

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).insert(any(User.class));
        verify(jwtUtil, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户注册 - 邮箱已存在")
    void testRegister_EmailExists() {
        // Arrange
        when(userMapper.selectOne(any(LambdaQueryWrapper.class)))
            .thenReturn(null)
            .thenReturn(testUser);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> authService.register(registerRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS.getCode());

        verify(userMapper, times(2)).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).insert(any(User.class));
        verify(jwtUtil, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户登录 - 成功（用户名登录）")
    void testLogin_Success_Username() {
        // Arrange
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getToken()).isEqualTo("test-jwt-token");

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper).updateById(any(User.class));
        verify(jwtUtil).generateToken(1L, "personal");
    }

    @Test
    @DisplayName("用户登录 - 成功（邮箱登录）")
    void testLogin_Success_Email() {
        // Arrange
        loginRequest.setAccount("test@example.com");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);
        when(userMapper.updateById(any(User.class))).thenReturn(1);
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(1L);

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper).updateById(any(User.class));
        verify(jwtUtil).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户登录 - 用户不存在")
    void testLogin_UserNotFound() {
        // Arrange
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> authService.login(loginRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.PASSWORD_ERROR.getCode());

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).updateById(any(User.class));
        verify(jwtUtil, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户登录 - 密码错误")
    void testLogin_WrongPassword() {
        // Arrange
        loginRequest.setPassword("WrongPassword123");
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> authService.login(loginRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.PASSWORD_ERROR.getCode());

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).updateById(any(User.class));
        verify(jwtUtil, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户登录 - 账号被禁用")
    void testLogin_AccountDisabled() {
        // Arrange
        testUser.setStatus(UserStatus.DISABLED);
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> authService.login(loginRequest));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ACCOUNT_DISABLED.getCode());

        verify(userMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(userMapper, never()).updateById(any(User.class));
        verify(jwtUtil, never()).generateToken(anyLong(), anyString());
    }

    @Test
    @DisplayName("用户注册 - 默认用户类型为企业用户")
    void testRegister_DefaultBusinessUserType() {
        // Arrange
        registerRequest.setUserType(null);
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });
        when(jwtUtil.generateToken(anyLong(), anyString())).thenReturn("test-jwt-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response.getUserType()).isEqualTo("personal");
    }
}
