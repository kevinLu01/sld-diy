package com.sld.backend.contract;

import com.sld.backend.config.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 契约烟测")
class ApiContractSmokeIntegrationTest extends TestBase {

    @Test
    @DisplayName("公开接口返回统一契约字段 code/message")
    void publicApiContract() throws Exception {
        mockMvc.perform(get("/api/v1/products?page=1&limit=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("认证参数校验返回统一业务码")
    void authValidationContract() throws Exception {
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("未登录访问受保护接口返回401 JSON契约")
    void unauthorizedContract() throws Exception {
        mockMvc.perform(get("/api/v1/orders?page=1&limit=5"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(401))
            .andExpect(jsonPath("$.message").exists());
    }
}
