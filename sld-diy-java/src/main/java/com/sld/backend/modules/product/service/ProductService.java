package com.sld.backend.modules.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.product.dto.response.ProductDetailVO;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.product.dto.response.ReviewVO;

import java.util.List;
import java.util.Map;

/**
 * 产品服务接口
 */
public interface ProductService {

    /**
     * 获取产品列表
     */
    Page<ProductVO> listProducts(Long categoryId, String brand, String search, Long page, Long limit, String sort);

    /**
     * 获取产品详情
     */
    ProductDetailVO getProductDetail(Long id);

    /**
     * 获取产品兼容性
     */
    List<Map<String, Object>> getProductCompatibility(Long productId);

    /**
     * 获取产品评价
     */
    Page<ReviewVO> getProductReviews(Long productId, Long page, Long limit);
}
