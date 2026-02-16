package com.sld.backend.modules.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.product.dto.response.ProductDetailVO;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.product.dto.response.ReviewVO;
import com.sld.backend.modules.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 产品控制器
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "产品管理", description = "产品相关接口")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "获取产品列表")
    public Result<PageResult<ProductVO>> listProducts(
        @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
        @Parameter(description = "品牌") @RequestParam(required = false) String brand,
        @Parameter(description = "搜索关键词") @RequestParam(required = false) String search,
        @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long page,
        @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") Long limit,
        @Parameter(description = "排序：sales/price_asc/price_desc/new") @RequestParam(defaultValue = "new") String sort
    ) {
        Page<ProductVO> productPage = productService.listProducts(categoryId, brand, search, page, limit, sort);
        return Result.success(PageResult.of(productPage));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取产品详情")
    public Result<ProductDetailVO> getProduct(@PathVariable Long id) {
        return Result.success(productService.getProductDetail(id));
    }

    @GetMapping("/{id}/compatibility")
    @Operation(summary = "获取产品兼容性")
    public Result<List<Map<String, Object>>> getCompatibility(@PathVariable Long id) {
        return Result.success(productService.getProductCompatibility(id));
    }

    @GetMapping("/{id}/reviews")
    @Operation(summary = "获取产品评价")
    public Result<PageResult<ReviewVO>> getProductReviews(
        @PathVariable Long id,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "10") Long limit
    ) {
        return Result.success(PageResult.of(productService.getProductReviews(id, page, limit)));
    }
}
