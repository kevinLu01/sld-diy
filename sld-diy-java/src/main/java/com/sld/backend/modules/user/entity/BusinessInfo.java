package com.sld.backend.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业认证信息实体
 */
@Data
@TableName("t_business_info")
public class BusinessInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 营业执照URL
     */
    @TableField("business_license")
    private String businessLicense;

    @TableField("credit_code")
    private String creditCode;

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
    @TableField("contact_person")
    private String contactPerson;

    @TableField("contact_phone")
    private String contactPhone;

    private Boolean verified;

    @TableField("verified_at")
    private LocalDateTime verifiedAt;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
