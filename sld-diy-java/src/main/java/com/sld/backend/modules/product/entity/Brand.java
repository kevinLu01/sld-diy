package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 品牌实体
 */
@Data
@TableName("t_brand")
public class Brand {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * Slug
     */
    private String slug;

    /**
     * Logo
     */
    private String logo;

    /**
     * 描述
     */
    private String description;

    /**
     * 国家
     */
    @TableField("website")
    private String country;

    /**
     * 是否启用
     */
    @TableField("status")
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
