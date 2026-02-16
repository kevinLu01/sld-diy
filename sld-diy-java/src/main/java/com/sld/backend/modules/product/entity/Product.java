package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体
 */
@Data
@TableName("t_product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sku;

    private String name;

    @TableField("brand_id")
    private Long brandId;

    @TableField("category_id")
    private Long categoryId;

    private String description;

    private BigDecimal price;

    @TableField("original_price")
    private BigDecimal originalPrice;

    private Integer stock;

    private String images;

    private String video;

    private String model3d;

    private String status;

    @TableField("sales_count")
    private Integer salesCount;

    private BigDecimal rating;

    @TableField("review_count")
    private Integer reviewCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String specifications;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
