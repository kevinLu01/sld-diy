package com.sld.backend.modules.admin.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.service.dto.request.UpdateServiceStatusRequest;
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
 * 管理后台 - 服务工单管理
 */
@RestController
@RequestMapping("/api/v1/admin/service-requests")
@RequiredArgsConstructor
@Tag(name = "管理员-服务工单", description = "后台服务工单状态管理")
public class AdminServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @GetMapping
    @Operation(summary = "后台分页查询服务工单")
    public Result<PageResult<ServiceRequestVO>> list(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        return Result.success(PageResult.of(serviceRequestService.adminListServiceRequests(status, page, limit)));
    }

    @PutMapping("/{requestNo}/status")
    @Operation(summary = "后台更新工单状态")
    public Result<ServiceRequestVO> updateStatus(
        @PathVariable String requestNo,
        @Parameter(description = "操作人ID") @CurrentUserId Long operatorId,
        @Valid @RequestBody UpdateServiceStatusRequest request
    ) {
        return Result.success(serviceRequestService.adminUpdateStatus(requestNo, operatorId, request));
    }
}

