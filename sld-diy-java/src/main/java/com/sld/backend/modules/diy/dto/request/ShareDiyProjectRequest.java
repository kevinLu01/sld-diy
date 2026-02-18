package com.sld.backend.modules.diy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分享DIY方案请求
 */
@Data
@Schema(description = "分享DIY方案请求")
public class ShareDiyProjectRequest {

    @Schema(description = "分享模式: public/private_offer", example = "private_offer")
    private String shareMode;

    @Schema(description = "折扣率(0.15 表示 85 折)", example = "0.15")
    private Double discountRate;

    @Schema(description = "立减金额", example = "200")
    private Double discountAmount;

    @Schema(description = "过期时间(ISO8601)", example = "2026-02-28T23:59:59")
    private String expiresAt;

    @Schema(description = "私发说明")
    private String privateNote;
}
