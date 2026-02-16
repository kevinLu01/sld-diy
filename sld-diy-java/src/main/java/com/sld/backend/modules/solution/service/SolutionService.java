package com.sld.backend.modules.solution.service;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.solution.dto.response.SolutionCaseVO;
import com.sld.backend.modules.solution.dto.response.SolutionVO;

import java.util.List;

/**
 * 解决方案服务接口
 */
public interface SolutionService {

    /**
     * 获取解决方案列表
     */
    PageResult<SolutionVO> listSolutions(String industry, String scenario, String temperatureRange, Long page, Long limit);

    /**
     * 获取解决方案详情
     */
    SolutionVO getSolution(Long id);

    /**
     * 获取解决方案案例
     */
    List<SolutionCaseVO> getSolutionCases(Long solutionId);

    /**
     * 基于解决方案创建订单
     */
    OrderVO createOrderFromSolution(Long solutionId, Long userId);
}
