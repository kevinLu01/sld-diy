package com.sld.backend.modules.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_service_request_history")
public class ServiceRequestHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("request_no")
    private String requestNo;

    @TableField("from_status")
    private String fromStatus;

    @TableField("to_status")
    private String toStatus;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("operator_role")
    private String operatorRole;

    private String note;

    @TableField("create_time")
    private LocalDateTime createTime;
}
