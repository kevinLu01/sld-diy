package com.sld.backend.modules.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.cart.dto.response.CartItemVO;
import com.sld.backend.modules.cart.entity.CartItem;
import com.sld.backend.modules.cart.mapper.CartItemMapper;
import com.sld.backend.modules.cart.service.CartService;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final ProductMapper productMapper;

    @Override
    public List<CartItemVO> getCart(Long userId) {
        List<CartItem> items = cartItemMapper.selectList(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getDeleted, 0)
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

        // 检查购物车是否已有该商品
        CartItem existItem = cartItemMapper.selectOne(
            new LambdaQueryWrapper<CartItem>()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getProductId, productId)
                .eq(CartItem::getDeleted, 0)
        );

        if (existItem != null) {
            existItem.setQuantity(existItem.getQuantity() + quantity);
            existItem.setUpdatedAt(LocalDateTime.now());
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
        item.setProductImage(extractPrimaryImage(product.getImages()));
        item.setDeleted(0);
        cartItemMapper.insert(item);
        return toVO(item);
    }

    @Override
    public CartItemVO updateItem(Long userId, Long id, Integer quantity) {
        CartItem item = cartItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        // 验证权限：只能修改自己的购物车项
        if (!item.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权修改此购物车项");
        }
        
        if (quantity <= 0) {
            cartItemMapper.deleteById(id);
            return null;
        }
        
        item.setQuantity(quantity);
        item.setUpdatedAt(LocalDateTime.now());
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

    private CartItemVO toVO(CartItem item) {
        Product product = productMapper.selectById(item.getProductId());
        BigDecimal price = item.getPrice() != null
            ? item.getPrice()
            : (product != null ? product.getPrice() : null);
        String image = StrUtil.isNotBlank(item.getProductImage())
            ? item.getProductImage()
            : (product != null ? extractPrimaryImage(product.getImages()) : null);
        return CartItemVO.builder()
            .id(item.getId())
            .productId(item.getProductId())
            .productName(item.getProductName() != null ? item.getProductName() : (product != null ? product.getName() : null))
            .sku(item.getSku() != null ? item.getSku() : (product != null ? product.getSku() : null))
            .price(price)
            .quantity(item.getQuantity())
            .total(price != null ? price.multiply(new BigDecimal(item.getQuantity())) : null)
            .productImage(image)
            .build();
    }

    private String extractPrimaryImage(String images) {
        if (StrUtil.isBlank(images)) {
            return null;
        }
        String value = images.trim();
        if (value.startsWith("[")) {
            try {
                java.util.List<String> list = cn.hutool.json.JSONUtil.toList(value, String.class);
                return list.isEmpty() ? null : list.get(0);
            } catch (Exception ignored) {
            }
        }
        if (value.contains(",")) {
            return value.split(",")[0].trim();
        }
        return value;
    }
}
