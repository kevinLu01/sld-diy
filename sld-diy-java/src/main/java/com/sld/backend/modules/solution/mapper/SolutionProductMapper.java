package com.sld.backend.modules.solution.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.solution.entity.SolutionProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 方案产品关联 Mapper
 */
@Mapper
public interface SolutionProductMapper extends BaseMapper<SolutionProduct> {

    /**
     * 根据方案ID获取产品列表
     */
    @Select("SELECT * FROM t_solution_product WHERE solution_id = #{solutionId} ORDER BY create_time")
    List<SolutionProduct> selectBySolutionId(@Param("solutionId") Long solutionId);
}
