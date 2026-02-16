package com.sld.backend.modules.cart.controller;

import com.sld.backend.common.result.Result;
import com.sld.backend.modules.cart.dto.response.CartItemVO;
import com.sld.backend.modules.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "购物车管理", description = "购物车相关接口")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "获取购物车列表")
    public Result<List<CartItemVO>> getCart(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        return Result.success(cartService.getCart(userId));
    }

    @PostMapping("/items")
    @Operation(summary = "添加购物车")
    public Result<CartItemVO> addItem(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId,
        @RequestParam Long productId,
        @RequestParam Integer quantity
    ) {
        return Result.success(cartService.addItem(userId, productId, quantity));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "更新购物车项数量")
    public Result<CartItemVO> updateItem(
        @PathVariable Long id,
        @RequestParam Integer quantity
    ) {
        return Result.success(cartService.updateItem(id, quantity));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "删除购物车项")
    public Result<Void> deleteItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return Result.success();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空购物车")
    public Result<Void> clearCart(
        @Parameter(description = "用户ID") @RequestHeader("X-User-Id") Long userId
    ) {
        cartService.clearCart(userId);
        return Result.success();
    }
}
