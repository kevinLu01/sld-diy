package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.modules.product.entity.Category;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DIY 推荐配置实体
 */
@Data
@TableName("t_diy_recommendation")
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
    @TableField("product_type")
    private String productType;

    /**
     * 组件角色：main/auxiliary
     */
    @TableField("component_role")
    private String componentRole;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否必选
     */
    @TableField("is_required")
    private Boolean isRequired;

    /**
     * 最小数量
     */
    @TableField("cooling_capacity_min")
    private Integer minQuantity;

    /**
     * 最大数量
     */
    @TableField("cooling_capacity_max")
    private Integer maxQuantity;

    /**
     * 是否启用
     */
    @TableField("is_active")
    private Boolean isActive;

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

    /**
     * 分类信息（非数据库字段）
     */
    @TableField(exist = false)
    private Category category;
}
