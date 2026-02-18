package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiyComponentOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiyComponentOptionMapper extends BaseMapper<DiyComponentOption> {

    @Select({
        "<script>",
        "SELECT id, scene_component_id AS sceneComponentId, product_id AS productId,",
        "option_name AS optionName, brand_name AS brandName, model_spec AS modelSpec,",
        "spec_json AS specJson, base_price AS basePrice, is_active AS isActive,",
        "sort_order AS sortOrder, create_time AS createTime, update_time AS updateTime",
        "FROM t_diy_component_option",
        "WHERE is_active = 1 AND scene_component_id IN",
        "<foreach collection='componentIds' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "ORDER BY sort_order, id",
        "</script>"
    })
    List<DiyComponentOption> selectByComponentIds(@Param("componentIds") List<Long> componentIds);
}
