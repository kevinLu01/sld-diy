package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.common.enums.SolutionIndustry;
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
    private SolutionIndustry industry;

    /**
     * 场景
     */
    private String scenario;

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
     * 描述
     */
    private String description;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 安装指导
     */
    private String installationGuide;

    /**
     * 技术文档（JSON数组）
     */
    private String technicalDocs;

    /**
     * 案例列表（JSON数组）
     */
    private String cases;

    /**
     * 使用次数
     */
    private Integer usageCount;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 排序
     */
    private Integer sortOrder;

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
