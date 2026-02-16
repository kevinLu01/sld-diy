package com.sld.backend.modules.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.cart.dto.response.CartItemVO;
import com.sld.backend.modules.cart.entity.Cart;
import com.sld.backend.modules.cart.entity.CartItem;
import com.sld.backend.modules.cart.mapper.CartItemMapper;
import com.sld.backend.modules.cart.mapper.CartMapper;
import com.sld.backend.modules.cart.service.CartService;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemMapper cartItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    @Override
    public List<CartItemVO> getCart(Long userId) {
        // 确保购物车存在
        getOrCreateCart(userId);
        
        List<CartItem> items = cartItemMapper.selectList(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .orderByDesc(CartItem::getCreateTime)
        );
        return items.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItemVO addItem(Long userId, Long productId, Integer quantity) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 检查库存
        if (product.getStock() < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // 检查购物车是否已有该商品
        CartItem existItem = cartItemMapper.selectOne(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, productId)
        );

        if (existItem != null) {
            existItem.setQuantity(existItem.getQuantity() + quantity);
            cartItemMapper.updateById(existItem);
            return toVO(existItem);
        }

        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        item.setSku(product.getSku());
        item.setProductName(product.getName());
        item.setCreateTime(LocalDateTime.now());
        cartItemMapper.insert(item);
        return toVO(item);
    }

    @Override
    public CartItemVO updateItem(Long id, Integer quantity) {
        CartItem item = cartItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        
        if (quantity <= 0) {
            cartItemMapper.deleteById(id);
            return null;
        }
        
        item.setQuantity(quantity);
        cartItemMapper.updateById(item);
        return toVO(item);
    }

    @Override
    public void deleteItem(Long id) {
        cartItemMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemMapper.delete(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
        );
    }

    private Cart getOrCreateCart(Long userId) {
        Cart cart = cartMapper.selectByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cart);
        }
        return cart;
    }

    private CartItemVO toVO(CartItem item) {
        return CartItemVO.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productName(item.getProductName())
            .sku(item.getSku())
            .price(item.getPrice())
            .quantity(item.getQuantity())
            .total(item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())))
            .productImage(item.getProductImage())
            .build();
    }
}
