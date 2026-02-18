package com.sld.backend.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台操作日志
 */
@Data
@TableName("t_admin_audit_log")
public class AdminAuditLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("operator_id")
    private Long operatorId;

    private String action;

    @TableField("entity_type")
    private String entityType;

    @TableField("entity_id")
    private String entityId;

    @TableField("request_path")
    private String requestPath;

    @TableField("request_method")
    private String requestMethod;

    @TableField("request_payload")
    private String requestPayload;

    @TableField("result_code")
    private Integer resultCode;

    @TableField("create_time")
    private LocalDateTime createTime;
}
