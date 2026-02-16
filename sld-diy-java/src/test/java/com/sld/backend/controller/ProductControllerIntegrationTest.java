package com.sld.backend.controller;

import com.sld.backend.config.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 产品控制器集成测试
 */
@DisplayName("产品控制器集成测试")
class ProductControllerIntegrationTest extends TestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 插入测试分类和品牌
        jdbcTemplate.execute("INSERT INTO t_category (id, name, slug, status) VALUES (1, '压缩机', 'compressor', 1)");
        jdbcTemplate.execute("INSERT INTO t_brand (id, name, slug, status) VALUES (1, '大金', 'daikin', 1)");
        // 插入测试产品
        jdbcTemplate.execute("INSERT INTO t_product (id, sku, name, brand_id, category_id, price, stock, status) " +
                "VALUES (1, 'TEST-001', '测试压缩机', 1, 1, 1000.00, 100, 'on_shelf')");
    }

    @Test
    @DisplayName("获取产品列表 - 成功")
    void testListProducts_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .param("page", "1")
                .param("limit", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    @DisplayName("获取产品列表 - 带搜索参数")
    void testListProducts_WithSearch() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                .param("search", "compressor")
                .param("sort", "sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("获取产品详情 - 成功")
    void testGetProduct_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.name").value("测试压缩机"));
    }

    @Test
    @DisplayName("获取产品详情 - 不存在")
    void testGetProduct_NotFound() throws Exception {
        // API returns HTTP 200 with error code in JSON body
        mockMvc.perform(get("/api/v1/products/99999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(2001));
    }

    @Test
    @DisplayName("获取产品评价 - 成功")
    void testGetProductReviews_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products/1/reviews")
                .param("page", "1")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @DisplayName("获取产品兼容性 - 成功")
    void testGetProductCompatibility_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products/1/compatibility"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
