package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建品牌请求
 */
@Data
public class CreateBrandRequest {

    @NotBlank(message = "品牌名称不能为空")
    private String name;

    private String slug;

    private String logo;

    private String country;

    private String description;
}
