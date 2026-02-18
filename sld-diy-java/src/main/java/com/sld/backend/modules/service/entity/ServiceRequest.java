package com.sld.backend.modules.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务请求实体
 */
@Data
@TableName("t_service_request")
public class ServiceRequest {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 服务单号
     */
    @TableField("request_no")
    private String requestNo;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 关联订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 服务类型：installation-安装，maintenance-维护，consultation-咨询，repair-维修
     */
    @TableField("service_type")
    private String serviceType;

    /**
     * 优先级：low-低，normal-正常，high-高，urgent-紧急
     */
    private String priority;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 联系信息（JSON）
     */
    @TableField("contact_info")
    private String contactInfo;

    /**
     * 预约时间
     */
    @TableField("scheduled_time")
    private LocalDateTime scheduledTime;

    /**
     * 指派给（工程师ID）
     */
    @TableField("assigned_to")
    private Long assignedTo;

    /**
     * 状态：pending-待处理，processing-处理中，completed-已完成，cancelled-已取消
     */
    private String status;

    /**
     * 处理结果
     */
    private String resolution;

    /**
     * 服务评价
     */
    private Integer rating;

    /**
     * 评价反馈
     */
    private String feedback;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
