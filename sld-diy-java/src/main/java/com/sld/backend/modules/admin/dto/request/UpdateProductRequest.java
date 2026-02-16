package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新产品请求
 */
@Data
public class UpdateProductRequest {

    private String name;
    private String sku;
    private Long categoryId;
    private Long brandId;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private String images;
    private String status;
}
