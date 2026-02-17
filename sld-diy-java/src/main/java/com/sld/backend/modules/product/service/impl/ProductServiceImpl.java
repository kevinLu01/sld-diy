package com.sld.backend.modules.product.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.product.dto.response.*;
import com.sld.backend.modules.product.entity.*;
import com.sld.backend.modules.product.mapper.*;
import com.sld.backend.modules.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品服务实现
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;
    private final ProductSpecMapper productSpecMapper;
    private final ProductAttrMapper productAttrMapper;
    private final ReviewMapper reviewMapper;
    private final CompatibilityMapper compatibilityMapper;

    @Override
    public Page<ProductVO> listProducts(Long categoryId, String brand, String search, Long page, Long limit, String sort) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        // 兼容历史数据：旧数据常用 active，新数据使用 on_shelf
        wrapper.in(Product::getStatus, Arrays.asList("on_shelf", "active"));

        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (StrUtil.isNotBlank(brand)) {
            try {
                wrapper.eq(Product::getBrandId, Long.parseLong(brand));
            } catch (NumberFormatException ignore) {
                // legacy 参数可能是品牌名，这里忽略避免错误过滤
            }
        }
        if (StrUtil.isNotBlank(search)) {
            wrapper.and(w -> w.like(Product::getName, search).or().like(Product::getDescription, search));
        }

        // 排序
        switch (sort) {
            case "sales":
                wrapper.orderByDesc(Product::getSalesCount);
                break;
            case "price_asc":
                wrapper.orderByAsc(Product::getPrice);
                break;
            case "price_desc":
                wrapper.orderByDesc(Product::getPrice);
                break;
            case "new":
            default:
                wrapper.orderByDesc(Product::getCreatedAt);
                break;
        }

        Page<Product> productPage = productMapper.selectPage(new Page<>(page, limit), wrapper);
        List<ProductVO> vos = productPage.getRecords().stream()
            .map(this::toVO)
            .collect(Collectors.toList());

        Page<ProductVO> voPage = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        voPage.setRecords(vos);
        return voPage;
    }

    @Override
    public ProductDetailVO getProductDetail(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Category category = categoryMapper.selectById(product.getCategoryId());
        Brand brand = brandMapper.selectById(product.getBrandId());

        // 获取规格和属性
        List<ProductSpec> specs = productSpecMapper.selectByProductId(id);
        List<ProductAttr> attrs = productAttrMapper.selectByProductId(id);

        ProductDetailVO vo = ProductDetailVO.builder()
            .id(product.getId())
            .sku(product.getSku())
            .name(product.getName())
            .brandId(product.getBrandId())
            .brandName(brand != null ? brand.getName() : null)
            .categoryId(product.getCategoryId())
            .categoryName(category != null ? category.getName() : null)
            .price(product.getPrice())
            .originalPrice(product.getOriginalPrice())
            .stock(product.getStockQuantity())
            .images(parseImages(product.getImages()))
            .video(product.getVideoUrl())
            .model3d(product.getModel3dUrl())
            .specifications(new LinkedHashMap<>(parseSpecifications(specs)))
            .description(product.getDescription())
            .salesCount(product.getSalesCount())
            .rating(product.getRating())
            .reviewCount(0)
            .status(product.getStatus())
            .build();

        // 设置兼容产品
        vo.setCompatibleProducts(getCompatibleProductVOs(id));
        
        // 设置评价统计
        vo.setReviews(getReviewStats(id, product.getRating(), 0));

        return vo;
    }

    @Override
    public List<Map<String, Object>> getProductCompatibility(Long productId) {
        List<Compatibility> compatibilities = compatibilityMapper.selectByProductId(productId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Compatibility compat : compatibilities) {
            Map<String, Object> item = new HashMap<>();
            item.put("productId", compat.getProductBId());
            item.put("compatibilityType", compat.getCompatibilityType());
            item.put("notes", compat.getNotes());
            
            // 获取产品B的信息
            Product productB = productMapper.selectById(compat.getProductBId());
            if (productB != null) {
                Map<String, Object> productInfo = new HashMap<>();
                productInfo.put("id", productB.getId());
                productInfo.put("name", productB.getName());
                productInfo.put("sku", productB.getSku());
                productInfo.put("price", productB.getPrice());
                productInfo.put("images", parseImages(productB.getImages()));
                item.put("product", productInfo);
            }
            result.add(item);
        }
        return result;
    }

    @Override
    public Page<ReviewVO> getProductReviews(Long productId, Long page, Long limit) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getProductId, productId)
               .eq(Review::getStatus, "published")
               .orderByDesc(Review::getCreateTime);
        
        Page<Review> reviewPage = reviewMapper.selectPage(new Page<>(page, limit), wrapper);
        List<ReviewVO> vos = reviewPage.getRecords().stream()
            .map(this::toReviewVO)
            .collect(Collectors.toList());

        Page<ReviewVO> voPage = new Page<>(reviewPage.getCurrent(), reviewPage.getSize(), reviewPage.getTotal());
        voPage.setRecords(vos);
        return voPage;
    }

    private ProductVO toVO(Product product) {
        Category category = categoryMapper.selectById(product.getCategoryId());
        Brand brand = brandMapper.selectById(product.getBrandId());

        return ProductVO.builder()
            .id(product.getId())
            .sku(product.getSku())
            .name(product.getName())
            .brandId(product.getBrandId())
            .brandName(brand != null ? brand.getName() : null)
            .categoryId(product.getCategoryId())
            .categoryName(category != null ? category.getName() : null)
            .price(product.getPrice())
            .originalPrice(product.getOriginalPrice())
            .stock(product.getStockQuantity())
            .images(parseImages(product.getImages()))
            .salesCount(product.getSalesCount())
            .rating(product.getRating())
            .status(product.getStatus())
            .specifications(null)
            .build();
    }

    private ReviewVO toReviewVO(Review review) {
        ReviewVO vo = new ReviewVO();
        vo.setId(review.getId());
        vo.setUserId(review.getUserId());
        vo.setRating(review.getRating());
        vo.setContent(review.getContent());
        vo.setIsAnonymous(review.getIsAnonymous());
        vo.setCreateTime(review.getCreateTime());
        if (!Boolean.TRUE.equals(review.getIsAnonymous())) {
            vo.setUsername("用户" + review.getUserId()); // 实际应该从用户表获取
        } else {
            vo.setUsername("匿名用户");
        }
        if (StrUtil.isNotBlank(review.getImages())) {
            vo.setImages(JSONUtil.toList(review.getImages(), String.class));
        } else {
            vo.setImages(Collections.emptyList());
        }
        return vo;
    }

    private List<String> parseImages(String images) {
        if (StrUtil.isBlank(images)) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.toList(images, String.class);
        } catch (Exception ignored) {
            // Backward compatibility: support plain URL or comma-separated URLs in legacy data.
            if (images.contains(",")) {
                return Arrays.stream(images.split(","))
                    .map(String::trim)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.toList());
            }
            return Collections.singletonList(images.trim());
        }
    }

    private Map<String, String> parseSpecifications(List<ProductSpec> specs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (ProductSpec spec : specs) {
            String value = spec.getSpecValue();
            if (StrUtil.isNotBlank(spec.getUnit())) {
                value += spec.getUnit();
            }
            map.put(spec.getSpecKey(), value);
        }
        return map;
    }

    private List<Map<String, Object>> parseAttributes(List<ProductAttr> attrs) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProductAttr attr : attrs) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", attr.getAttrName());
            map.put("value", attr.getAttrValue());
            map.put("unit", attr.getUnit());
            list.add(map);
        }
        return list;
    }

    private List<Map<String, Object>> getCompatibleProducts(Long productId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Compatibility> compatibilities = compatibilityMapper.selectByProductId(productId);
        
        for (Compatibility compat : compatibilities) {
            if ("recommended".equals(compat.getCompatibilityType()) || "compatible".equals(compat.getCompatibilityType())) {
                Product product = productMapper.selectById(compat.getProductBId());
                if (product != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", product.getId());
                    item.put("name", product.getName());
                    item.put("price", product.getPrice());
                    item.put("compatibilityType", compat.getCompatibilityType());
                    item.put("note", compat.getNotes());
                    result.add(item);
                }
            }
        }
        return result;
    }

    private List<CompatibleProductVO> getCompatibleProductVOs(Long productId) {
        List<CompatibleProductVO> result = new ArrayList<>();
        List<Compatibility> compatibilities = compatibilityMapper.selectByProductId(productId);

        for (Compatibility compat : compatibilities) {
            if ("recommended".equals(compat.getCompatibilityType()) || "compatible".equals(compat.getCompatibilityType())) {
                Product product = productMapper.selectById(compat.getProductBId());
                if (product != null) {
                    CompatibleProductVO vo = CompatibleProductVO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .compatibilityType(compat.getCompatibilityType())
                        .build();
                    result.add(vo);
                }
            }
        }
        return result;
    }

    private ReviewStatsVO getReviewStats(Long productId, BigDecimal rating, Integer reviewCount) {
        // 获取评分分布
        List<Map<String, Object>> distribution = reviewMapper.selectRatingDistribution(productId);
        Map<String, Integer> distMap = new HashMap<>();
        for (Map<String, Object> map : distribution) {
            Integer ratingKey = (Integer) map.get("rating");
            Long count = (Long) map.get("count");
            distMap.put(String.valueOf(ratingKey), count.intValue());
        }

        return ReviewStatsVO.builder()
            .average(rating != null ? rating : BigDecimal.ZERO)
            .count(reviewCount != null ? reviewCount : 0)
            .distribution(distMap)
            .build();
    }
}
