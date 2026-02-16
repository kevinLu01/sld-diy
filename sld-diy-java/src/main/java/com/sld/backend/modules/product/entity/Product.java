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

    /**
     * SKU
     */
    private String sku;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 描述
     */
    private String description;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    private Integer stockQuantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 图片（JSON数组）
     */
    private String images;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 3D模型URL
     */
    private String model3dUrl;

    /**
     * 状态 (active/inactive)
     */
    private String status;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 销量
     */
    private Integer salesCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
