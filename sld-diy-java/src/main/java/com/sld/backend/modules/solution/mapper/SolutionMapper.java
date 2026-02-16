package com.sld.backend.modules.solution.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.solution.entity.Solution;
import org.apache.ibatis.annotations.Mapper;

/**
 * 解决方案 Mapper
 */
@Mapper
public interface SolutionMapper extends BaseMapper<Solution> {
}
