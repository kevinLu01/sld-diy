package com.sld.backend.modules.knowledge.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识文章实体
 */
@Data
@TableName("t_article")
public class Article {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String category;

    private String content;

    private String tags;

    @TableField("cover_image")
    private String coverImage;

    @TableField(exist = false)
    private String attachments;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("helpful_count")
    private Integer helpfulCount;

    @TableField(exist = false)
    private String status;

    private String author;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField("summary")
    private String summary;

    @TableField("source")
    private String source;

    @TableField("like_count")
    private Integer likeCount;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("publish_status")
    private Integer publishStatus;

    @TableField("publish_time")
    private LocalDateTime publishTime;

    @TableField("deleted")
    private Integer deleted;
}
