package com.sld.backend.modules.diy.dto.response;

import lombok.Data;

/**
 * DIY配置VO
 */
@Data
public class DiyConfigVO {

    private Long id;
    private String category;
    private String key;
    private String label;
    private String value;
    private String icon;
    private String description;
    private Integer sortOrder;
}
