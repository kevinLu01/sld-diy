package com.sld.backend.modules.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.common.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@Data
@TableName("t_order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("user_id")
    private Long userId;

    @TableField("diy_project_id")
    private Long diyProjectId;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("discount_amount")
    private BigDecimal discountAmount;

    @TableField("shipping_fee")
    private BigDecimal shippingFee;

    @TableField("installation_fee")
    private BigDecimal installationFee;

    @TableField("final_amount")
    private BigDecimal finalAmount;

    private OrderStatus status;

    private String paymentMethod;

    private LocalDateTime paymentTime;

    private String shippingInfo;

    private String notes;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

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

    @TableField(value = "need_installation", exist = false)
    private Boolean needInstallation;

    @TableField(value = "pay_time", exist = false)
    private LocalDateTime payTime;

    @TableField(value = "ship_time", exist = false)
    private LocalDateTime shipTime;

    @TableField(value = "deliver_time", exist = false)
    private LocalDateTime deliverTime;

    @TableField(value = "cancel_time", exist = false)
    private LocalDateTime cancelTime;
}
