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
    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String website;

    @TableField(exist = false)
    private Integer productCount;

    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private Integer deleted;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
