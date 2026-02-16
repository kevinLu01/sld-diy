package com.sld.backend.modules.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.product.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 产品评价 Mapper
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 获取产品评价列表（含用户信息）
     */
    @Select("SELECT r.*, u.username, u.avatar " +
            "FROM t_review r " +
            "LEFT JOIN t_user u ON r.user_id = u.id " +
            "WHERE r.product_id = #{productId} AND r.status = 'published' " +
            "ORDER BY r.create_time DESC")
    List<Review> selectByProductId(@Param("productId") Long productId);

    /**
     * 获取产品评价统计
     */
    @Select("SELECT COUNT(*) as total, AVG(rating) as avg_rating " +
            "FROM t_review WHERE product_id = #{productId} AND status = 'published'")
    java.util.Map<String, Object> selectStatsByProductId(@Param("productId") Long productId);

    /**
     * 获取评分分布
     */
    @Select("SELECT rating, COUNT(*) as count " +
            "FROM t_review WHERE product_id = #{productId} AND status = 'published' " +
            "GROUP BY rating")
    List<java.util.Map<String, Object>> selectRatingDistribution(@Param("productId") Long productId);
}
