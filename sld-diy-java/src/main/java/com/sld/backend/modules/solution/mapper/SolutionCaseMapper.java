package com.sld.backend.modules.solution.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.solution.entity.SolutionCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 解决方案案例 Mapper
 */
@Mapper
public interface SolutionCaseMapper extends BaseMapper<SolutionCase> {

    /**
     * 根据解决方案ID获取案例列表
     */
    @Select("SELECT * FROM t_solution_case WHERE solution_id = #{solutionId} ORDER BY create_time DESC")
    List<SolutionCase> selectBySolutionId(@Param("solutionId") Long solutionId);
}
