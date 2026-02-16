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
    private String configKey;

    /**
     * 标签
     */
    private String label;

    /**
     * 配置值
     */
    @TableField("`value`")
    private String configValue;

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

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中 =====

    @TableField(exist = false)
    private Integer deleted;
}
