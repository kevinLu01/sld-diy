package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 后台创建DIY私发报价请求
 */
@Data
public class CreatePrivateDiyQuoteRequest {
    private Double discountRate;
    private Double discountAmount;
    private String expiresAt;
    private String privateNote;
}
