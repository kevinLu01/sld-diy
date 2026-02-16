package com.sld.backend.modules.solution.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 解决方案响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "解决方案响应")
public class SolutionVO {

    @Schema(description = "解决方案ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "场景")
    private String scenario;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "图片列表")
    private String images;

    @Schema(description = "温度范围")
    private String temperatureRange;

    @Schema(description = "容量范围")
    private String capacityRange;

    @Schema(description = "特性")
    private List<String> features;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "安装指南")
    private String installationGuide;

    @Schema(description = "使用次数")
    private Integer usageCount;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "评分")
    private BigDecimal rating;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "产品列表")
    private List<ProductItemVO> products;

    /**
     * 方案产品项 VO
     */
    @Data
    public static class ProductItemVO {
        private Long productId;
        private Integer quantity;
        private String notes;
        private Boolean isRequired;
        private ProductInfoVO product;
    }

    /**
     * 产品信息 VO
     */
    @Data
    public static class ProductInfoVO {
        private Long id;
        private String name;
        private BigDecimal price;
        private String images;
    }
}
