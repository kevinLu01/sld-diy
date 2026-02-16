package com.sld.backend.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品 Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
