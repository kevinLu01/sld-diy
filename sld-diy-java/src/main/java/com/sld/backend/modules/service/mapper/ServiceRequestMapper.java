package com.sld.backend.modules.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.service.entity.ServiceRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 服务请求 Mapper
 */
@Mapper
public interface ServiceRequestMapper extends BaseMapper<ServiceRequest> {

    /**
     * 根据用户ID获取服务请求列表
     */
    @Select("SELECT id,request_no AS requestNo,user_id AS userId,order_id AS orderId,service_type AS serviceType,priority,description,contact_info AS contactInfo,scheduled_time AS scheduledTime,assigned_to AS assignedTo,status,resolution,rating,feedback,create_time AS createTime,update_time AS updateTime FROM t_service_request WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<ServiceRequest> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据服务单号获取详情
     */
    @Select("SELECT id,request_no AS requestNo,user_id AS userId,order_id AS orderId,service_type AS serviceType,priority,description,contact_info AS contactInfo,scheduled_time AS scheduledTime,assigned_to AS assignedTo,status,resolution,rating,feedback,create_time AS createTime,update_time AS updateTime FROM t_service_request WHERE request_no = #{requestNo}")
    ServiceRequest selectByRequestNo(@Param("requestNo") String requestNo);
}
