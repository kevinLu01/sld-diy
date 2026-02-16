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

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 安装费
     */
    private BigDecimal installationFee;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    private BigDecimal finalAmount;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * DIY方案ID
     */
    private Long diyProjectId;

    /**
     * 收货人
     */
    private String recipient;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 备注
     */
    private String notes;

    /**
     * 是否需要安装
     */
    private Boolean needInstallation;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    private LocalDateTime shipTime;

    /**
     * 签收时间
     */
    private LocalDateTime deliverTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

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
