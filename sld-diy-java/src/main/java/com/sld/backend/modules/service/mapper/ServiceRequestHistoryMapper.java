package com.sld.backend.modules.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.service.entity.ServiceRequestHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceRequestHistoryMapper extends BaseMapper<ServiceRequestHistory> {
}
