package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 产品属性实体
 */
@Data
@TableName("t_product_attr")
public class ProductAttr {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 属性名
     */
    @TableField("attr_name")
    private String attrName;

    /**
     * 属性值
     */
    @TableField("attr_value")
    private String attrValue;

    /**
     * 单位
     */
    private String unit;

    /**
     * 排序
     */
    private Integer sortOrder;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
