package com.sld.backend.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
