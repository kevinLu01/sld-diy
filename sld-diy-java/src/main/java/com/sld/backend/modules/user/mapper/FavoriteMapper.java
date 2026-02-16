package com.sld.backend.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.user.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户收藏 Mapper
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    /**
     * 获取用户收藏列表（含产品信息）
     */
    @Select("SELECT f.*, p.id as product_id, p.name as product_name, p.sku as product_sku, " +
            "p.price as product_price, p.images as product_images, b.name as brand_name " +
            "FROM t_favorite f " +
            "LEFT JOIN t_product p ON f.product_id = p.id " +
            "LEFT JOIN t_brand b ON p.brand_id = b.id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.create_time DESC")
    List<Favorite> selectFavoritesWithProduct(@Param("userId") Long userId);

    /**
     * 检查是否已收藏
     */
    @Select("SELECT COUNT(*) FROM t_favorite WHERE user_id = #{userId} AND product_id = #{productId}")
    int checkFavoriteExists(@Param("userId") Long userId, @Param("productId") Long productId);
}
