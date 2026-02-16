package com.sld.backend.modules.solution.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 解决方案案例实体
 */
@Data
@TableName("SolutionCase")
public class SolutionCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 解决方案ID
     */
    private Long solutionId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 项目地点
     */
    private String location;

    /**
     * 完工日期
     */
    private LocalDate completionDate;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 项目图片（JSON数组）
     */
    private String images;

    /**
     * 项目成果/效果
     */
    private String results;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
