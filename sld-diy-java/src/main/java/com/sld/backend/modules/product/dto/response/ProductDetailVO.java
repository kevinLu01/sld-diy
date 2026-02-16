package com.sld.backend.modules.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 产品详情响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "产品详情响应")
public class ProductDetailVO {

    @Schema(description = "产品ID")
    private Long id;

    @Schema(description = "SKU")
    private String sku;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "品牌ID")
    private Long brandId;

    @Schema(description = "品牌名称")
    private String brandName;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "图片列表")
    private List<String> images;

    @Schema(description = "视频")
    private String video;

    @Schema(description = "3D模型")
    private String model3d;

    @Schema(description = "规格（JSON）")
    private Map<String, Object> specifications;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "销量")
    private Integer salesCount;

    @Schema(description = "评分")
    private BigDecimal rating;

    @Schema(description = "评价数")
    private Integer reviewCount;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "兼容产品列表")
    private List<CompatibleProductVO> compatibleProducts;

    @Schema(description = "评价统计")
    private ReviewStatsVO reviews;
}
