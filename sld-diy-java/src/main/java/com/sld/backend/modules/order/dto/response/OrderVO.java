package com.sld.backend.modules.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单响应")
public class OrderVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "订单金额")
    private BigDecimal totalAmount;

    @Schema(description = "运费")
    private BigDecimal shippingFee;

    @Schema(description = "安装费")
    private BigDecimal installationFee;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "实付金额")
    private BigDecimal finalAmount;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "收货人")
    private String recipient;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "地址")
    private String fullAddress;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "是否需要安装")
    private Boolean needInstallation;

    @Schema(description = "订单项")
    private List<OrderItemVO> items;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "订单项响应")
    public static class OrderItemVO {
        @Schema(description = "产品ID")
        private Long productId;

        @Schema(description = "产品名称")
        private String productName;

        @Schema(description = "SKU")
        private String sku;

        @Schema(description = "数量")
        private Integer quantity;

        @Schema(description = "单价")
        private BigDecimal price;

        @Schema(description = "总价")
        private BigDecimal total;
    }
}
