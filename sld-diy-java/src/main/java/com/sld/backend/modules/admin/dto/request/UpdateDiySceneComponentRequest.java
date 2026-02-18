package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

@Data
public class UpdateDiySceneComponentRequest {
    private String componentCode;
    private String componentName;
    private String componentRole;
    private Boolean required;
    private Integer minQty;
    private Integer maxQty;
    private String selectionMode;
    private String specRequirement;
    private Integer sortOrder;
    private Boolean isActive;
}
