package com.sld.backend.modules.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("`Order`")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private Long diyProjectId;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal shippingFee;

    private BigDecimal installationFee;

    private BigDecimal finalAmount;

    private String status;

    private String paymentMethod;

    private LocalDateTime paymentTime;

    private String shippingInfo;

    private String notes;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String recipient;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String province;

    @TableField(exist = false)
    private String city;

    @TableField(exist = false)
    private String district;

    @TableField(exist = false)
    private String address;

    @TableField(exist = false)
    private String postalCode;

    @TableField(exist = false)
    private Boolean needInstallation;

    @TableField(exist = false)
    private LocalDateTime payTime;

    @TableField(exist = false)
    private LocalDateTime shipTime;

    @TableField(exist = false)
    private LocalDateTime deliverTime;

    @TableField(exist = false)
    private LocalDateTime cancelTime;

    @TableField(exist = false)
    private Integer deleted;
}
