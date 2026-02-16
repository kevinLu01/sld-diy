package com.sld.backend.modules.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 */
@Data
@Schema(description = "用户登录请求")
public class LoginRequest {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "用户名或邮箱", example = "test_user")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "password123")
    private String password;
}
