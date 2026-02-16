package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品评价实体
 */
@Data
@TableName("Review")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 评分 1-5
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片（JSON数组）
     */
    private String images;

    /**
     * 是否匿名
     */
    private Boolean isAnonymous;

    /**
     * 状态：published-已发布，hidden-隐藏
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 用户信息（非数据库字段）
     */
    @TableField(exist = false)
    private Object user;
}
