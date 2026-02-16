package com.sld.backend.modules.admin.dto.request;

import lombok.Data;

/**
 * 更新文章请求
 */
@Data
public class UpdateArticleRequest {

    private String title;
    private String category;
    private String content;
    private String tags;
    private String coverImage;
    private String author;
    private String status;
}
