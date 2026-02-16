package com.sld.backend.modules.diy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 保存 DIY 方案请求
 */
@Data
@Schema(description = "保存DIY方案请求")
public class SaveDiyProjectRequest {

    @NotBlank(message = "项目名称不能为空")
    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "场景")
    private String scenario;

    @Schema(description = "需求参数")
    private Map<String, Object> requirements;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "预估安装费")
    private BigDecimal estimatedInstallationFee;

    @Schema(description = "是否分享")
    private Boolean shared;
}
