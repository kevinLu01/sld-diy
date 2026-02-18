package com.sld.backend.modules.order.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.order.dto.request.CreateOrderRequest;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.order.service.OrderService;
import com.sld.backend.security.CurrentUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "创建订单")
    public Result<OrderVO> createOrder(
        @Parameter(description = "用户ID") @CurrentUserId(required = false) Long userId,
        @RequestBody CreateOrderRequest request
    ) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(orderService.createOrder(userId, request));
    }

    @GetMapping
    @Operation(summary = "获取订单列表")
    public Result<PageResult<OrderVO>> listOrders(
        @Parameter(description = "用户ID") @CurrentUserId(required = false) Long userId,
        @Parameter(description = "订单状态") @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(orderService.listOrders(userId, status, page, limit));
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "获取订单详情")
    public Result<OrderVO> getOrder(
        @PathVariable String orderNo,
        @Parameter(description = "用户ID") @CurrentUserId(required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(orderService.getOrder(orderNo, userId));
    }

    @PostMapping("/{orderNo}/cancel")
    @Operation(summary = "取消订单")
    public Result<Void> cancelOrder(
        @PathVariable String orderNo,
        @Parameter(description = "用户ID") @CurrentUserId(required = false) Long userId
    ) {
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        orderService.cancelOrder(orderNo, userId);
        return Result.success();
    }
}
