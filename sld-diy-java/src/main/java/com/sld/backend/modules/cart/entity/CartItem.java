package com.sld.backend.modules.cart.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.modules.product.entity.Product;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车项实体
 */
@Data
@TableName("t_cart_item")
public class CartItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 兼容旧测试代码字段，不参与持久化
     */
    @TableField(exist = false)
    private Long cartId;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 冗余字段，便于直接返回购物车明细
     */
    private java.math.BigDecimal price;
    private String sku;
    private String productName;
    private String productImage;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private Integer deleted;

    /**
     * 产品信息（非数据库字段）
     */
    @TableField(exist = false)
    private Product product;
}
