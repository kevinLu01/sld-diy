package com.sld.backend.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业认证信息实体
 */
@Data
@TableName("BusinessInfo")
public class BusinessInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 营业执照URL
     */
    private String businessLicense;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contactPerson;

    private Boolean verified;

    private LocalDateTime verifiedAt;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String creditCode;

    @TableField(exist = false)
    private String contactPhone;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
