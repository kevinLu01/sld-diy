package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiySceneComponent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiySceneComponentMapper extends BaseMapper<DiySceneComponent> {

    @Select({
        "<script>",
        "SELECT id, scene_id AS sceneId, component_code AS componentCode, component_name AS componentName,",
        "component_role AS componentRole, is_required AS required, min_qty AS minQty, max_qty AS maxQty,",
        "selection_mode AS selectionMode, spec_requirement AS specRequirement,",
        "sort_order AS sortOrder, is_active AS isActive, create_time AS createTime, update_time AS updateTime",
        "FROM t_diy_scene_component",
        "WHERE is_active = 1 AND scene_id IN",
        "<foreach collection='sceneIds' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "ORDER BY sort_order, id",
        "</script>"
    })
    List<DiySceneComponent> selectBySceneIds(@Param("sceneIds") List<Long> sceneIds);
}
