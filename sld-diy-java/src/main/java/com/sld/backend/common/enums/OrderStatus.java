package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatus {

    PENDING("pending", "待支付"),
    PAID("paid", "已支付"),
    PROCESSING("processing", "处理中"),
    SHIPPED("shipped", "已发货"),
    DELIVERED("delivered", "已签收"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消"),
    REFUNDED("refunded", "已退款");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    OrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
