package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DIY 配置实体
 */
@Data
@TableName("DiyConfig")
public class DiyConfig {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分类
     */
    private String category;

    /**
     * 配置键
     */
    @TableField("`key`")
    private String key;

    /**
     * 标签
     */
    private String label;

    /**
     * 配置值
     */
    private String value;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;

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
}
