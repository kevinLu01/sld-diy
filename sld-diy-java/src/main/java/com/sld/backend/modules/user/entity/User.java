package com.sld.backend.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sld.backend.common.enums.UserStatus;
import com.sld.backend.common.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体
 */
@Data
@TableName("t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String avatar;

    @TableField("user_type")
    private UserType userType;

    private UserStatus status;

    private String nickname;

    @TableField("company_name")
    private String companyName;

    @TableField("business_license")
    private String businessLicense;

    @TableField("credit_code")
    private String creditCode;

    @TableField("verify_status")
    private String verifyStatus;

    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
