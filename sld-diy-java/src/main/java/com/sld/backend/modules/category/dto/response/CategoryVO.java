package com.sld.backend.modules.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分类响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类响应")
public class CategoryVO {

    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "Slug")
    private String slug;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "产品数量")
    private Integer count;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "父分类ID")
    private Long parentId;

    @Schema(description = "子分类")
    private List<CategoryVO> children;
}
