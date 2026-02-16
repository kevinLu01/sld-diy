package com.sld.backend.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.product.entity.ProductAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品属性 Mapper
 */
@Mapper
public interface ProductAttrMapper extends BaseMapper<ProductAttr> {

    /**
     * 根据产品ID获取属性列表
     */
    @Select("SELECT * FROM t_product_attr WHERE product_id = #{productId} ORDER BY sort_order")
    List<ProductAttr> selectByProductId(@Param("productId") Long productId);
}
