package com.sld.backend.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 轮播图实体
 */
@Data
@TableName("Banner")
public class Banner {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 位置：home-首页，category-分类页等
     */
    private String position;

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
    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
