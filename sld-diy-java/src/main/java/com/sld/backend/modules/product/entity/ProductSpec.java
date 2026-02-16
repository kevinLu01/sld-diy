package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 产品规格实体
 */
@Data
@TableName("t_product_spec")
public class ProductSpec {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 规格键
     */
    @TableField("spec_key")
    private String specKey;

    /**
     * 规格值
     */
    @TableField("spec_value")
    private String specValue;

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
