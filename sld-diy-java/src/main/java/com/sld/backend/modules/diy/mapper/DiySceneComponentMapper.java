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
        "SELECT * FROM t_diy_scene_component",
        "WHERE is_active = 1 AND scene_id IN",
        "<foreach collection='sceneIds' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "ORDER BY sort_order, id",
        "</script>"
    })
    List<DiySceneComponent> selectBySceneIds(@Param("sceneIds") List<Long> sceneIds);
}
