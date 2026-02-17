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

    @TableField(exist = false)
    private String paymentMethod;

    @TableField(exist = false)
    private LocalDateTime paymentTime;

    @TableField(exist = false)
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

    @TableField("username")
    private String username;

    @TableField("recipient")
    private String recipient;

    @TableField("phone")
    private String phone;

    @TableField("province")
    private String province;

    @TableField("city")
    private String city;

    @TableField("district")
    private String district;

    @TableField("address")
    private String address;

    @TableField("postal_code")
    private String postalCode;

    @TableField("need_installation")
    private Boolean needInstallation;

    @TableField("pay_time")
    private LocalDateTime payTime;

    @TableField("ship_time")
    private LocalDateTime shipTime;

    @TableField("deliver_time")
    private LocalDateTime deliverTime;

    @TableField("cancel_time")
    private LocalDateTime cancelTime;
}
