package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 产品状态枚举
 */
@Getter
public enum ProductStatus {

    ON_SHELF("on_shelf", "上架"),
    OFF_SHELF("off_shelf", "下架"),
    OUT_OF_STOCK("out_of_stock", "缺货");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    ProductStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
