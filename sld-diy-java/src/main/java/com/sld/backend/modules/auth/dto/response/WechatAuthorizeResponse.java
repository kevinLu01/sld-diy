package com.sld.backend.modules.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 微信授权地址响应
 */
@Data
@Builder
@Schema(description = "微信授权地址响应")
public class WechatAuthorizeResponse {

    @Schema(description = "微信授权地址")
    private String authorizeUrl;

    @Schema(description = "state")
    private String state;
}

