package com.sld.backend.modules.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识文章实体
 */
@Data
@TableName("KnowledgeArticle")
public class Article {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String category;

    private String content;

    private String tags;

    private String coverImage;

    private String attachments;

    private Integer viewCount;

    private Integer helpfulCount;

    private String status;

    private String author;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String summary;

    @TableField(exist = false)
    private String source;

    @TableField(exist = false)
    private Integer likeCount;

    @TableField(exist = false)
    private Integer sortOrder;

    @TableField(exist = false)
    private Integer publishStatus;

    @TableField(exist = false)
    private LocalDateTime publishTime;

    @TableField(exist = false)
    private Integer deleted;
}
