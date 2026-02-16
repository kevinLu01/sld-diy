package com.sld.backend.modules.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.cart.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 购物车 Mapper
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 根据用户ID获取购物车
     */
    @Select("SELECT * FROM t_cart WHERE user_id = #{userId}")
    Cart selectByUserId(@Param("userId") Long userId);
}
