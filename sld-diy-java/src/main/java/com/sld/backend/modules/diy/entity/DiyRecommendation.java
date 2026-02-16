package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 推荐配置实体
 */
@Data
@TableName("t_diy_recommendation")
public class DiyRecommendation {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 制冷量范围最小值
     */
    private BigDecimal coolingCapacityMin;

    /**
     * 制冷量范围最大值
     */
    private BigDecimal coolingCapacityMax;

    /**
     * 温度范围
     */
    private String temperatureRange;

    /**
     * 推荐产品ID列表（JSON）
     */
    private String productIds;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 能效等级
     */
    private String energyEfficiency;

    /**
     * 描述
     */
    private String description;

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
