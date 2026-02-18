package com.sld.backend.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
@Schema(description = "微信登录请求")
public class WechatLoginRequest {

    @NotBlank(message = "code不能为空")
    @Schema(description = "微信授权码")
    private String code;

    @NotBlank(message = "state不能为空")
    @Schema(description = "防CSRF state")
    private String state;
}

