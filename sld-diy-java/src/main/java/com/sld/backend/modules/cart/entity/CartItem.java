package com.sld.backend.modules.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车实体
 */
@Data
@TableName("t_cart_item")
public class CartItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 购物车ID
     */
    @TableField("cart_id")
    private Long cartId;

    @TableField("product_id")
    private Long productId;

    private Integer quantity;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(value = "user_id", exist = false)
    private Long userId;

    @TableField(exist = false)
    private BigDecimal price;

    @TableField(exist = false)
    private String sku;

    @TableField(value = "product_name", exist = false)
    private String productName;

    @TableField(value = "product_image", exist = false)
    private String productImage;
}
