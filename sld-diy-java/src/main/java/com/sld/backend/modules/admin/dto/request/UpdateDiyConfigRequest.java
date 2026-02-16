package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 更新DIY配置请求
 */
@Data
public class UpdateDiyConfigRequest {

    private String category;
    private String key;
    private String label;
    private String value;
    private String icon;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;
}
