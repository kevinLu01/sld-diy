package com.sld.backend.modules.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.enums.OrderStatus;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.order.dto.request.CreateOrderRequest;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.order.entity.Order;
import com.sld.backend.modules.order.entity.OrderItem;
import com.sld.backend.modules.order.mapper.OrderItemMapper;
import com.sld.backend.modules.order.mapper.OrderMapper;
import com.sld.backend.modules.order.service.OrderService;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, CreateOrderRequest request) {
        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
            Product product = productMapper.selectById(itemReq.getProductId());
            if (product == null) {
                throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            BigDecimal price = itemReq.getPrice() != null ? itemReq.getPrice() : product.getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getImages());
            item.setSku(product.getSku());
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(price);
            item.setTotal(itemTotal);
            items.add(item);
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setShippingFee(BigDecimal.ZERO);
        order.setInstallationFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setDiyProjectId(request.getDiyProjectId());
        order.setRecipient(request.getRecipient());
        order.setPhone(request.getPhone());
        order.setProvince(request.getProvince());
        order.setCity(request.getCity());
        order.setDistrict(request.getDistrict());
        order.setAddress(request.getAddress());
        order.setPostalCode(request.getPostalCode());
        order.setNotes(request.getNotes());
        order.setNeedInstallation(request.getNeedInstallation());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 保存订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            item.setCreateTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            orderItemMapper.insert(item);
        }

        return toVO(order, items);
    }

    @Override
    public PageResult<OrderVO> listOrders(Long userId, String status, Long page, Long limit) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, limit), wrapper);
        List<OrderVO> voList = orderPage.getRecords().stream()
            .map(order -> {
                List<OrderItem> items = orderItemMapper.selectList(
                    new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId())
                );
                return toVO(order, items);
            })
            .collect(Collectors.toList());

        return PageResult.of(orderPage.getTotal(), orderPage.getCurrent(), orderPage.getSize(), voList);
    }

    @Override
    public OrderVO getOrder(String orderNo, Long userId) {
        Order order = orderMapper.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId)
        );
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        List<OrderItem> items = orderItemMapper.selectList(
            new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId())
        );
        return toVO(order, items);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderNo, Long userId) {
        Order order = orderMapper.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId)
        );
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只能取消待支付订单");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    private OrderVO toVO(Order order, List<OrderItem> items) {
        String fullAddress = String.join(" ",
            order.getProvince() != null ? order.getProvince() : "",
            order.getCity() != null ? order.getCity() : "",
            order.getDistrict() != null ? order.getDistrict() : "",
            order.getAddress() != null ? order.getAddress() : ""
        ).trim();

        List<OrderVO.OrderItemVO> itemVOs = items.stream()
            .map(item -> new OrderVO.OrderItemVO(
                item.getProductId(),
                item.getProductName(),
                item.getSku(),
                item.getQuantity(),
                item.getPrice(),
                item.getTotal()
            ))
            .collect(Collectors.toList());

        return OrderVO.builder()
            .id(order.getId())
            .orderNo(order.getOrderNo())
            .userId(order.getUserId())
            .username(order.getUsername())
            .totalAmount(order.getTotalAmount())
            .shippingFee(order.getShippingFee())
            .installationFee(order.getInstallationFee())
            .discountAmount(order.getDiscountAmount())
            .finalAmount(order.getFinalAmount())
            .status(order.getStatus() != null ? order.getStatus().getCode() : null)
            .recipient(order.getRecipient())
            .phone(order.getPhone())
            .fullAddress(fullAddress)
            .notes(order.getNotes())
            .needInstallation(order.getNeedInstallation())
            .items(itemVOs)
            .createTime(order.getCreateTime())
            .payTime(order.getPayTime())
            .build();
    }

    private String generateOrderNo() {
        return "SLD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}

