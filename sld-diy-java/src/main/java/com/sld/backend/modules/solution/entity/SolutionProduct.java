package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 解决方案产品关联实体
 */
@Data
@TableName("t_solution_product")
public class SolutionProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 解决方案ID
     */
    @TableField("solution_id")
    private Long solutionId;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 数量
     */
    private Integer quantity;

    private java.math.BigDecimal price;

    private java.math.BigDecimal total;

    /**
     * 备注
     */
    private String notes;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private java.time.LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private java.time.LocalDateTime updateTime;

    @TableField(exist = false)
    private Boolean isRequired;

    @TableField(exist = false)
    private Integer sortOrder;
}
