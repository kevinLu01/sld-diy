package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 更新分类请求
 */
@Data
public class UpdateCategoryRequest {

    private String name;
    private String slug;
    private Long parentId;
    private String icon;
    private Integer sortOrder;
    private String description;
}
