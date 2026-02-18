package com.sld.backend.modules.admin.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * 后台操作日志展示对象
 */
@Data
@Builder
public class AdminAuditLogVO {
    private Long id;
    private Long operatorId;
    private String action;
    private String entityType;
    private String entityId;
    private String requestPath;
    private String requestMethod;
    private String requestPayload;
    private Integer resultCode;
    private String createTime;
}
