package com.sld.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信登录配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth.wechat")
public class WechatAuthProperties {

    /**
     * 是否开启微信登录
     */
    private boolean enabled = false;

    /**
     * 微信开放平台 AppId
     */
    private String appId;

    /**
     * 微信开放平台 AppSecret
     */
    private String appSecret;

    /**
     * 回调地址
     */
    private String redirectUri;

    /**
     * scope 默认需要拿用户信息
     */
    private String scope = "snsapi_userinfo";
}

