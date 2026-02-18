package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * DIY 方案项实体
 */
@Data
@TableName("t_diy_project_item")
public class DiyProjectItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目ID
     */
    @TableField("diy_project_id")
    private Long projectId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 备注
     */
    private String notes;

    /**
     * 排序
     */
    @TableField(exist = false)
    private Integer sortOrder;
}
