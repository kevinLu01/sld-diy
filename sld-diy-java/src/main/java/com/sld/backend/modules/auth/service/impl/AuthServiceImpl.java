package com.sld.backend.modules.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
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
import com.sld.backend.modules.auth.service.AuthService;
import com.sld.backend.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
        );
        if (existUser != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 检查邮箱是否已注册
        existUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getEmail, request.getEmail())
        );
        if (existUser != null) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        String userTypeStr = request.getUserType() != null ? request.getUserType() : "personal";
        UserType userType = UserType.PERSONAL; // default
        for (UserType type : UserType.values()) {
            if (type.getCode().equalsIgnoreCase(userTypeStr)) {
                userType = type;
                break;
            }
        }
        user.setUserType(userType);
        user.setStatus(UserStatus.ACTIVE);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.insert(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getCode());

        return AuthResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .userType(user.getUserType().getCode())
            .token(token)
            .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 查找用户（支持用户名或邮箱登录）
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getAccount())
                .or()
                .eq(User::getEmail, request.getAccount())
        );

        if (user == null) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }

        // 检查账号状态
        if (user.getStatus() == UserStatus.DISABLED) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 生成 Token
        String token = jwtUtil.generateToken(user.getId(), user.getUserType().getCode());

        return AuthResponse.builder()
            .userId(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .userType(user.getUserType().getCode())
            .token(token)
            .build();
    }
}
