package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.common.enums.DiyScenario;
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

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 场景
     */
    private DiyScenario scenario;

    /**
     * 需求（JSON）
     */
    private String requirements;

    /**
     * 总价
     */
    private BigDecimal totalPrice;

    /**
     * 预估安装费
     */
    private BigDecimal estimatedInstallationFee;

    /**
     * 能效等级
     */
    private String energyEfficiency;

    /**
     * 预估能耗
     */
    private String estimatedPowerConsumption;

    /**
     * 建议（JSON数组）
     */
    private String suggestions;

    /**
     * 是否分享
     */
    private Boolean shared;

    /**
     * 分享Token
     */
    private String shareToken;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 使用次数
     */
    private Integer usageCount;

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
