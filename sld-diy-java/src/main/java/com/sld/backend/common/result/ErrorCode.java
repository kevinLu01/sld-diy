package com.sld.backend.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 通用错误码 200-999
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_ERROR(422, "参数校验失败"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 用户相关错误码 1001-1999
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户名已存在"),
    EMAIL_ALREADY_EXISTS(1003, "邮箱已注册"),
    PHONE_ALREADY_EXISTS(1004, "手机号已注册"),
    PASSWORD_ERROR(1005, "密码错误"),
    ACCOUNT_DISABLED(1006, "账号已被禁用"),

    // 产品相关错误码 2001-2999
    PRODUCT_NOT_FOUND(2001, "产品不存在"),
    PRODUCT_OUT_OF_STOCK(2002, "库存不足"),
    INSUFFICIENT_STOCK(2002, "库存不足"),
    PRODUCT_OFF_SHELF(2003, "产品已下架"),
    CATEGORY_NOT_FOUND(2004, "分类不存在"),
    BRAND_NOT_FOUND(2005, "品牌不存在"),

    // 购物车相关错误码 3001-3999
    CART_ITEM_NOT_FOUND(3001, "购物车项不存在"),

    // 订单相关错误码 4001-4999
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态不允许此操作"),
    ORDER_PAID(4003, "订单已支付，无法取消"),

    // DIY相关错误码 5001-5999
    DIY_PROJECT_NOT_FOUND(5001, "DIY方案不存在"),
    DIY_INCOMPATIBLE(5002, "配件不兼容"),

    // 解决方案相关错误码 6001-6999
    SOLUTION_NOT_FOUND(6001, "解决方案不存在"),

    // 知识库相关错误码 7001-7999
    ARTICLE_NOT_FOUND(7001, "文章不存在"),

    // 管理后台相关错误码 8001-8999
    ADMIN_OPERATION_FAILED(8001, "操作失败");

    private final Integer code;
    private final String message;
}
