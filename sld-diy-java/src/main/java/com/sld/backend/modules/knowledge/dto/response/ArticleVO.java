package com.sld.backend.modules.knowledge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识文章响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "知识文章响应")
public class ArticleVO {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "标签")
    private List<String> tags;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "有用次数")
    private Integer helpfulCount;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
