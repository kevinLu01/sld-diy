package com.sld.backend.modules.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.cart.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车 Mapper
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
}
