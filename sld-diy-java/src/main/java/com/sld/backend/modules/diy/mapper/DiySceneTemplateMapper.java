package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiySceneTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiySceneTemplateMapper extends BaseMapper<DiySceneTemplate> {

    @Select("SELECT id, scene_code AS sceneCode, name, description, application_notes AS applicationNotes, " +
        "temp_min AS tempMin, temp_max AS tempMax, capacity_min AS capacityMin, capacity_max AS capacityMax, " +
        "is_active AS isActive, sort_order AS sortOrder, create_time AS createTime, update_time AS updateTime " +
        "FROM t_diy_scene_template WHERE is_active = 1 ORDER BY sort_order, id")
    List<DiySceneTemplate> selectAllActive();
}
