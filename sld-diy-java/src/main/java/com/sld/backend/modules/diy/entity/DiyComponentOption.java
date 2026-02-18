package com.sld.backend.modules.diy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_diy_component_option")
public class DiyComponentOption {

    @TableId
    private Long id;

    @TableField("scene_component_id")
    private Long sceneComponentId;

    @TableField("product_id")
    private Long productId;

    @TableField("option_name")
    private String optionName;

    @TableField("brand_name")
    private String brandName;

    @TableField("model_spec")
    private String modelSpec;

    @TableField("spec_json")
    private String specJson;

    @TableField("base_price")
    private BigDecimal basePrice;

    @TableField("is_active")
    private Boolean isActive;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
