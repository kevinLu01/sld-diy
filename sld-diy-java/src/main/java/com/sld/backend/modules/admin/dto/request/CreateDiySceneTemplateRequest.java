package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateDiySceneTemplateRequest {
    @NotBlank
    private String sceneCode;
    @NotBlank
    private String name;
    private String description;
    private String applicationNotes;
    private BigDecimal tempMin;
    private BigDecimal tempMax;
    private BigDecimal capacityMin;
    private BigDecimal capacityMax;
    private Integer sortOrder;
}
