package com.sld.backend.modules.diy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DIY 推荐响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DIY推荐响应")
public class DiyRecommendResponse {

    @Schema(description = "推荐ID")
    private String recommendationId;

    @Schema(description = "场景")
    private String scenario;

    @Schema(description = "需求参数")
    private Map<String, Object> requirements;

    @Schema(description = "推荐产品")
    private Map<String, List<ProductVO>> products;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "预估安装费")
    private BigDecimal estimatedInstallationFee;

    @Schema(description = "能效等级")
    private String energyEfficiency;

    @Schema(description = "预估能耗")
    private String estimatedPowerConsumption;

    @Schema(description = "建议")
    private List<String> suggestions;

    @Schema(description = "推荐解释")
    private List<ExplanationVO> explanations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "产品响应")
    public static class ProductVO {
        @Schema(description = "产品ID")
        private Long id;

        @Schema(description = "产品名称")
        private String name;

        @Schema(description = "价格")
        private BigDecimal price;

        @Schema(description = "匹配分数")
        private Integer matchScore;

        @Schema(description = "匹配原因")
        private String matchReason;

        @Schema(description = "数量")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "推荐解释")
    public static class ExplanationVO {
        @Schema(description = "产品类型")
        private String productType;

        @Schema(description = "评分")
        private Integer score;

        @Schema(description = "命中原因")
        private String reason;

        @Schema(description = "替代建议")
        private List<String> alternatives;
    }
}
