package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private Long solutionId;

    /**
     * 产品ID
     */
    private Long productId;

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
