package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 解决方案实体
 */
@Data
@TableName("t_solution")
public class Solution {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String industry;

    private String scenario;

    private String description;

    @TableField("cover_image")
    private String coverImage;

    private String images;

    @TableField("temperature_range")
    private String temperatureRange;

    @TableField("capacity_range")
    private String capacityRange;

    private String features;

    @TableField("total_price")
    private BigDecimal totalPrice;

    @TableField("installation_guide")
    private String installationGuide;

    @TableField("view_count")
    private Integer viewCount;

    @TableField("usage_count")
    private Integer usageCount;

    private BigDecimal rating;

    // status 在数据库中是 INT 类型，1=已发布
    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String technicalDocs;

    @TableField(exist = false)
    private String cases;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}
