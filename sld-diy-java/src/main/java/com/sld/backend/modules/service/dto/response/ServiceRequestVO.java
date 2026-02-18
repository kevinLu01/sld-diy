package com.sld.backend.modules.service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<HistoryVO> histories;

    @Data
    public static class HistoryVO {
        private String fromStatus;
        private String toStatus;
        private Long operatorId;
        private String operatorRole;
        private String note;
        private LocalDateTime createTime;
    }
}
