package com.sld.backend.modules.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.modules.product.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车项实体
 */
@Data
@TableName("CartItem")
public class CartItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 购物车ID
     */
    private Long cartId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 产品信息（非数据库字段）
     */
    @TableField(exist = false)
    private Product product;
}
