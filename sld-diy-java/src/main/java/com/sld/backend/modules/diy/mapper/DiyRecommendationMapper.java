package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiyRecommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DIY推荐配置 Mapper
 */
@Mapper
public interface DiyRecommendationMapper extends BaseMapper<DiyRecommendation> {

    /**
     * 根据场景获取推荐配置
     */
    @Select("SELECT dr.*, c.name as category_name, c.slug as category_slug " +
            "FROM DiyRecommendation dr " +
            "LEFT JOIN Category c ON dr.category_id = c.id " +
            "WHERE dr.scenario = #{scenario} AND dr.is_active = 1 " +
            "ORDER BY dr.priority")
    List<DiyRecommendation> selectByScenario(@Param("scenario") String scenario);

    /**
     * 获取所有启用的推荐配置
     */
    @Select("SELECT dr.*, c.name as category_name, c.slug as category_slug " +
            "FROM DiyRecommendation dr " +
            "LEFT JOIN Category c ON dr.category_id = c.id " +
            "WHERE dr.is_active = 1 " +
            "ORDER BY dr.scenario, dr.priority")
    List<DiyRecommendation> selectAllActive();
}
