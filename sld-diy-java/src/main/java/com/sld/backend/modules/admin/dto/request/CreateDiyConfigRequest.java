package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建DIY配置请求
 */
@Data
public class CreateDiyConfigRequest {

    private String category;

    @NotBlank(message = "键不能为空")
    private String key;

    @NotBlank(message = "标签不能为空")
    private String label;

    private String value;

    private String icon;

    private String description;

    private Integer sortOrder;
}
