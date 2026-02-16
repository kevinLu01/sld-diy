package com.sld.backend.modules.admin.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 管理后台统计响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理后台统计响应")
public class AdminStatsResponse {

    @Schema(description = "用户总数")
    private Long totalUsers;

    @Schema(description = "今日新增用户")
    private Long todayNewUsers;

    @Schema(description = "产品总数")
    private Long totalProducts;

    @Schema(description = "订单总数")
    private Long totalOrders;

    @Schema(description = "今日订单数")
    private Long todayOrders;

    @Schema(description = "总销售额")
    private BigDecimal totalSales;

    @Schema(description = "今日销售额")
    private BigDecimal todaySales;

    @Schema(description = "待处理订单数")
    private Long pendingOrders;

    @Schema(description = "解决方案总数")
    private Integer totalSolutions;
}
