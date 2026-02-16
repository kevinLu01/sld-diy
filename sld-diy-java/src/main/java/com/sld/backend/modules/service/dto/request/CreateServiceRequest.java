package com.sld.backend.modules.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创建服务请求DTO
 */
@Data
public class CreateServiceRequest {

    @NotBlank(message = "服务类型不能为空")
    private String serviceType;

    private Long orderId;

    private String description;

    private String contactInfo;

    private LocalDateTime scheduledTime;

    private String priority;
}
