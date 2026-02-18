package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDiySceneComponentRequest {
    @NotNull
    private Long sceneId;
    @NotBlank
    private String componentCode;
    @NotBlank
    private String componentName;
    private String componentRole;
    private Boolean required;
    private Integer minQty;
    private Integer maxQty;
    private String selectionMode;
    private String specRequirement;
    private Integer sortOrder;
}
