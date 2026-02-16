package com.sld.backend.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.system.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 轮播图 Mapper
 */
@Mapper
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 根据位置获取启用的轮播图
     */
    @Select("SELECT * FROM t_banner WHERE position = #{position} AND is_active = 1 ORDER BY sort_order")
    List<Banner> selectActiveByPosition(@Param("position") String position);
}
