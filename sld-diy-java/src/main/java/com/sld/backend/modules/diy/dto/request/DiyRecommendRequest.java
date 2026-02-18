package com.sld.backend.modules.diy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DIY 推荐请求
 */
@Data
@Schema(description = "DIY推荐请求")
public class DiyRecommendRequest {

    @NotBlank(message = "场景不能为空")
    @Schema(description = "场景", example = "cold_storage")
    private String scenario;

    @Schema(description = "自定义场景名称", example = "海鲜加工车间")
    private String customScenarioName;

    @Schema(description = "温度范围", example = "-5~0")
    private String temperatureRange;

    @NotNull(message = "制冷量不能为空")
    @Schema(description = "制冷量", example = "50")
    private BigDecimal coolingCapacity;

    @Schema(description = "制冷量单位", example = "kW")
    private String capacityUnit;

    @Schema(description = "容积", example = "100")
    private BigDecimal volume;

    @Schema(description = "容积单位", example = "m3")
    private String volumeUnit;

    @Schema(description = "环境温度", example = "30~40")
    private String ambientTemp;

    @Schema(description = "选项")
    private Map<String, Boolean> options;
}
