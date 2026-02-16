package com.sld.backend.modules.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务请求VO
 */
@Data
public class ServiceRequestVO {

    private Long id;
    private String requestNo;
    private Long userId;
    private Long orderId;
    private String serviceType;
    private String serviceTypeName;
    private String priority;
    private String priorityName;
    private String description;
    private String contactInfo;
    private LocalDateTime scheduledTime;
    private String status;
    private String statusName;
    private String resolution;
    private Integer rating;
    private String feedback;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
