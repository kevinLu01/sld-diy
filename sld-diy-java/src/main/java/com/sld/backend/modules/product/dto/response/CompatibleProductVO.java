package com.sld.backend.modules.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 兼容产品响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "兼容产品响应")
public class CompatibleProductVO {

    @Schema(description = "产品ID")
    private Long id;

    @Schema(description = "产品名称")
    private String name;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "兼容类型")
    private String compatibilityType;
}
