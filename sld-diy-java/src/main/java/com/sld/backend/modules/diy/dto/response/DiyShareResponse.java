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
}
