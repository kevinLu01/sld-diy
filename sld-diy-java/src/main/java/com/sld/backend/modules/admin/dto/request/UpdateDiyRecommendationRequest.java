package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 更新DIY推荐配置请求
 */
@Data
public class UpdateDiyRecommendationRequest {

    private String scenario;
    private String productType;
    private String componentRole;
    private Long categoryId;
    private Integer priority;
    private Boolean isRequired;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Boolean isActive;
}
