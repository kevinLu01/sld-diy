package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 更新品牌请求
 */
@Data
public class UpdateBrandRequest {

    private String name;
    private String slug;
    private String logo;
    private String country;
    private String description;
}
