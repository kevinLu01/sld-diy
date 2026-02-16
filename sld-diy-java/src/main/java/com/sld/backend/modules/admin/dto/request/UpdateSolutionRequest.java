package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新解决方案请求
 */
@Data
public class UpdateSolutionRequest {

    private String title;
    private String industry;
    private String scenario;
    private String description;
    private String coverImage;
    private String temperatureRange;
    private String capacityRange;
    private String features;
    private BigDecimal totalPrice;
    private String installationGuide;
    private String status;
}
