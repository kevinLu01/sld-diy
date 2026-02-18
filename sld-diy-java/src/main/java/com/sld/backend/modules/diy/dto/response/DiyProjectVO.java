package com.sld.backend.modules.diy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DIY 方案响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DIY方案响应")
public class DiyProjectVO {

    @Schema(description = "方案ID")
    private Long id;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "场景")
    private String scenario;

    @Schema(description = "自定义场景名称")
    private String customScenarioName;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "预估安装费")
    private BigDecimal estimatedInstallationFee;

    @Schema(description = "能效等级")
    private String energyEfficiency;

    @Schema(description = "预估能耗")
    private String estimatedPowerConsumption;

    @Schema(description = "是否分享")
    private Boolean shared;

    @Schema(description = "分享模式")
    private String shareMode;

    @Schema(description = "报价金额")
    private BigDecimal quotedTotalPrice;

    @Schema(description = "折扣比例")
    private BigDecimal discountRate;

    @Schema(description = "立减金额")
    private BigDecimal discountAmount;

    @Schema(description = "私发说明")
    private String privateNote;

    @Schema(description = "分享过期时间")
    private String shareExpiresAt;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "使用次数")
    private Integer usageCount;

    @Schema(description = "创建时间")
    private String createTime;
}
