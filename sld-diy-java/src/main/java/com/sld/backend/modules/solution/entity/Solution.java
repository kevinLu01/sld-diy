package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 解决方案实体
 */
@Data
@TableName("t_solution")
public class Solution {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 行业
     */
    private String industry;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面图
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 图片列表（JSON数组）
     */
    private String images;

    /**
     * 温度范围
     */
    @TableField("temperature_range")
    private String temperatureRange;

    /**
     * 容量范围
     */
    @TableField("capacity_range")
    private String capacityRange;

    /**
     * 特性（JSON数组）
     */
    private String features;

    /**
     * 总价
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 安装指导
     */
    @TableField("installation_guide")
    private String installationGuide;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 使用次数
     */
    @TableField("usage_count")
    private Integer usageCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 状态 (published/draft)
     */
    private String status;

    /**
     * 创建者ID
     */
    @TableField(exist = false)
    private Long createdBy;

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
}
