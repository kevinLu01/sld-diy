package com.sld.backend.modules.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单项实体
 */
@Data
@TableName("OrderItem")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    @TableField("productSku")
    private String sku;

    private BigDecimal price;

    private Integer quantity;

    @TableField("subtotal")
    private BigDecimal total;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String productImage;

    @TableField(exist = false)
    private String specifications;

    @TableField(exist = false)
    private Integer deleted;

    @TableField(exist = false)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
