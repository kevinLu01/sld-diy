package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatus {

    ACTIVE("active", "正常"),
    DISABLED("disabled", "禁用"),
    PENDING("pending", "待审核");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    UserStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
