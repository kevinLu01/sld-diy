package com.sld.backend.modules.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户收藏实体
 */
@Data
@TableName("Favorite")
public class Favorite {

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
     * 创建时间
     */
    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 产品信息（非数据库字段）
     */
    @TableField(exist = false)
    private Object product;
}
