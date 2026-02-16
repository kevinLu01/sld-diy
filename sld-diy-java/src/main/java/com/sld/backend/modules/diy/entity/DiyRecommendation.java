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

    private String scenario;

    @TableField("product_type")
    private String productType;

    @TableField("category_id")
    private Long categoryId;

    private Integer priority;

    @TableField("is_required")
    private Boolean isRequired;

    private Integer minQuantity;

    private Integer maxQuantity;

    private Boolean isActive;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(value = "cooling_capacity_min", exist = false)
    private BigDecimal coolingCapacityMin;

    @TableField(value = "cooling_capacity_max", exist = false)
    private BigDecimal coolingCapacityMax;

    @TableField(value = "temperature_range", exist = false)
    private String temperatureRange;

    @TableField(value = "product_ids", exist = false)
    private String productIds;

    @TableField(value = "total_price", exist = false)
    private BigDecimal totalPrice;

    @TableField(value = "energy_efficiency", exist = false)
    private String energyEfficiency;

    @TableField(exist = false)
    private String description;

    @TableField(exist = false)
    private Integer status;
}
