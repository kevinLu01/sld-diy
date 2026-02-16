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
     * 统一社会信用代码
     */
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
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 是否已认证
     */
    private Boolean verified;

    /**
     * 认证时间
     */
    private LocalDateTime verifiedAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
