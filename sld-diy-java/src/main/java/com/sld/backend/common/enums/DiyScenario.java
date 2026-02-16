package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * DIY 场景枚举
 */
@Getter
public enum DiyScenario {

    COLD_STORAGE("cold_storage", "冷库"),
    FREEZER("freezer", "冷冻柜"),
    REFRIGERATOR("refrigerator", "冷藏柜"),
    SHOWCASE("showcase", "展示柜"),
    WATER_CHILLER("water_chiller", "冷水机"),
    AIR_CONDITIONER("air_conditioner", "空调");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    DiyScenario(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
