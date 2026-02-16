package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.modules.product.entity.Category;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DIY 推荐配置实体
 */
@Data
@TableName("DiyRecommendation")
public class DiyRecommendation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 最小数量
     */
    private Integer minQuantity;

    /**
     * 最大数量
     */
    private Integer maxQuantity;

    /**
     * 是否启用
     */
    private Boolean isActive;

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

    /**
     * 分类信息（非数据库字段）
     */
    @TableField(exist = false)
    private Category category;
}
