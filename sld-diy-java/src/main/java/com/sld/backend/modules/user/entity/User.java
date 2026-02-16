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

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码（加密）
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 状态
     */
    private UserStatus status;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 公司名（企业用户）
     */
    private String companyName;

    /**
     * 营业执照（企业用户）
     */
    private String businessLicense;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 企业认证状态
     */
    private String verifyStatus;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

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
