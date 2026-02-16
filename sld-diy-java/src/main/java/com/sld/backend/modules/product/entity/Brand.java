package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 品牌实体
 */
@Data
@TableName("Brand")
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
    private String country;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
