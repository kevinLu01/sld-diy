package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建分类请求
 */
@Data
public class CreateCategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    private String name;

    private String slug;

    private Long parentId;

    private String icon;

    private Integer sortOrder;

    private String description;
}
