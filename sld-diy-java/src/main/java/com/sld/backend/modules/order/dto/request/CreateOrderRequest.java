package com.sld.backend.modules.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建订单请求
 */
@Data
@Schema(description = "创建订单请求")
public class CreateOrderRequest {

    @NotEmpty(message = "订单项不能为空")
    @Schema(description = "订单项列表")
    private List<OrderItemRequest> items;

    @Schema(description = "DIY方案ID")
    private Long diyProjectId;

    @NotNull(message = "收货人不能为空")
    @Schema(description = "收货人")
    private String recipient;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "邮编")
    private String postalCode;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "是否需要安装")
    private Boolean needInstallation;

    @Data
    @Schema(description = "订单项")
    public static class OrderItemRequest {
        @NotNull(message = "产品ID不能为空")
        private Long productId;

        @NotNull(message = "数量不能为空")
        private Integer quantity;

        @Schema(description = "单价")
        private BigDecimal price;
    }
}
