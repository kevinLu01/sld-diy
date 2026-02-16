package com.sld.backend.modules.product.dto.response;

import lombok.Data;

/**
 * 产品规格VO
 */
@Data
public class ProductSpecVO {

    private Long id;
    private String specKey;
    private String specValue;
    private String unit;
}
