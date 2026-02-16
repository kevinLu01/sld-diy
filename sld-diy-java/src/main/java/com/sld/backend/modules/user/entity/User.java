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
@TableName("User")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String email;

    @TableField("passwordHash")
    private String password;

    private String phone;

    private String avatar;

    private UserType userType;

    private UserStatus status;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String companyName;

    @TableField(exist = false)
    private String businessLicense;

    @TableField(exist = false)
    private String creditCode;

    @TableField(exist = false)
    private String verifyStatus;

    @TableField(exist = false)
    private LocalDateTime lastLoginTime;

    @TableField(exist = false)
    private String lastLoginIp;

    @TableField(exist = false)
    private Integer deleted;
}
