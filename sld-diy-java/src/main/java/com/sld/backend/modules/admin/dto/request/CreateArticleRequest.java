package com.sld.backend.modules.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建文章请求
 */
@Data
public class CreateArticleRequest {

    @NotBlank(message = "标题不能为空")
    private String title;

    private String category;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String tags;

    private String coverImage;

    private String author;

    private String status;
}
