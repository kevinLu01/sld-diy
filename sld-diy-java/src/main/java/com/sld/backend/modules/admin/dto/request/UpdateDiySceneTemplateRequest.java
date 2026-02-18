package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateDiySceneTemplateRequest {
    private String sceneCode;
    private String name;
    private String description;
    private String applicationNotes;
    private BigDecimal tempMin;
    private BigDecimal tempMax;
    private BigDecimal capacityMin;
    private BigDecimal capacityMax;
    private Integer sortOrder;
    private Boolean isActive;
}
