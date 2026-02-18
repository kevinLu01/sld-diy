package com.sld.backend.modules.auth.service;

import com.sld.backend.modules.auth.dto.request.LoginRequest;
import com.sld.backend.modules.auth.dto.request.RegisterRequest;
import com.sld.backend.modules.auth.dto.request.WechatLoginRequest;
import com.sld.backend.modules.auth.dto.response.AuthResponse;
import com.sld.backend.modules.auth.dto.response.WechatAuthorizeResponse;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户注册
     */
    AuthResponse register(RegisterRequest request);

    /**
     * 用户登录
     */
    AuthResponse login(LoginRequest request);

    /**
     * 获取微信授权地址
     */
    WechatAuthorizeResponse getWechatAuthorizeUrl();

    /**
     * 微信登录
     */
    AuthResponse wechatLogin(WechatLoginRequest request);
}
