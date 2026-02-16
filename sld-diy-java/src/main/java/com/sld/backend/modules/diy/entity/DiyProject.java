package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 方案实体
 */
@Data
@TableName("t_diy_project")
public class DiyProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private Long solutionId;

    @TableField("project_name")
    private String projectName;

    private String scenario;

    private String temperatureRange;

    private BigDecimal coolingCapacity;

    private String capacityUnit;

    private BigDecimal volume;

    private String volumeUnit;

    private String ambientTemp;

    private String options;

    @TableField("total_price")
    private BigDecimal totalPrice;

    private String status;

    private Boolean shared;

    @TableField("share_token")
    private String shareToken;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String requirements;

    @TableField(value = "estimated_installation_fee", exist = false)
    private BigDecimal estimatedInstallationFee;

    @TableField(value = "energy_efficiency", exist = false)
    private String energyEfficiency;

    @TableField(value = "estimated_power_consumption", exist = false)
    private String estimatedPowerConsumption;

    @TableField(exist = false)
    private String suggestions;

    @TableField(value = "view_count", exist = false)
    private Integer viewCount;

    @TableField(value = "usage_count", exist = false)
    private Integer usageCount;
}
