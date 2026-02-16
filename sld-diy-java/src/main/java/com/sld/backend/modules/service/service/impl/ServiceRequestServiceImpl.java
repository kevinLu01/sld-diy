package com.sld.backend.modules.service.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.service.dto.request.CreateServiceRequest;
import com.sld.backend.modules.service.dto.request.RateServiceRequest;
import com.sld.backend.modules.service.dto.response.ServiceRequestVO;
import com.sld.backend.modules.service.entity.ServiceRequest;
import com.sld.backend.modules.service.mapper.ServiceRequestMapper;
import com.sld.backend.modules.service.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务请求服务实现
 */
@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestMapper serviceRequestMapper;

    private static final Map<String, String> SERVICE_TYPE_NAMES = new HashMap<>();
    private static final Map<String, String> PRIORITY_NAMES = new HashMap<>();
    private static final Map<String, String> STATUS_NAMES = new HashMap<>();

    static {
        SERVICE_TYPE_NAMES.put("installation", "安装服务");
        SERVICE_TYPE_NAMES.put("maintenance", "维护服务");
        SERVICE_TYPE_NAMES.put("consultation", "咨询服务");
        SERVICE_TYPE_NAMES.put("repair", "维修服务");

        PRIORITY_NAMES.put("low", "低");
        PRIORITY_NAMES.put("normal", "正常");
        PRIORITY_NAMES.put("high", "高");
        PRIORITY_NAMES.put("urgent", "紧急");

        STATUS_NAMES.put("pending", "待处理");
        STATUS_NAMES.put("processing", "处理中");
        STATUS_NAMES.put("completed", "已完成");
        STATUS_NAMES.put("cancelled", "已取消");
    }

    @Override
    public ServiceRequestVO createServiceRequest(Long userId, CreateServiceRequest request) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setRequestNo(generateRequestNo());
        serviceRequest.setUserId(userId);
        serviceRequest.setOrderId(request.getOrderId());
        serviceRequest.setServiceType(request.getServiceType());
        serviceRequest.setPriority(request.getPriority() != null ? request.getPriority() : "normal");
        serviceRequest.setDescription(request.getDescription());
        serviceRequest.setContactInfo(request.getContactInfo());
        serviceRequest.setScheduledTime(request.getScheduledTime());
        serviceRequest.setStatus("pending");
        serviceRequest.setCreateTime(LocalDateTime.now());
        serviceRequest.setUpdateTime(LocalDateTime.now());

        serviceRequestMapper.insert(serviceRequest);
        return convertToVO(serviceRequest);
    }

    @Override
    public Page<ServiceRequestVO> listServiceRequests(Long userId, String status, Long page, Long limit) {
        Page<ServiceRequest> pageParam = new Page<>(page, limit);

        List<ServiceRequest> list = serviceRequestMapper.selectByUserId(userId);
        List<ServiceRequestVO> voList = list.stream()
                .filter(r -> status == null || status.equals(r.getStatus()))
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<ServiceRequestVO> result = new Page<>(page, limit, voList.size());
        result.setRecords(voList);
        return result;
    }

    @Override
    public ServiceRequestVO getServiceRequest(String requestNo, Long userId) {
        ServiceRequest serviceRequest = serviceRequestMapper.selectByRequestNo(requestNo);
        if (serviceRequest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "服务请求不存在");
        }
        if (!serviceRequest.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权访问此服务请求");
        }
        return convertToVO(serviceRequest);
    }

    @Override
    public void rateService(String requestNo, Long userId, RateServiceRequest request) {
        ServiceRequest serviceRequest = serviceRequestMapper.selectByRequestNo(requestNo);
        if (serviceRequest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "服务请求不存在");
        }
        if (!serviceRequest.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权评价此服务");
        }
        if (!"completed".equals(serviceRequest.getStatus())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "服务未完成，无法评价");
        }

        serviceRequest.setRating(request.getRating());
        serviceRequest.setFeedback(request.getFeedback());
        serviceRequest.setUpdateTime(LocalDateTime.now());

        serviceRequestMapper.updateById(serviceRequest);
    }

    private String generateRequestNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "SR" + dateStr + random;
    }

    private ServiceRequestVO convertToVO(ServiceRequest serviceRequest) {
        ServiceRequestVO vo = new ServiceRequestVO();
        BeanUtils.copyProperties(serviceRequest, vo);
        vo.setServiceTypeName(SERVICE_TYPE_NAMES.getOrDefault(serviceRequest.getServiceType(), serviceRequest.getServiceType()));
        vo.setPriorityName(PRIORITY_NAMES.getOrDefault(serviceRequest.getPriority(), serviceRequest.getPriority()));
        vo.setStatusName(STATUS_NAMES.getOrDefault(serviceRequest.getStatus(), serviceRequest.getStatus()));
        return vo;
    }
}
