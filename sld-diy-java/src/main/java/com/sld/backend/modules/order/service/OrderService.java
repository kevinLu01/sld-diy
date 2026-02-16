package com.sld.backend.modules.order.service;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.order.dto.request.CreateOrderRequest;
import com.sld.backend.modules.order.dto.response.OrderVO;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    OrderVO createOrder(Long userId, CreateOrderRequest request);

    /**
     * 获取订单列表
     */
    PageResult<OrderVO> listOrders(Long userId, String status, Long page, Long limit);

    /**
     * 获取订单详情
     */
    OrderVO getOrder(String orderNo, Long userId);

    /**
     * 取消订单
     */
    void cancelOrder(String orderNo, Long userId);
}
