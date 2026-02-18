package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_diy_scene_template")
public class DiySceneTemplate {

    @TableId
    private Long id;

    @TableField("scene_code")
    private String sceneCode;

    private String name;
    private String description;

    @TableField("application_notes")
    private String applicationNotes;

    @TableField("temp_min")
    private BigDecimal tempMin;

    @TableField("temp_max")
    private BigDecimal tempMax;

    @TableField("capacity_min")
    private BigDecimal capacityMin;

    @TableField("capacity_max")
    private BigDecimal capacityMax;

    @TableField("is_active")
    private Boolean isActive;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
