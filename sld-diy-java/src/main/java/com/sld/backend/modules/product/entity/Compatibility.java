package com.sld.backend.modules.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品兼容性实体
 */
@Data
@TableName("t_compatibility")
public class Compatibility {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 产品A ID
     */
    private Long productAId;

    /**
     * 产品B ID
     */
    private Long productBId;

    /**
     * 兼容类型：compatible-兼容，incompatible-不兼容，recommended-推荐搭配
     */
    private String compatibilityType;

    /**
     * 备注说明
     */
    private String notes;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 产品A信息（非数据库字段）
     */
    @TableField(exist = false)
    private Product productA;

    /**
     * 产品B信息（非数据库字段）
     */
    @TableField(exist = false)
    private Product productB;
}
