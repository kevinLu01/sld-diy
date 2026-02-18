package com.sld.backend.modules.solution.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.solution.dto.response.SolutionCaseVO;
import com.sld.backend.modules.solution.dto.response.SolutionVO;
import com.sld.backend.modules.solution.service.SolutionService;
import com.sld.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 解决方案控制器
 */
@RestController
@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
@Tag(name = "解决方案管理", description = "解决方案相关接口")
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping
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

    @GetMapping("/{id}")
    @Operation(summary = "获取解决方案详情")
    public Result<SolutionVO> getSolution(@PathVariable Long id) {
        return Result.success(solutionService.getSolution(id));
    }

    @GetMapping("/{id}/cases")
    @Operation(summary = "获取解决方案案例")
    public Result<List<SolutionCaseVO>> getSolutionCases(@PathVariable Long id) {
        return Result.success(solutionService.getSolutionCases(id));
    }

    @PostMapping("/{id}/create-order")
    @Operation(summary = "基于解决方案创建订单")
    public Result<OrderVO> createOrderFromSolution(
        @PathVariable Long id,
        @Parameter(description = "用户ID") @CurrentUserId Long userId
    ) {
        return Result.success(solutionService.createOrderFromSolution(id, userId));
    }
}
