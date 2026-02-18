package com.sld.backend.modules.diy.dto.response;

import lombok.Data;

/**
 * DIY分享响应
 */
@Data
public class DiyShareResponse {

    private String shareUrl;
    private String shareToken;
    private String qrCode;
    private String shareMode;
    private java.math.BigDecimal quotedTotalPrice;
    private java.math.BigDecimal discountRate;
    private java.math.BigDecimal discountAmount;
    private String shareExpiresAt;
}
