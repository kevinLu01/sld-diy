package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 解决方案实体
 */
@Data
@TableName("Solution")
public class Solution {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String industry;

    private String scenario;

    private String description;

    private String coverImage;

    private String images;

    private String temperatureRange;

    private String capacityRange;

    private String features;

    private BigDecimal totalPrice;

    private String installationGuide;

    private Integer viewCount;

    private Integer usageCount;

    private BigDecimal rating;

    private String status;

    private Integer createdBy;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private String technicalDocs;

    @TableField(exist = false)
    private String cases;

    @TableField(exist = false)
    private Integer sortOrder;

    @TableField(exist = false)
    private Integer deleted;
}
