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
        Cart cart = getOrCreateCart(userId);
        
        List<CartItem> items = cartItemMapper.selectList(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartId, cart.getId())
                .orderByDesc(CartItem::getCreatedAt)
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
        if (product.getStockQuantity() < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }

        Cart cart = getOrCreateCart(userId);

        // 检查购物车是否已有该商品
        CartItem existItem = cartItemMapper.selectOne(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartId, cart.getId())
                .eq(CartItem::getProductId, productId)
        );

        if (existItem != null) {
            existItem.setQuantity(existItem.getQuantity() + quantity);
            cartItemMapper.updateById(existItem);
            return toVO(existItem);
        }

        CartItem item = new CartItem();
        item.setCartId(cart.getId());
        item.setProductId(productId);
        item.setQuantity(quantity);
        cartItemMapper.insert(item);
        return toVO(item);
    }

    @Override
    public CartItemVO updateItem(Long userId, Long id, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        
        CartItem item = cartItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        // 验证权限：只能修改自己的购物车项
        if (!item.getCartId().equals(cart.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权修改此购物车项");
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
        Cart cart = getOrCreateCart(userId);
        cartItemMapper.delete(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getCartId, cart.getId())
        );
    }

    private Cart getOrCreateCart(Long userId) {
        Cart cart = cartMapper.selectByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cartMapper.insert(cart);
        }
        return cart;
    }

    private CartItemVO toVO(CartItem item) {
        Product product = productMapper.selectById(item.getProductId());
        return CartItemVO.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productName(product != null ? product.getName() : null)
            .sku(product != null ? product.getSku() : null)
            .price(product != null ? product.getPrice() : null)
            .quantity(item.getQuantity())
            .total(product != null && product.getPrice() != null ? 
                product.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())) : null)
            .productImage(product != null && product.getImages() != null ? 
                product.getImages().split(",")[0] : null)
            .build();
    }
}