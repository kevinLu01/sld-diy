package com.sld.backend.modules.diy.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.diy.dto.request.DiyRecommendRequest;
import com.sld.backend.modules.diy.dto.request.SaveDiyProjectRequest;
import com.sld.backend.modules.diy.dto.response.DiyConfigVO;
import com.sld.backend.modules.diy.dto.response.DiyProjectVO;
import com.sld.backend.modules.diy.dto.response.DiyRecommendResponse;
import com.sld.backend.modules.diy.dto.response.DiyShareResponse;
import com.sld.backend.modules.diy.service.DiyService;
import com.sld.backend.modules.solution.dto.response.SolutionVO;
import com.sld.backend.modules.solution.service.SolutionService;
import com.sld.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * DIY 控制器
 */
@RestController
@RequestMapping("/api/v1/diy")
@RequiredArgsConstructor
@Tag(name = "DIY管理", description = "DIY相关接口")
public class DiyController {

    private final DiyService diyService;
    private final SolutionService solutionService;

    @GetMapping("/config")
    @Operation(summary = "获取DIY配置")
    public Result<Map<String, Object>> getDiyConfig() {
        return Result.success(diyService.getDiyConfig());
    }

    @GetMapping("/solutions")
    @Operation(summary = "获取解决方案列表")
    public Result<PageResult<SolutionVO>> listSolutions(
        @Parameter(description = "行业") @RequestParam(required = false) String industry,
        @Parameter(description = "场景") @RequestParam(required = false) String scenario,
        @Parameter(description = "温度范围") @RequestParam(required = false) String temperatureRange,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "10") Long limit
    ) {
        return Result.success(solutionService.listSolutions(industry, scenario, temperatureRange, page, limit));
    }

    @PostMapping("/recommend")
    @Operation(summary = "智能推荐配件")
    public Result<DiyRecommendResponse> recommend(@Valid @RequestBody DiyRecommendRequest request) {
        return Result.success(diyService.recommend(request));
    }

    @PostMapping("/validate-compatibility")
    @Operation(summary = "验证配件兼容性")
    public Result<Map<String, Object>> validateCompatibility(@RequestBody java.util.List<Long> productIds) {
        return Result.success(diyService.validateCompatibility(productIds));
    }

    @PostMapping("/projects")
    @Operation(summary = "保存DIY方案")
    public Result<DiyProjectVO> saveProject(
        @Parameter(description = "用户ID") @CurrentUserId Long userId,
        @Valid @RequestBody SaveDiyProjectRequest request
    ) {
        return Result.success(diyService.saveProject(userId, request));
    }

    @GetMapping("/projects")
    @Operation(summary = "获取用户DIY方案列表")
    public Result<PageResult<DiyProjectVO>> listProjects(
        @Parameter(description = "用户ID") @CurrentUserId Long userId,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "10") Long limit
    ) {
        return Result.success(PageResult.of(diyService.listProjects(userId, page, limit)));
    }

    @GetMapping("/projects/{id}")
    @Operation(summary = "获取DIY方案详情")
    public Result<DiyProjectVO> getProject(@PathVariable Long id) {
        return Result.success(diyService.getProject(id));
    }

    @PostMapping("/projects/{id}/share")
    @Operation(summary = "分享DIY方案")
    public Result<DiyShareResponse> shareProject(
        @PathVariable Long id,
        @Parameter(description = "用户ID") @CurrentUserId Long userId
    ) {
        return Result.success(diyService.shareProject(id, userId));
    }

    @GetMapping("/share/{token}")
    @Operation(summary = "通过分享Token获取DIY方案")
    public Result<DiyProjectVO> getProjectByShareToken(@PathVariable String token) {
        return Result.success(diyService.getProjectByShareToken(token));
    }
}
