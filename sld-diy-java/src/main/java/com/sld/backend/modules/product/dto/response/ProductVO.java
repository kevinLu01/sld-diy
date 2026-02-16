package com.sld.backend.modules.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "产品响应")
public class ProductVO {

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

    @Schema(description = "销量")
    private Integer salesCount;

    @Schema(description = "评分")
    private BigDecimal rating;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "规格（JSON）")
    private String specifications;
}
