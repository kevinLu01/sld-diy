package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 方案实体
 */
@Data
@TableName("DiyProject")
public class DiyProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long solutionId;

    private String projectName;

    private String scenario;

    private String temperatureRange;

    private BigDecimal coolingCapacity;

    private String capacityUnit;

    private BigDecimal volume;

    private String volumeUnit;

    private String ambientTemp;

    private String options;

    private BigDecimal totalPrice;

    private String status;

    private Boolean shared;

    private String shareToken;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String requirements;

    @TableField(exist = false)
    private BigDecimal estimatedInstallationFee;

    @TableField(exist = false)
    private String energyEfficiency;

    @TableField(exist = false)
    private String estimatedPowerConsumption;

    @TableField(exist = false)
    private String suggestions;

    @TableField(exist = false)
    private Integer viewCount;

    @TableField(exist = false)
    private Integer usageCount;

    @TableField(exist = false)
    private Integer deleted;
}
