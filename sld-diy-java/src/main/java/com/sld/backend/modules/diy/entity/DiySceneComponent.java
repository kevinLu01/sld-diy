package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_diy_scene_component")
public class DiySceneComponent {

    @TableId
    private Long id;

    @TableField("scene_id")
    private Long sceneId;

    @TableField("component_code")
    private String componentCode;

    @TableField("component_name")
    private String componentName;

    @TableField("component_role")
    private String componentRole;

    @TableField("is_required")
    private Boolean required;

    @TableField("min_qty")
    private Integer minQty;

    @TableField("max_qty")
    private Integer maxQty;

    @TableField("selection_mode")
    private String selectionMode;

    @TableField("spec_requirement")
    private String specRequirement;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("is_active")
    private Boolean isActive;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
