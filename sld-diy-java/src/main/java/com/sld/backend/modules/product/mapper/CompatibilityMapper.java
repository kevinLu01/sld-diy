package com.sld.backend.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.product.entity.Compatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品兼容性 Mapper
 */
@Mapper
public interface CompatibilityMapper extends BaseMapper<Compatibility> {

    /**
     * 获取产品的兼容性信息
     */
    @Select("SELECT c.*, p.id as product_b_id, p.name as product_b_name, p.sku as product_b_sku " +
            "FROM t_compatibility c " +
            "LEFT JOIN t_product p ON c.product_b_id = p.id " +
            "WHERE c.product_a_id = #{productId}")
    List<Compatibility> selectByProductId(@Param("productId") Long productId);

    /**
     * 检查产品间的兼容性
     */
    @Select("SELECT * FROM t_compatibility " +
            "WHERE (product_a_id = #{productAId} AND product_b_id = #{productBId}) " +
            "OR (product_a_id = #{productBId} AND product_b_id = #{productAId})")
    Compatibility checkCompatibility(@Param("productAId") Long productAId, @Param("productBId") Long productBId);
}
