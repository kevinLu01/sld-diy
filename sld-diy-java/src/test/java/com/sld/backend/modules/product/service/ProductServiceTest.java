package com.sld.backend.modules.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.product.dto.response.ProductDetailVO;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.product.dto.response.ReviewVO;
import com.sld.backend.modules.product.entity.*;
import com.sld.backend.modules.product.mapper.*;
import com.sld.backend.modules.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 产品服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("产品服务测试")
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private ProductSpecMapper productSpecMapper;

    @Mock
    private ProductAttrMapper productAttrMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private CompatibilityMapper compatibilityMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Category testCategory;
    private Brand testBrand;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setSku("SKU001");
        testProduct.setCategoryId(1L);
        testProduct.setBrandId(1L);
        testProduct.setPrice(new BigDecimal("100.00"));
        testProduct.setOriginalPrice(new BigDecimal("120.00"));
        testProduct.setStock(100);
        testProduct.setSalesCount(50);
        testProduct.setRating(new BigDecimal("4.5"));
        testProduct.setReviewCount(10);
        testProduct.setStatus("on_shelf");
        testProduct.setImages("[\"image1.jpg\", \"image2.jpg\"]");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");

        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Test Brand");
    }

    @Test
    @DisplayName("获取产品列表 - 成功")
    void testListProducts_Success() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new Page<>(1, 20, 1);
        productPage.setRecords(products);

        when(productMapper.selectPage(any(Page.class), any())).thenReturn(productPage);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);

        // Act
        Page<ProductVO> result = productService.listProducts(null, null, null, 1L, 20L, "new");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);

        verify(productMapper).selectPage(any(Page.class), any());
    }

    @Test
    @DisplayName("获取产品列表 - 按分类筛选")
    void testListProducts_ByCategory() {
        // Arrange
        List<Product> products = Arrays.asList(testProduct);
        Page<Product> productPage = new Page<>(1, 20, 1);
        productPage.setRecords(products);

        when(productMapper.selectPage(any(Page.class), any())).thenReturn(productPage);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);

        // Act
        Page<ProductVO> result = productService.listProducts(1L, null, null, 1L, 20L, "new");

        // Assert
        assertThat(result).isNotNull();

        verify(productMapper).selectPage(any(Page.class), any());
    }

    @Test
    @DisplayName("获取产品详情 - 成功")
    void testGetProductDetail_Success() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);
        when(productSpecMapper.selectByProductId(1L)).thenReturn(new ArrayList<>());
        when(productAttrMapper.selectByProductId(1L)).thenReturn(new ArrayList<>());
        when(compatibilityMapper.selectByProductId(1L)).thenReturn(new ArrayList<>());
        when(reviewMapper.selectRatingDistribution(1L)).thenReturn(new ArrayList<>());

        // Act
        ProductDetailVO result = productService.getProductDetail(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("100.00"));

        verify(productMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取产品详情 - 产品不存在")
    void testGetProductDetail_NotFound() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> productService.getProductDetail(1L));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());
    }

    @Test
    @DisplayName("获取产品兼容性 - 成功")
    void testGetProductCompatibility_Success() {
        // Arrange
        Compatibility compat = new Compatibility();
        compat.setProductAId(1L);
        compat.setProductBId(2L);
        compat.setCompatibilityType("compatible");
        compat.setNotes("Compatible products");

        Product compatProduct = new Product();
        compatProduct.setId(2L);
        compatProduct.setName("Compatible Product");
        compatProduct.setSku("SKU002");
        compatProduct.setPrice(new BigDecimal("80.00"));

        when(compatibilityMapper.selectByProductId(1L)).thenReturn(Arrays.asList(compat));
        when(productMapper.selectById(2L)).thenReturn(compatProduct);

        // Act
        List<Map<String, Object>> result = productService.getProductCompatibility(1L);

        // Assert
        assertThat(result).isNotEmpty();

        verify(compatibilityMapper).selectByProductId(1L);
    }

    @Test
    @DisplayName("获取产品评价 - 成功")
    void testGetProductReviews_Success() {
        // Arrange
        List<Review> reviews = new ArrayList<>();
        Review review = new Review();
        review.setId(1L);
        review.setUserId(1L);
        review.setProductId(1L);
        review.setRating(5);
        review.setContent("Great product!");
        review.setIsAnonymous(false);
        reviews.add(review);

        Page<Review> reviewPage = new Page<>(1, 10, 1);
        reviewPage.setRecords(reviews);

        when(reviewMapper.selectPage(any(Page.class), any())).thenReturn(reviewPage);

        // Act
        Page<ReviewVO> result = productService.getProductReviews(1L, 1L, 10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);

        verify(reviewMapper).selectPage(any(Page.class), any());
    }

    @Test
    @DisplayName("获取产品规格 - 成功")
    void testGetProductSpecs_Success() {
        // Arrange
        List<ProductSpec> specs = new ArrayList<>();
        ProductSpec spec = new ProductSpec();
        spec.setId(1L);
        spec.setProductId(1L);
        spec.setSpecKey("Power");
        spec.setSpecValue("2HP");
        spec.setUnit("");
        specs.add(spec);

        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);
        when(productSpecMapper.selectByProductId(1L)).thenReturn(specs);
        when(productAttrMapper.selectByProductId(1L)).thenReturn(new ArrayList<>());
        when(compatibilityMapper.selectByProductId(1L)).thenReturn(new ArrayList<>());
        when(reviewMapper.selectRatingDistribution(1L)).thenReturn(new ArrayList<>());

        // Act
        ProductDetailVO result = productService.getProductDetail(1L);

        // Assert
        assertThat(result.getSpecifications()).isNotEmpty();
        assertThat(result.getSpecifications().get("Power")).isEqualTo("2HP");
    }
}
