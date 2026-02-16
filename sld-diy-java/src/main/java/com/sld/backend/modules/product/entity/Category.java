package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类实体
 */
@Data
@TableName("Category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * Slug（URL友好名称）
     */
    private String slug;

    /**
     * 图标
     */
    private String icon;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private Integer count;

    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private Integer deleted;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
