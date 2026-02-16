package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建解决方案请求
 */
@Data
public class CreateSolutionRequest {

    @NotBlank(message = "标题不能为空")
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
