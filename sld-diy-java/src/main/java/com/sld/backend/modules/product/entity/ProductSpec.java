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
    private Long productId;

    /**
     * 规格键
     */
    private String specKey;

    /**
     * 规格值
     */
    private String specValue;

    /**
     * 单位
     */
    private String unit;

    /**
     * 排序
     */
    private Integer sortOrder;
}
