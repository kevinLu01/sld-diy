package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDiyComponentOptionRequest {
    @NotNull
    private Long sceneComponentId;
    private Long productId;
    @NotBlank
    private String optionName;
    private String brandName;
    private String modelSpec;
    private String specJson;
    private BigDecimal basePrice;
    private Integer sortOrder;
}
