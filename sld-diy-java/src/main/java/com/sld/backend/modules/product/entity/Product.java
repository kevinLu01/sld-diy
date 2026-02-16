package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品实体
 */
@Data
@TableName("Product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sku;

    private String name;

    private Long brandId;

    private Long categoryId;

    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private BigDecimal costPrice;

    @TableField("stockQuantity")
    private Integer stock;

    private String unit;

    private String images;

    @TableField("videoUrl")
    private String video;

    @TableField("model3dUrl")
    private String model3d;

    private String status;

    private Integer viewCount;

    private Integer salesCount;

    private BigDecimal rating;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String specifications;

    @TableField(exist = false)
    private Integer reviewCount;

    @TableField(exist = false)
    private Integer sortOrder;

    @TableField(exist = false)
    private Integer deleted;
}
