package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 解决方案实体
 */
@Data
@TableName("Solution")
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
    private String coverImage;

    /**
     * 图片列表（JSON数组）
     */
    private String images;

    /**
     * 温度范围
     */
    private String temperatureRange;

    /**
     * 容量范围
     */
    private String capacityRange;

    /**
     * 特性（JSON数组）
     */
    private String features;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 安装指导
     */
    private String installationGuide;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 使用次数
     */
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
    private Long createdBy;

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
