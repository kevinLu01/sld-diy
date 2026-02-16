package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 方案项实体
 */
@Data
@TableName("DiyProjectItem")
public class DiyProjectItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("projectId")
    private Long diyProjectId;

    private Long productId;

    private Integer quantity;

    private String notes;

    private Integer sortOrder;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String sku;

    @TableField(exist = false)
    private String productName;

    @TableField(exist = false)
    private String productImage;

    @TableField(exist = false)
    private BigDecimal price;

    @TableField(exist = false)
    private BigDecimal total;

    @TableField(exist = false)
    private Integer matchScore;

    @TableField(exist = false)
    private String matchReason;

    @TableField(exist = false)
    private Integer deleted;

    @TableField(exist = false)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
