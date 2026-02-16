package com.sld.backend.modules.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.admin.dto.request.*;
import com.sld.backend.modules.admin.dto.response.AdminStatsResponse;
import com.sld.backend.modules.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理后台控制器
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "管理后台", description = "管理后台相关接口")
public class AdminController {

    private final AdminService adminService;

    // ==================== 统计 ====================
    @GetMapping("/stats")
    @Operation(summary = "获取统计数据")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<AdminStatsResponse> getStats() {
        return Result.success(adminService.getStats());
    }

    // ==================== 产品管理 ====================
    @GetMapping("/products")
    @Operation(summary = "获取产品列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getProducts(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Long categoryId
    ) {
        return Result.success(PageResult.of(adminService.getProducts(page, limit, search, status, categoryId)));
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "获取产品详情")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getProduct(@PathVariable Long id) {
        return Result.success(adminService.getProduct(id));
    }

    @PostMapping("/products")
    @Operation(summary = "创建产品")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return Result.success(adminService.createProduct(request));
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "更新产品")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        return Result.success(adminService.updateProduct(id, request));
    }

    @DeleteMapping("/products/{id}")
    @Operation(summary = "删除产品")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return Result.success();
    }

    // ==================== 分类管理 ====================
    @GetMapping("/categories")
    @Operation(summary = "获取分类列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getCategories(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        return Result.success(PageResult.of(adminService.getCategories(page, limit)));
    }

    @PostMapping("/categories")
    @Operation(summary = "创建分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return Result.success(adminService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    @Operation(summary = "更新分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        return Result.success(adminService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    @Operation(summary = "删除分类")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return Result.success();
    }

    // ==================== 品牌管理 ====================
    @GetMapping("/brands")
    @Operation(summary = "获取品牌列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getBrands(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        return Result.success(PageResult.of(adminService.getBrands(page, limit)));
    }

    @PostMapping("/brands")
    @Operation(summary = "创建品牌")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        return Result.success(adminService.createBrand(request));
    }

    @PutMapping("/brands/{id}")
    @Operation(summary = "更新品牌")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateBrand(@PathVariable Long id, @Valid @RequestBody UpdateBrandRequest request) {
        return Result.success(adminService.updateBrand(id, request));
    }

    @DeleteMapping("/brands/{id}")
    @Operation(summary = "删除品牌")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteBrand(@PathVariable Long id) {
        adminService.deleteBrand(id);
        return Result.success();
    }

    // ==================== 订单管理 ====================
    @GetMapping("/orders")
    @Operation(summary = "获取订单列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getOrders(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String search
    ) {
        return Result.success(PageResult.of(adminService.getOrders(page, limit, status, search)));
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "获取订单详情")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getOrder(@PathVariable Long id) {
        return Result.success(adminService.getOrder(id));
    }

    @PutMapping("/orders/{id}/status")
    @Operation(summary = "更新订单状态")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(adminService.updateOrderStatus(id, status));
    }

    // ==================== 解决方案管理 ====================
    @GetMapping("/solutions")
    @Operation(summary = "获取解决方案列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getSolutions(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        return Result.success(PageResult.of(adminService.getSolutions(page, limit)));
    }

    @GetMapping("/solutions/{id}")
    @Operation(summary = "获取解决方案详情")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getSolution(@PathVariable Long id) {
        return Result.success(adminService.getSolution(id));
    }

    @PostMapping("/solutions")
    @Operation(summary = "创建解决方案")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createSolution(@Valid @RequestBody CreateSolutionRequest request) {
        return Result.success(adminService.createSolution(request));
    }

    @PutMapping("/solutions/{id}")
    @Operation(summary = "更新解决方案")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateSolution(@PathVariable Long id, @Valid @RequestBody UpdateSolutionRequest request) {
        return Result.success(adminService.updateSolution(id, request));
    }

    @DeleteMapping("/solutions/{id}")
    @Operation(summary = "删除解决方案")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteSolution(@PathVariable Long id) {
        adminService.deleteSolution(id);
        return Result.success();
    }

    // ==================== 文章管理 ====================
    @GetMapping("/articles")
    @Operation(summary = "获取文章列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getArticles(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String search
    ) {
        return Result.success(PageResult.of(adminService.getArticles(page, limit, category, search)));
    }

    @GetMapping("/articles/{id}")
    @Operation(summary = "获取文章详情")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> getArticle(@PathVariable Long id) {
        return Result.success(adminService.getArticle(id));
    }

    @PostMapping("/articles")
    @Operation(summary = "创建文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createArticle(@Valid @RequestBody CreateArticleRequest request) {
        return Result.success(adminService.createArticle(request));
    }

    @PutMapping("/articles/{id}")
    @Operation(summary = "更新文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateArticle(@PathVariable Long id, @Valid @RequestBody UpdateArticleRequest request) {
        return Result.success(adminService.updateArticle(id, request));
    }

    @DeleteMapping("/articles/{id}")
    @Operation(summary = "删除文章")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        adminService.deleteArticle(id);
        return Result.success();
    }

    // ==================== 用户管理 ====================
    @GetMapping("/users")
    @Operation(summary = "获取用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getUsers(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String search
    ) {
        return Result.success(PageResult.of(adminService.getUsers(page, limit, search)));
    }

    @PutMapping("/users/{id}/status")
    @Operation(summary = "更新用户状态")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateUserStatus(@PathVariable Long id, @RequestParam String status) {
        return Result.success(adminService.updateUserStatus(id, status));
    }

    // ==================== DIY配置管理 ====================
    @GetMapping("/diy-configs")
    @Operation(summary = "获取DIY配置列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getDiyConfigs(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String category
    ) {
        return Result.success(PageResult.of(adminService.getDiyConfigs(page, limit, category)));
    }

    @PostMapping("/diy-configs")
    @Operation(summary = "创建DIY配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createDiyConfig(@Valid @RequestBody CreateDiyConfigRequest request) {
        return Result.success(adminService.createDiyConfig(request));
    }

    @PutMapping("/diy-configs/{id}")
    @Operation(summary = "更新DIY配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateDiyConfig(@PathVariable Long id, @Valid @RequestBody UpdateDiyConfigRequest request) {
        return Result.success(adminService.updateDiyConfig(id, request));
    }

    @DeleteMapping("/diy-configs/{id}")
    @Operation(summary = "删除DIY配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteDiyConfig(@PathVariable Long id) {
        adminService.deleteDiyConfig(id);
        return Result.success();
    }

    // ==================== DIY推荐配置管理 ====================
    @GetMapping("/diy-recommendations")
    @Operation(summary = "获取DIY推荐配置列表")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<Map<String, Object>>> getDiyRecommendations(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) String scenario
    ) {
        return Result.success(PageResult.of(adminService.getDiyRecommendations(page, limit, scenario)));
    }

    @PostMapping("/diy-recommendations")
    @Operation(summary = "创建DIY推荐配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> createDiyRecommendation(@Valid @RequestBody CreateDiyRecommendationRequest request) {
        return Result.success(adminService.createDiyRecommendation(request));
    }

    @PutMapping("/diy-recommendations/{id}")
    @Operation(summary = "更新DIY推荐配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Map<String, Object>> updateDiyRecommendation(@PathVariable Long id, @Valid @RequestBody UpdateDiyRecommendationRequest request) {
        return Result.success(adminService.updateDiyRecommendation(id, request));
    }

    @DeleteMapping("/diy-recommendations/{id}")
    @Operation(summary = "删除DIY推荐配置")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteDiyRecommendation(@PathVariable Long id) {
        adminService.deleteDiyRecommendation(id);
        return Result.success();
    }
}
