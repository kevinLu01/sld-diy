package com.sld.backend.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.product.entity.ProductSpec;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品规格 Mapper
 */
@Mapper
public interface ProductSpecMapper extends BaseMapper<ProductSpec> {

    /**
     * 根据产品ID获取规格列表
     */
    @Select("SELECT * FROM t_product_spec WHERE product_id = #{productId} ORDER BY sort_order")
    List<ProductSpec> selectByProductId(@Param("productId") Long productId);
}
