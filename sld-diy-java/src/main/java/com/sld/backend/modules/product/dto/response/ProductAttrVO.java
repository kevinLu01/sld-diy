package com.sld.backend.modules.product.dto.response;

import lombok.Data;

/**
 * 产品属性VO
 */
@Data
public class ProductAttrVO {

    private Long id;
    private String attrName;
    private String attrValue;
    private String unit;
}
