package com.sld.backend.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.user.entity.BusinessInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业认证信息 Mapper
 */
@Mapper
public interface BusinessInfoMapper extends BaseMapper<BusinessInfo> {
}
