package com.sld.backend.modules.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.service.dto.request.CreateServiceRequest;
import com.sld.backend.modules.service.dto.request.RateServiceRequest;
import com.sld.backend.modules.service.dto.response.ServiceRequestVO;

/**
 * 服务请求服务接口
 */
public interface ServiceRequestService {

    /**
     * 创建服务请求
     */
    ServiceRequestVO createServiceRequest(Long userId, CreateServiceRequest request);

    /**
     * 获取用户的服务请求列表
     */
    Page<ServiceRequestVO> listServiceRequests(Long userId, String status, Long page, Long limit);

    /**
     * 获取服务请求详情
     */
    ServiceRequestVO getServiceRequest(String requestNo, Long userId);

    /**
     * 评价服务
     */
    void rateService(String requestNo, Long userId, RateServiceRequest request);
}
