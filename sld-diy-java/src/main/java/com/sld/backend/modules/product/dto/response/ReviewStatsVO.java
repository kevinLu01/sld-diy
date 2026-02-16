package com.sld.backend.modules.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 评价统计响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评价统计响应")
public class ReviewStatsVO {

    @Schema(description = "平均评分")
    private BigDecimal average;

    @Schema(description = "评价总数")
    private Integer count;

    @Schema(description = "评分分布")
    private Map<String, Integer> distribution;
}
