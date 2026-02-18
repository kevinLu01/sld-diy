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

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 解决方案ID
     */
    @TableField(exist = false)
    private Long solutionId;

    /**
     * 项目名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 自定义场景名称
     */
    @TableField("custom_scenario_name")
    private String customScenarioName;

    /**
     * 温度范围
     */
    @TableField(exist = false)
    private String temperatureRange;

    /**
     * 制冷量
     */
    @TableField(exist = false)
    private Double coolingCapacity;

    /**
     * 容量单位
     */
    @TableField(exist = false)
    private String capacityUnit;

    /**
     * 体积
     */
    @TableField(exist = false)
    private Double volume;

    /**
     * 体积单位
     */
    @TableField(exist = false)
    private String volumeUnit;

    /**
     * 环境温度
     */
    @TableField(exist = false)
    private String ambientTemp;

    /**
     * 选项（JSON）
     */
    @TableField("requirements")
    private String options;

    /**
     * 总价
     */
    @TableField("total_price")
    private Double totalPrice;

    /**
     * 状态 (draft/saved)
     */
    private Integer status;

    /**
     * 是否分享
     */
    private Boolean shared;

    /**
     * 分享模式：public/private_offer
     */
    @TableField("share_mode")
    private String shareMode;

    /**
     * 分享Token
     */
    @TableField("share_token")
    private String shareToken;

    /**
     * 分享过期时间
     */
    @TableField("share_expires_at")
    private LocalDateTime shareExpiresAt;

    /**
     * 报价金额（折扣后）
     */
    @TableField("quoted_total_price")
    private Double quotedTotalPrice;

    /**
     * 折扣比例（0.15 = 85折）
     */
    @TableField("discount_rate")
    private Double discountRate;

    /**
     * 立减金额
     */
    @TableField("discount_amount")
    private Double discountAmount;

    /**
     * 私发说明
     */
    @TableField("private_note")
    private String privateNote;

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

    // Keep backward compatibility with tests/service code that still sets status by string.
    public void setStatus(String status) {
        if (status == null) {
            this.status = null;
            return;
        }
        if ("draft".equalsIgnoreCase(status)) {
            this.status = 0;
            return;
        }
        if ("saved".equalsIgnoreCase(status)) {
            this.status = 1;
            return;
        }
        this.status = Integer.valueOf(status);
    }
}
