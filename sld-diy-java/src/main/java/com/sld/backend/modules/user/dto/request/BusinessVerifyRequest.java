package com.sld.backend.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 企业认证请求
 */
@Data
public class BusinessVerifyRequest {

    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    private String businessLicense;

    private String creditCode;

    private String industry;

    private String address;

    private String contactPerson;

    private String contactPhone;
}
