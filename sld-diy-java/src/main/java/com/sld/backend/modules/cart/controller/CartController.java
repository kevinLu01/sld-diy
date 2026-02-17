package com.sld.backend.modules.cart.controller;

import com.sld.backend.common.result.Result;
import com.sld.backend.modules.cart.dto.response.CartItemVO;
import com.sld.backend.modules.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Collections;
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
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        userId = resolveUserId(userId);
        // 匿名用户返回空购物车
        if (userId == null) {
            return Result.success(Collections.emptyList());
        }
        return Result.success(cartService.getCart(userId));
    }

    @PostMapping("/items")
    @Operation(summary = "添加购物车")
    public Result<CartItemVO> addItem(
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestParam Long productId,
        @RequestParam Integer quantity
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(cartService.addItem(userId, productId, quantity));
    }

    @PostMapping
    @Operation(summary = "添加购物车(兼容前端JSON入参)")
    public Result<CartItemVO> addItemCompat(
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestBody Map<String, Object> body
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Long productId = Long.valueOf(String.valueOf(body.get("productId")));
        Integer quantity = body.get("quantity") == null ? 1 : Integer.valueOf(String.valueOf(body.get("quantity")));
        return Result.success(cartService.addItem(userId, productId, quantity));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "更新购物车项数量")
    public Result<CartItemVO> updateItem(
        @PathVariable Long id,
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestParam Integer quantity
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(cartService.updateItem(userId, id, quantity));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新购物车项数量(兼容前端JSON入参)")
    public Result<CartItemVO> updateItemCompat(
        @PathVariable Long id,
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
        @RequestBody Map<String, Object> body
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        Integer quantity = Integer.valueOf(String.valueOf(body.get("quantity")));
        return Result.success(cartService.updateItem(userId, id, quantity));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "删除购物车项")
    public Result<Void> deleteItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除购物车项(兼容前端路径)")
    public Result<Void> deleteItemCompat(@PathVariable Long id) {
        cartService.deleteItem(id);
        return Result.success();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清空购物车")
    public Result<Void> clearCart(
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        cartService.clearCart(userId);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "清空购物车(兼容前端路径)")
    public Result<Void> clearCartCompat(
        @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        userId = resolveUserId(userId);
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        cartService.clearCart(userId);
        return Result.success();
    }

    private Long resolveUserId(Long userIdHeader) {
        if (userIdHeader != null) {
            return userIdHeader;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        try {
            return Long.valueOf(auth.getName());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
