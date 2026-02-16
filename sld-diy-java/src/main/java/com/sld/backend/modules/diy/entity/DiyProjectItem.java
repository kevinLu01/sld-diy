package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DIY 方案项实体
 */
@Data
@TableName("t_diy_project_item")
public class DiyProjectItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * DIY方案ID
     */
    private Long diyProjectId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * SKU
     */
    private String sku;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品图片
     */
    private String productImage;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal total;

    /**
     * 匹配分数
     */
    private Integer matchScore;

    /**
     * 匹配原因
     */
    private String matchReason;

    /**
     * 备注
     */
    private String notes;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
