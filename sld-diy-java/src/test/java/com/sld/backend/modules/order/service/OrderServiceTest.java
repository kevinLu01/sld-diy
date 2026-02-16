package com.sld.backend.modules.order.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.order.dto.request.CreateOrderRequest;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private CreateOrderRequest createOrderRequest;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        // 初始化测试订单
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("ORD20250216001");
        testOrder.setUserId(1L);
        testOrder.setUsername("testuser");
        testOrder.setTotalAmount(new BigDecimal("5000.00"));
        testOrder.setShippingFee(new BigDecimal("10.00"));
        testOrder.setInstallationFee(new BigDecimal("0.00"));
        testOrder.setDiscountAmount(new BigDecimal("0.00"));
        testOrder.setFinalAmount(new BigDecimal("5010.00"));
        testOrder.setStatus("pending");
        testOrder.setRecipient("张三");
        testOrder.setPhone("13800138000");
        testOrder.setProvince("广东省");
        testOrder.setCity("深圳市");
        testOrder.setDistrict("南山区");
        testOrder.setAddress("科技园南区123号");
        testOrder.setPostalCode("518000");
        testOrder.setNotes("请尽快发货");
        testOrder.setNeedInstallation(false);
        testOrder.setCreateTime(LocalDateTime.now());

        // 初始化创建订单请求
        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setRecipient("张三");
        createOrderRequest.setPhone("13800138000");
        createOrderRequest.setProvince("广东省");
        createOrderRequest.setCity("深圳市");
        createOrderRequest.setDistrict("南山区");
        createOrderRequest.setAddress("科技园南区123号");
        createOrderRequest.setPostalCode("518000");
        createOrderRequest.setNotes("请尽快发货");
        createOrderRequest.setNeedInstallation(false);

        List<CreateOrderRequest.OrderItemRequest> items = new ArrayList<>();
        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);
        itemRequest.setPrice(new BigDecimal("2500.00"));
        items.add(itemRequest);
        createOrderRequest.setItems(items);

        // 初始化订单项
        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrderId(1L);
        testOrderItem.setProductId(1L);
        testOrderItem.setProductName("Danfoss 压缩机");
        testOrderItem.setSku("DF-001");
        testOrderItem.setPrice(new BigDecimal("2500.00"));
        testOrderItem.setQuantity(2);
        testOrderItem.setTotal(new BigDecimal("5000.00"));
        testOrderItem.setCreateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("获取订单列表 - 成功")
    void testListOrders_Success() {
        // Arrange
        Page<Order> orderPage = new Page<>(1, 20);
        orderPage.setRecords(List.of(testOrder));
        orderPage.setCurrent(1L);
        orderPage.setSize(20L);
        orderPage.setTotal(1L);

        when(orderMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(orderPage);
        when(orderItemMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(new ArrayList<>());

        // Act
        PageResult<OrderVO> result = orderService.listOrders(1L, null, 1L, 20L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);

        verify(orderMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取订单列表 - 按状态筛选")
    void testListOrders_WithStatusFilter() {
        // Arrange
        Page<Order> orderPage = new Page<>(1, 20);
        orderPage.setRecords(List.of(testOrder));
        orderPage.setCurrent(1L);
        orderPage.setSize(20L);
        orderPage.setTotal(1L);

        when(orderMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(orderPage);
        when(orderItemMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(new ArrayList<>());

        // Act
        PageResult<OrderVO> result = orderService.listOrders(1L, "pending", 1L, 20L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);

        verify(orderMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取订单列表 - 空结果")
    void testListOrders_EmptyResult() {
        // Arrange
        Page<Order> orderPage = new Page<>(1, 20);
        orderPage.setRecords(new ArrayList<>());
        orderPage.setCurrent(1L);
        orderPage.setSize(20L);
        orderPage.setTotal(0L);

        when(orderMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(orderPage);

        // Act
        PageResult<OrderVO> result = orderService.listOrders(1L, null, 1L, 20L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0L);

        verify(orderMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取订单详情 - 成功")
    void testGetOrder_Success() {
        // Arrange
        when(orderMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testOrder);
        when(orderItemMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(new ArrayList<>());

        // Act
        OrderVO result = orderService.getOrder("ORD20250216001", 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getOrderNo()).isEqualTo("ORD20250216001");
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getRecipient()).isEqualTo("张三");

        verify(orderMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取订单详情 - 订单不存在")
    void testGetOrder_NotFound() {
        // Arrange
        when(orderMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> orderService.getOrder("NOTEXIST", 1L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND.getCode());

        verify(orderMapper).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("取消订单 - 成功")
    void testCancelOrder_Success() {
        // Arrange
        when(orderMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testOrder);
        when(orderMapper.updateById(any(Order.class))).thenReturn(1);

        // Act
        orderService.cancelOrder("ORD20250216001", 1L);

        // Assert
        verify(orderMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(orderMapper).updateById(any(Order.class));
    }

    @Test
    @DisplayName("取消订单 - 订单不存在")
    void testCancelOrder_NotFound() {
        // Arrange
        when(orderMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> orderService.cancelOrder("NOTEXIST", 1L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND.getCode());

        verify(orderMapper).selectOne(any(LambdaQueryWrapper.class));
        verify(orderMapper, never()).updateById(any(Order.class));
    }

    @Test
    @DisplayName("获取订单详情 - 用户不匹配")
    void testGetOrder_UserMismatch() {
        // Arrange - 查询条件包含userId, 所以不匹配时返回null
        when(orderMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> orderService.getOrder("ORD20250216001", 999L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND.getCode());

        verify(orderMapper).selectOne(any(LambdaQueryWrapper.class));
    }
}
