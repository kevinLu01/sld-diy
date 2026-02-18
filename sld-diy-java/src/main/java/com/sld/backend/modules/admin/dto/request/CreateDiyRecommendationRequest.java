package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建DIY推荐配置请求
 */
@Data
public class CreateDiyRecommendationRequest {

    @NotBlank(message = "场景不能为空")
    private String scenario;

    @NotBlank(message = "产品类型不能为空")
    private String productType;

    private String componentRole;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private Integer priority;

    private Boolean isRequired;

    private Integer minQuantity;

    private Integer maxQuantity;
}
