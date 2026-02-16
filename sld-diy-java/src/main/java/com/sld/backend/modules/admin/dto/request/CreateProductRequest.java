package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建产品请求
 */
@Data
public class CreateProductRequest {

    @NotBlank(message = "产品名称不能为空")
    private String name;

    @NotBlank(message = "SKU不能为空")
    private String sku;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private Long brandId;

    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private String images;

    private String status;
}
