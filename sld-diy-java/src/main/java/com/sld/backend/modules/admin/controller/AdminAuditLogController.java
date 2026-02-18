package com.sld.backend.modules.admin.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.admin.dto.response.AdminAuditLogVO;
import com.sld.backend.modules.admin.service.AdminAuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
@RequiredArgsConstructor
@Tag(name = "管理员-操作日志", description = "后台操作日志查询")
public class AdminAuditLogController {

    private final AdminAuditLogService adminAuditLogService;

    @GetMapping
    @Operation(summary = "查询操作日志")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<PageResult<AdminAuditLogVO>> list(
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit,
        @RequestParam(required = false) Long operatorId,
        @RequestParam(required = false) String action,
        @RequestParam(required = false) String entityType
    ) {
        return Result.success(PageResult.of(adminAuditLogService.query(page, limit, operatorId, action, entityType)));
    }
}
