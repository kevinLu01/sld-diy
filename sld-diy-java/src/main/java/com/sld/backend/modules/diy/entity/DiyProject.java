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

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 解决方案ID
     */
    private Long solutionId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 温度范围
     */
    private String temperatureRange;

    /**
     * 制冷量
     */
    private Double coolingCapacity;

    /**
     * 容量单位
     */
    private String capacityUnit;

    /**
     * 体积
     */
    private Double volume;

    /**
     * 体积单位
     */
    private String volumeUnit;

    /**
     * 环境温度
     */
    private String ambientTemp;

    /**
     * 选项（JSON）
     */
    private String options;

    /**
     * 总价
     */
    private Double totalPrice;

    /**
     * 状态 (draft/saved)
     */
    private String status;

    /**
     * 是否分享
     */
    private Boolean shared;

    /**
     * 分享Token
     */
    private String shareToken;

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
