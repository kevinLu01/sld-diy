package com.sld.backend.modules.diy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.diy.entity.DiyConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DIY配置 Mapper
 */
@Mapper
public interface DiyConfigMapper extends BaseMapper<DiyConfig> {

    /**
     * 根据分类获取配置列表
     */
    @Select("SELECT * FROM DiyConfig WHERE category = #{category} AND isActive = 1 ORDER BY sortOrder")
    List<DiyConfig> selectByCategory(@Param("category") String category);

    /**
     * 获取所有启用的配置
     */
    @Select("SELECT * FROM DiyConfig WHERE isActive = 1 ORDER BY category, sortOrder")
    List<DiyConfig> selectAllActive();
}
