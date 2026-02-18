package com.sld.backend.modules.service.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理员更新工单状态请求
 */
@Data
@Schema(description = "管理员更新工单状态请求")
public class UpdateServiceStatusRequest {

    @NotBlank(message = "状态不能为空")
    @Schema(description = "目标状态: processing/completed/closed")
    private String status;

    @Schema(description = "处理结果")
    private String resolution;

    @Schema(description = "指派工程师ID")
    private Long assignedTo;
}

