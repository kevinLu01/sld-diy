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

    private Long solutionId;

    private Long productId;

    private Integer quantity;

    private String notes;

    private Boolean isRequired;

    private Integer sortOrder;

    // ===== 以下字段不在数据库中，保留以兼容 Service 层 =====

    @TableField(exist = false)
    private BigDecimal price;

    @TableField(exist = false)
    private BigDecimal total;

    @TableField(exist = false)
    private Integer deleted;

    @TableField(exist = false)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private LocalDateTime updateTime;
}
