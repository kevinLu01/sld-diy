package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.common.enums.ProductStatus;
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
    private Long brandId;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 图片（JSON数组）
     */
    private String images;

    /**
     * 视频
     */
    private String video;

    /**
     * 3D模型
     */
    private String model3d;

    /**
     * 规格（JSON）
     */
    private String specifications;

    /**
     * 描述
     */
    private String description;

    /**
     * 销量
     */
    private Integer salesCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 评价数
     */
    private Integer reviewCount;

    /**
     * 状态
     */
    private ProductStatus status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
