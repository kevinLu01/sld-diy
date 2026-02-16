package com.sld.backend.controller;

import com.sld.backend.config.TestBase;
import com.sld.backend.modules.auth.dto.request.LoginRequest;
import com.sld.backend.modules.auth.dto.request.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证控制器集成测试
 */
@DisplayName("认证控制器集成测试")
class AuthControllerIntegrationTest extends TestBase {

    @Test
    @DisplayName("用户注册 - 成功")
    void testRegister_Success() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("Test@123456");
        request.setPhone("13800138001");
        request.setUserType("personal");

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    @DisplayName("用户注册 - 参数校验失败")
    void testRegister_ValidationFailed() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername(""); // 空用户名
        request.setEmail("invalid-email"); // 无效邮箱
        request.setPassword("123"); // 密码太短

        // Act & Assert - API returns HTTP 200 with error code in JSON body
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(422));
    }

    @Test
    @DisplayName("用户登录 - 成功")
    void testLogin_Success() throws Exception {
        // 先注册用户
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("logintest");
        registerRequest.setEmail("logintest@example.com");
        registerRequest.setPassword("Test@123456");
        registerRequest.setPhone("13800138002");
        registerRequest.setUserType("personal");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(registerRequest)))
                .andExpect(status().isOk());

        // 登录
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccount("logintest");
        loginRequest.setPassword("Test@123456");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    @DisplayName("用户登录 - 密码错误")
    void testLogin_WrongPassword() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setAccount("nonexistent");
        request.setPassword("WrongPassword");

        // Act & Assert - API returns HTTP 200 with error code in JSON body
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1005));
    }
}
