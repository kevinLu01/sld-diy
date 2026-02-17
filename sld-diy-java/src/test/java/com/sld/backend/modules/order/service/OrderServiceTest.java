package com.sld.backend.modules.order.service;

import com.sld.backend.common.enums.OrderStatus;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.order.entity.Order;
import com.sld.backend.modules.order.entity.OrderItem;
import com.sld.backend.modules.order.mapper.OrderItemMapper;
import com.sld.backend.modules.order.mapper.OrderMapper;
import com.sld.backend.modules.order.service.impl.OrderServiceImpl;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 订单服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("订单服务测试")
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        // 初始化测试订单 - 使用正确的字段名
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("ORD202401010001");
        testOrder.setUserId(1L);
        testOrder.setTotalAmount(new BigDecimal("5000.00"));
        testOrder.setFinalAmount(new BigDecimal("4800.00"));
        testOrder.setStatus(OrderStatus.PENDING);
        testOrder.setRecipient("张三");
        testOrder.setPhone("13800138000");
        testOrder.setAddress("北京市朝阳区");
        testOrder.setCreateTime(LocalDateTime.now());
        testOrder.setUpdateTime(LocalDateTime.now());

        // 初始化测试订单项 - 使用正确的字段名
        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrderId(1L);
        testOrderItem.setProductId(1L);
        testOrderItem.setProductName("Danfoss 压缩机");
        testOrderItem.setSku("DF-001");
        testOrderItem.setPrice(new BigDecimal("2500.00"));
        testOrderItem.setQuantity(2);
    }

    @Test
    @DisplayName("获取订单列表 - 成功")
    void testListOrders_Success() {
        // Arrange - 使用MyBatis Plus标准方法
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> page =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10);
        page.setRecords(List.of(testOrder));
        page.setTotal(1);
        when(orderMapper.selectPage(any(), any())).thenReturn(page);
        when(orderItemMapper.selectList(any())).thenReturn(List.of(testOrderItem));

        // Act
        PageResult<OrderVO> result = orderService.listOrders(1L, null, 1L, 10L);

        // Assert
        assertThat(result).isNotNull();
        verify(orderMapper).selectPage(any(), any());
    }

    @Test
    @DisplayName("获取订单详情 - 成功")
    void testGetOrder_Success() {
        // Arrange - 使用MyBatis Plus标准方法
        when(orderMapper.selectOne(any())).thenReturn(testOrder);
        when(orderItemMapper.selectList(any())).thenReturn(List.of(testOrderItem));

        // Act
        OrderVO result = orderService.getOrder("ORD202401010001", 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderNo()).isEqualTo("ORD202401010001");

        verify(orderMapper).selectOne(any());
        verify(orderItemMapper).selectList(any());
    }

    @Test
    @DisplayName("获取订单详情 - 订单不存在")
    void testGetOrder_NotFound() {
        // Arrange
        when(orderMapper.selectOne(any())).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> orderService.getOrder("NOTEXIST", 1L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND.getCode());

        verify(orderMapper).selectOne(any());
    }

    @Test
    @DisplayName("取消订单 - 成功")
    void testCancelOrder_Success() {
        // Arrange
        testOrder.setStatus(OrderStatus.PENDING);
        when(orderMapper.selectOne(any())).thenReturn(testOrder);
        when(orderMapper.updateById(any(Order.class))).thenReturn(1);

        // Act
        orderService.cancelOrder("ORD202401010001", 1L);

        // Assert
        verify(orderMapper).selectOne(any());
        verify(orderMapper).updateById(any(Order.class));
    }

    @Test
    @DisplayName("取消订单 - 订单不存在")
    void testCancelOrder_NotFound() {
        // Arrange
        when(orderMapper.selectOne(any())).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> orderService.cancelOrder("NOTEXIST", 1L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND.getCode());

        verify(orderMapper).selectOne(any());
        verify(orderMapper, never()).updateById(any(Order.class));
    }

    @Test
    @DisplayName("获取订单列表 - 空结果")
    void testListOrders_EmptyResult() {
        // Arrange
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Order> page =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10);
        page.setRecords(List.of());
        page.setTotal(0);
        when(orderMapper.selectPage(any(), any())).thenReturn(page);

        // Act
        PageResult<OrderVO> result = orderService.listOrders(1L, null, 1L, 10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(0);

        verify(orderMapper).selectPage(any(), any());
    }
}
