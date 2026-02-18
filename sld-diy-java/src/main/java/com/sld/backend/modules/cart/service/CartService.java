package com.sld.backend.modules.cart.service;

import com.sld.backend.modules.cart.dto.response.CartItemVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 获取购物车列表
     */
    List<CartItemVO> getCart(Long userId);

    /**
     * 添加购物车项
     */
    CartItemVO addItem(Long userId, Long productId, Integer quantity);

    /**
     * 更新购物车项数量
     */
    CartItemVO updateItem(Long userId, Long id, Integer quantity);

    /**
     * 删除购物车项
     */
    void deleteItem(Long userId, Long id);

    /**
     * 清空购物车
     */
    void clearCart(Long userId);
}
