package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 解决方案产品关联实体
 */
@Data
@TableName("SolutionProduct")
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
     * 备注
     */
    private String notes;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 排序
     */
    private Integer sortOrder;
}
