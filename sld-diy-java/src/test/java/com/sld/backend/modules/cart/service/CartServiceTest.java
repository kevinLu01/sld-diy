package com.sld.backend.modules.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.enums.ProductStatus;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.cart.dto.response.CartItemVO;
import com.sld.backend.modules.cart.entity.Cart;
import com.sld.backend.modules.cart.entity.CartItem;
import com.sld.backend.modules.cart.mapper.CartItemMapper;
import com.sld.backend.modules.cart.mapper.CartMapper;
import com.sld.backend.modules.cart.service.impl.CartServiceImpl;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 购物车服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("购物车服务测试")
class CartServiceTest {

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartItem testCartItem;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // 初始化测试产品
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("DF-001");
        testProduct.setName("Danfoss 压缩机");
        testProduct.setPrice(new BigDecimal("2500.00"));
        testProduct.setOriginalPrice(new BigDecimal("3000.00"));
        testProduct.setStock(100);
        testProduct.setSalesCount(50);
        testProduct.setRating(new BigDecimal("4.5"));
        testProduct.setReviewCount(10);
        testProduct.setStatus(ProductStatus.ON_SHELF);
        testProduct.setCreateTime(LocalDateTime.now());

        // 初始化测试购物车项
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUserId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setProductName("Danfoss 压缩机");
        testCartItem.setSku("DF-001");
        testCartItem.setPrice(new BigDecimal("2500.00"));
        testCartItem.setQuantity(2);
        testCartItem.setProductImage("https://example.com/product1.jpg");
        testCartItem.setCreateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("获取购物车列表 - 成功")
    void testGetCart_Success() {
        // Arrange
        when(cartMapper.selectByUserId(1L)).thenReturn(new Cart());
        when(cartItemMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(testCartItem));

        // Act
        List<CartItemVO> result = cartService.getCart(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getProductId()).isEqualTo(1L);
        assertThat(result.get(0).getProductName()).isEqualTo("Danfoss 压缩机");
        assertThat(result.get(0).getQuantity()).isEqualTo(2);
        assertThat(result.get(0).getPrice()).isEqualTo(new BigDecimal("2500.00"));
        assertThat(result.get(0).getTotal()).isEqualTo(new BigDecimal("5000.00"));

        verify(cartItemMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取购物车列表 - 空结果")
    void testGetCart_EmptyResult() {
        // Arrange
        when(cartMapper.selectByUserId(1L)).thenReturn(new Cart());
        when(cartItemMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(List.of());

        // Act
        List<CartItemVO> result = cartService.getCart(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(cartItemMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("添加购物车项 - 成功（新商品）")
    void testAddItem_Success_NewProduct() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(cartItemMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(cartItemMapper.insert(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem item = invocation.getArgument(0);
            item.setId(1L);
            return 1;
        });

        // Act
        CartItemVO result = cartService.addItem(1L, 1L, 2);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("2500.00"));

        verify(productMapper).selectById(1L);
        verify(cartItemMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(cartItemMapper).insert(any(CartItem.class));
    }

    @Test
    @DisplayName("添加购物车项 - 成功（已存在商品，更新数量）")
    void testAddItem_Success_ExistingProduct() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(cartItemMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testCartItem);
        when(cartItemMapper.updateById(any(CartItem.class))).thenReturn(1);

        // Act
        CartItemVO result = cartService.addItem(1L, 1L, 3);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(5); // 2 + 3
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("2500.00"));

        verify(productMapper).selectById(1L);
        verify(cartItemMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(cartItemMapper).updateById(any(CartItem.class));
        verify(cartItemMapper, never()).insert(any(CartItem.class));
    }

    @Test
    @DisplayName("添加购物车项 - 产品不存在")
    void testAddItem_ProductNotFound() {
        // Arrange
        when(productMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> cartService.addItem(1L, 999L, 2)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productMapper).selectById(999L);
        verify(cartItemMapper, never()).selectOne(any(LambdaQueryWrapper.class));
        verify(cartItemMapper, never()).insert(any(CartItem.class));
    }

    @Test
    @DisplayName("更新购物车项数量 - 成功")
    void testUpdateItem_Success() {
        // Arrange
        when(cartItemMapper.selectById(1L)).thenReturn(testCartItem);
        when(cartItemMapper.updateById(any(CartItem.class))).thenReturn(1);

        // Act
        CartItemVO result = cartService.updateItem(1L, 5);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(5);

        verify(cartItemMapper).selectById(1L);
        verify(cartItemMapper).updateById(any(CartItem.class));
    }

    @Test
    @DisplayName("更新购物车项数量 - 购物车项不存在")
    void testUpdateItem_NotFound() {
        // Arrange
        when(cartItemMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> cartService.updateItem(999L, 5)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND.getCode());

        verify(cartItemMapper).selectById(999L);
        verify(cartItemMapper, never()).updateById(any(CartItem.class));
    }

    @Test
    @DisplayName("删除购物车项 - 成功")
    void testDeleteItem_Success() {
        // Arrange
        when(cartItemMapper.deleteById(1L)).thenReturn(1);

        // Act
        cartService.deleteItem(1L);

        // Assert
        verify(cartItemMapper).deleteById(1L);
    }

    @Test
    @DisplayName("删除购物车项 - 不存在的项")
    void testDeleteItem_NotExists() {
        // Arrange
        when(cartItemMapper.deleteById(999L)).thenReturn(0);

        // Act
        cartService.deleteItem(999L);

        // Assert
        verify(cartItemMapper).deleteById(999L);
    }

    @Test
    @DisplayName("添加购物车项 - 数量为1")
    void testAddItem_QuantityOne() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(cartItemMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        when(cartItemMapper.insert(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem item = invocation.getArgument(0);
            item.setId(1L);
            return 1;
        });

        // Act
        CartItemVO result = cartService.addItem(1L, 1L, 1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(1);

        verify(productMapper).selectById(1L);
        verify(cartItemMapper).insert(any(CartItem.class));
    }

    @Test
    @DisplayName("更新购物车项数量 - 数量为0时删除")
    void testUpdateItem_ZeroQuantity() {
        // Arrange
        when(cartItemMapper.selectById(1L)).thenReturn(testCartItem);
        when(cartItemMapper.deleteById(1L)).thenReturn(1);

        // Act
        CartItemVO result = cartService.updateItem(1L, 0);

        // Assert - 当数量<=0时，实现会删除该项并返回null
        assertThat(result).isNull();

        verify(cartItemMapper).selectById(1L);
        verify(cartItemMapper).deleteById(1L);
        verify(cartItemMapper, never()).updateById(any(CartItem.class));
    }

    @Test
    @DisplayName("获取购物车列表 - 多个商品")
    void testGetCart_MultipleItems() {
        // Arrange
        CartItem item2 = new CartItem();
        item2.setId(2L);
        item2.setUserId(1L);
        item2.setProductId(2L);
        item2.setProductName("冷凝器");
        item2.setSku("CD-002");
        item2.setPrice(new BigDecimal("1500.00"));
        item2.setQuantity(1);
        item2.setCreateTime(LocalDateTime.now());

        when(cartMapper.selectByUserId(1L)).thenReturn(new Cart());
        when(cartItemMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(testCartItem, item2));

        // Act
        List<CartItemVO> result = cartService.getCart(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Danfoss 压缩机");
        assertThat(result.get(1).getProductName()).isEqualTo("冷凝器");

        verify(cartItemMapper).selectList(any(LambdaQueryWrapper.class));
    }
}
