package com.sld.backend.modules.category.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.category.dto.response.CategoryVO;
import com.sld.backend.modules.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取分类列表")
    public Result<List<CategoryVO>> listCategories(
        @Parameter(description = "父分类ID，为空获取所有分类") @RequestParam(required = false) Long parentId
    ) {
        return Result.success(categoryService.listCategories(parentId));
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树")
    public Result<List<CategoryVO>> getCategoryTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情")
    public Result<CategoryVO> getCategory(@PathVariable Long id) {
        return Result.success(categoryService.getCategory(id));
    }
}
