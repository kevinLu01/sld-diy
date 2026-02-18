package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateDiyComponentOptionRequest {
    private Long productId;
    private String optionName;
    private String brandName;
    private String modelSpec;
    private String specJson;
    private BigDecimal basePrice;
    private Integer sortOrder;
    private Boolean isActive;
}
