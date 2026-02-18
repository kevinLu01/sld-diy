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
    @TableField("brand_id")
    private Long brandId;

    /**
     * 分类ID
     */
    @TableField("category_id")
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
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 成本价
     */
    @TableField(exist = false)
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stockQuantity;

    /**
     * 单位
     */
    @TableField(exist = false)
    private String unit;

    /**
     * 图片（JSON数组）
     */
    private String images;

    /**
     * 视频URL
     */
    @TableField("video")
    private String videoUrl;

    /**
     * 3D模型URL
     */
    @TableField("model3d")
    private String model3dUrl;

    /**
     * 状态 (active/inactive)
     */
    private String status;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 销量
     */
    @TableField("sales_count")
    private Integer salesCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
