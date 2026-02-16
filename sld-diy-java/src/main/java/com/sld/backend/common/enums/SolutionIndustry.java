package com.sld.backend.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 解决方案行业枚举
 */
@Getter
public enum SolutionIndustry {

    RETAIL("retail", "零售业"),
    WAREHOUSE("warehouse", "仓储物流"),
    INDUSTRIAL("industrial", "工业制造"),
    MEDICAL("medical", "医疗医药"),
    FOOD("food", "食品加工"),
    SUPERMARKET("supermarket", "商超");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    SolutionIndustry(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
