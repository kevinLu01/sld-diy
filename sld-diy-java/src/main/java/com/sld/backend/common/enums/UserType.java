package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用户类型枚举
 */
@Getter
public enum UserType {

    PERSONAL("personal", "个人用户"),
    BUSINESS("business", "企业用户"),
    ADMIN("admin", "管理员");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    UserType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
