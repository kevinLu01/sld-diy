package com.sld.backend.modules.service.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.service.dto.request.CreateServiceRequest;
import com.sld.backend.modules.service.dto.request.RateServiceRequest;
import com.sld.backend.modules.service.dto.response.ServiceRequestVO;
import com.sld.backend.modules.service.service.ServiceRequestService;
import com.sld.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 服务请求控制器
 */
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
@Tag(name = "服务管理", description = "服务请求相关接口")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @PostMapping("/requests")
    @Operation(summary = "创建服务请求")
    public Result<ServiceRequestVO> createServiceRequest(
            @Parameter(description = "用户ID") @CurrentUserId Long userId,
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return Result.success(serviceRequestService.createServiceRequest(userId, request));
    }

    @GetMapping("/requests")
    @Operation(summary = "获取服务请求列表")
    public Result<PageResult<ServiceRequestVO>> listServiceRequests(
            @Parameter(description = "用户ID") @CurrentUserId Long userId,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long limit
    ) {
        return Result.success(PageResult.of(serviceRequestService.listServiceRequests(userId, status, page, limit)));
    }

    @GetMapping("/requests/{requestNo}")
    @Operation(summary = "获取服务请求详情")
    public Result<ServiceRequestVO> getServiceRequest(
            @PathVariable String requestNo,
            @Parameter(description = "用户ID") @CurrentUserId Long userId
    ) {
        return Result.success(serviceRequestService.getServiceRequest(requestNo, userId));
    }

    @PostMapping("/requests/{requestNo}/rate")
    @Operation(summary = "评价服务")
    public Result<Void> rateService(
            @PathVariable String requestNo,
            @Parameter(description = "用户ID") @CurrentUserId Long userId,
            @Valid @RequestBody RateServiceRequest request
    ) {
        serviceRequestService.rateService(requestNo, userId, request);
        return Result.success();
    }
}
