package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 推荐配置实体
 */
@Data
@TableName("DiyRecommendation")
public class DiyRecommendation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String scenario;

    private String productType;

    private Long categoryId;

    private Integer priority;

    private Boolean isRequired;

    private Integer minQuantity;

    private Integer maxQuantity;

    private Boolean isActive;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private BigDecimal coolingCapacityMin;

    @TableField(exist = false)
    private BigDecimal coolingCapacityMax;

    @TableField(exist = false)
    private String temperatureRange;

    @TableField(exist = false)
    private String productIds;

    @TableField(exist = false)
    private BigDecimal totalPrice;

    @TableField(exist = false)
    private String energyEfficiency;

    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private Integer status;

    @TableField(exist = false)
    private Integer deleted;
}
