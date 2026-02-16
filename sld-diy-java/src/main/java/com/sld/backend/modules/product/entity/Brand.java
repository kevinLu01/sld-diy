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
     * 官网
     */
    private String website;

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 状态
     */
    private Integer status;

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
