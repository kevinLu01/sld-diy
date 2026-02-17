package com.sld.backend.modules.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.product.dto.response.ProductDetailVO;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.product.entity.Brand;
import com.sld.backend.modules.product.entity.Category;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.BrandMapper;
import com.sld.backend.modules.product.mapper.CategoryMapper;
import com.sld.backend.modules.product.mapper.CompatibilityMapper;
import com.sld.backend.modules.product.mapper.ProductMapper;
import com.sld.backend.modules.product.mapper.ProductAttrMapper;
import com.sld.backend.modules.product.mapper.ProductSpecMapper;
import com.sld.backend.modules.product.mapper.ReviewMapper;
import com.sld.backend.modules.product.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        // 初始化测试分类
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("压缩机");
        testCategory.setParentId(0L);
        testCategory.setIsActive(true);
        testCategory.setCreatedAt(LocalDateTime.now());

        // 初始化测试品牌
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("Danfoss");
        testBrand.setDescription("丹麦品牌");

        // 初始化测试产品 - 使用正确的字段名
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("DF-001");
        testProduct.setName("Danfoss 压缩机");
        testProduct.setCategoryId(1L);
        testProduct.setBrandId(1L);
        testProduct.setPrice(new BigDecimal("2500.00"));
        testProduct.setOriginalPrice(new BigDecimal("3000.00"));
        testProduct.setStockQuantity(100);
        testProduct.setSalesCount(50);
        testProduct.setRating(new BigDecimal("4.5"));
        testProduct.setStatus("on_shelf");
        testProduct.setImages("[\"https://example.com/product1.jpg\"]");
        testProduct.setDescription("高效压缩机");
        testProduct.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("获取产品列表 - 成功")
    void testListProducts_Success() {
        // Arrange
        Page<Product> productPage = new Page<>(1, 10);
        productPage.setRecords(List.of(testProduct));
        productPage.setTotal(1);

        when(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(productPage);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);

        // Act
        Page<ProductVO> result = productService.listProducts(1L, null, null, 1L, 10L, "new");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getName()).isEqualTo("Danfoss 压缩机");

        verify(productMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取产品详情 - 成功")
    void testGetProductDetail_Success() {
        // Arrange
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);
        when(productSpecMapper.selectByProductId(1L)).thenReturn(List.of());
        when(productAttrMapper.selectByProductId(1L)).thenReturn(List.of());
        when(compatibilityMapper.selectByProductId(1L)).thenReturn(List.of());
        when(reviewMapper.selectRatingDistribution(1L)).thenReturn(List.of());

        // Act
        ProductDetailVO result = productService.getProductDetail(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Danfoss 压缩机");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("2500.00"));

        verify(productMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取产品详情 - 产品不存在")
    void testGetProductDetail_NotFound() {
        // Arrange
        when(productMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> productService.getProductDetail(999L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND.getCode());

        verify(productMapper).selectById(999L);
    }

    @Test
    @DisplayName("获取产品列表 - 按销量排序")
    void testListProducts_SortBySales() {
        // Arrange
        Page<Product> productPage = new Page<>(1, 10);
        productPage.setRecords(List.of(testProduct));
        productPage.setTotal(1);

        when(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(productPage);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);

        // Act
        Page<ProductVO> result = productService.listProducts(null, null, null, 1L, 10L, "sales");

        // Assert
        assertThat(result).isNotNull();
        verify(productMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取产品列表 - 按价格升序")
    void testListProducts_SortByPriceAsc() {
        // Arrange
        Page<Product> productPage = new Page<>(1, 10);
        productPage.setRecords(List.of(testProduct));
        productPage.setTotal(1);

        when(productMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(productPage);
        when(categoryMapper.selectById(1L)).thenReturn(testCategory);
        when(brandMapper.selectById(1L)).thenReturn(testBrand);

        // Act
        Page<ProductVO> result = productService.listProducts(null, null, null, 1L, 10L, "price_asc");

        // Assert
        assertThat(result).isNotNull();
        verify(productMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }
}
