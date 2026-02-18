package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiySceneTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiySceneTemplateMapper extends BaseMapper<DiySceneTemplate> {

    @Select("SELECT * FROM t_diy_scene_template WHERE is_active = 1 ORDER BY sort_order, id")
    List<DiySceneTemplate> selectAllActive();
}
