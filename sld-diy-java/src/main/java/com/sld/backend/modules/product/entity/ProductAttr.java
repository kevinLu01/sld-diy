package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 产品属性实体
 */
@Data
@TableName("ProductAttr")
public class ProductAttr {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 属性值
     */
    private String attrValue;

    /**
     * 单位
     */
    private String unit;

    /**
     * 排序
     */
    private Integer sortOrder;
}
