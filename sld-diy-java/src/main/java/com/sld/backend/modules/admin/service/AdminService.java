package com.sld.backend.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.admin.dto.request.*;
import com.sld.backend.modules.admin.dto.response.AdminStatsResponse;

import java.util.Map;

/**
 * 管理后台服务接口
 */
public interface AdminService {

    /**
     * 获取统计数据
     */
    AdminStatsResponse getStats();

    // ==================== 产品管理 ====================
    Page<Map<String, Object>> getProducts(Long page, Long limit, String search, String status, Long categoryId);
    Map<String, Object> getProduct(Long id);
    Map<String, Object> createProduct(CreateProductRequest request);
    Map<String, Object> updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);

    // ==================== 分类管理 ====================
    Page<Map<String, Object>> getCategories(Long page, Long limit);
    Map<String, Object> createCategory(CreateCategoryRequest request);
    Map<String, Object> updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);

    // ==================== 品牌管理 ====================
    Page<Map<String, Object>> getBrands(Long page, Long limit);
    Map<String, Object> createBrand(CreateBrandRequest request);
    Map<String, Object> updateBrand(Long id, UpdateBrandRequest request);
    void deleteBrand(Long id);

    // ==================== 订单管理 ====================
    Page<Map<String, Object>> getOrders(Long page, Long limit, String status, String search);
    Map<String, Object> getOrder(Long id);
    Map<String, Object> updateOrderStatus(Long id, String status);

    // ==================== 解决方案管理 ====================
    Page<Map<String, Object>> getSolutions(Long page, Long limit);
    Map<String, Object> getSolution(Long id);
    Map<String, Object> createSolution(CreateSolutionRequest request);
    Map<String, Object> updateSolution(Long id, UpdateSolutionRequest request);
    void deleteSolution(Long id);

    // ==================== 文章管理 ====================
    Page<Map<String, Object>> getArticles(Long page, Long limit, String category, String search);
    Map<String, Object> getArticle(Long id);
    Map<String, Object> createArticle(CreateArticleRequest request);
    Map<String, Object> updateArticle(Long id, UpdateArticleRequest request);
    void deleteArticle(Long id);

    // ==================== 用户管理 ====================
    Page<Map<String, Object>> getUsers(Long page, Long limit, String search);
    Map<String, Object> updateUserStatus(Long id, String status);

    // ==================== DIY配置管理 ====================
    Page<Map<String, Object>> getDiyConfigs(Long page, Long limit, String category);
    Map<String, Object> createDiyConfig(CreateDiyConfigRequest request);
    Map<String, Object> updateDiyConfig(Long id, UpdateDiyConfigRequest request);
    void deleteDiyConfig(Long id);

    // ==================== DIY推荐配置管理 ====================
    Page<Map<String, Object>> getDiyRecommendations(Long page, Long limit, String scenario);
    Map<String, Object> createDiyRecommendation(CreateDiyRecommendationRequest request);
    Map<String, Object> updateDiyRecommendation(Long id, UpdateDiyRecommendationRequest request);
    void deleteDiyRecommendation(Long id);

    // ==================== DIY私发报价 ====================
    Map<String, Object> createPrivateDiyQuote(Long projectId, CreatePrivateDiyQuoteRequest request);

    // ==================== DIY场景模板管理 ====================
    Page<Map<String, Object>> getDiySceneTemplates(Long page, Long limit);
    Map<String, Object> createDiySceneTemplate(CreateDiySceneTemplateRequest request);
    Map<String, Object> updateDiySceneTemplate(Long id, UpdateDiySceneTemplateRequest request);
    void deleteDiySceneTemplate(Long id);

    // ==================== DIY场景组件管理 ====================
    Page<Map<String, Object>> getDiySceneComponents(Long sceneId, Long page, Long limit);
    Map<String, Object> createDiySceneComponent(CreateDiySceneComponentRequest request);
    Map<String, Object> updateDiySceneComponent(Long id, UpdateDiySceneComponentRequest request);
    void deleteDiySceneComponent(Long id);

    // ==================== DIY组件规格选项管理 ====================
    Page<Map<String, Object>> getDiyComponentOptions(Long sceneComponentId, Long page, Long limit);
    Map<String, Object> createDiyComponentOption(CreateDiyComponentOptionRequest request);
    Map<String, Object> updateDiyComponentOption(Long id, UpdateDiyComponentOptionRequest request);
    void deleteDiyComponentOption(Long id);
}
