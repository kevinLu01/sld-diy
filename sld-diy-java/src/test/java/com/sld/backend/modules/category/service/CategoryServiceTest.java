package com.sld.backend.modules.category.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.category.dto.response.CategoryVO;
import com.sld.backend.modules.product.entity.Category;
import com.sld.backend.modules.product.mapper.CategoryMapper;
import com.sld.backend.modules.category.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 分类服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("分类服务测试")
class CategoryServiceTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category parentCategory;
    private Category childCategory1;
    private Category childCategory2;

    @BeforeEach
    void setUp() {
        // 初始化父分类
        parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("制冷配件");
        parentCategory.setSlug("cooling");
        parentCategory.setParentId(0L);
        parentCategory.setSortOrder(1);
        parentCategory.setCount(10);
        parentCategory.setStatus(1);
        parentCategory.setCreateTime(LocalDateTime.now());

        // 初始化子分类1
        childCategory1 = new Category();
        childCategory1.setId(2L);
        childCategory1.setName("压缩机");
        childCategory1.setSlug("compressor");
        childCategory1.setParentId(1L);
        childCategory1.setSortOrder(1);
        childCategory1.setCount(5);
        childCategory1.setStatus(1);
        childCategory1.setCreateTime(LocalDateTime.now());

        // 初始化子分类2
        childCategory2 = new Category();
        childCategory2.setId(3L);
        childCategory2.setName("冷凝器");
        childCategory2.setSlug("condenser");
        childCategory2.setParentId(1L);
        childCategory2.setSortOrder(2);
        childCategory2.setCount(5);
        childCategory2.setStatus(1);
        childCategory2.setCreateTime(LocalDateTime.now());
    }

    @Test
    @DisplayName("获取分类列表 - 成功（根分类）")
    void testListCategories_RootCategories() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(parentCategory));

        // Act
        List<CategoryVO> result = categoryService.listCategories(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("制冷配件");
        assertThat(result.get(0).getParentId()).isEqualTo(0L);

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类列表 - 成功（子分类）")
    void testListCategories_ChildCategories() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(childCategory1, childCategory2));

        // Act
        List<CategoryVO> result = categoryService.listCategories(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getName()).isEqualTo("压缩机");
        assertThat(result.get(0).getParentId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(3L);
        assertThat(result.get(1).getName()).isEqualTo("冷凝器");
        assertThat(result.get(1).getParentId()).isEqualTo(1L);

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类列表 - 空结果")
    void testListCategories_EmptyResult() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(new ArrayList<>());

        // Act
        List<CategoryVO> result = categoryService.listCategories(999L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类树 - 成功")
    void testGetCategoryTree_Success() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(parentCategory, childCategory1, childCategory2));

        // Act
        List<CategoryVO> result = categoryService.getCategoryTree();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("制冷配件");
        assertThat(result.get(0).getChildren()).isNotNull();
        assertThat(result.get(0).getChildren()).hasSize(2);
        assertThat(result.get(0).getChildren().get(0).getId()).isEqualTo(2L);
        assertThat(result.get(0).getChildren().get(0).getName()).isEqualTo("压缩机");
        assertThat(result.get(0).getChildren().get(1).getId()).isEqualTo(3L);
        assertThat(result.get(0).getChildren().get(1).getName()).isEqualTo("冷凝器");

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类树 - 空结果")
    void testGetCategoryTree_EmptyResult() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(new ArrayList<>());

        // Act
        List<CategoryVO> result = categoryService.getCategoryTree();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类树 - 只有根分类")
    void testGetCategoryTree_OnlyRootCategories() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(Arrays.asList(parentCategory));

        // Act
        List<CategoryVO> result = categoryService.getCategoryTree();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getChildren()).isNotNull();
        assertThat(result.get(0).getChildren()).isEmpty();

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类详情 - 成功")
    void testGetCategory_Success() {
        // Arrange
        when(categoryMapper.selectById(1L)).thenReturn(parentCategory);

        // Act
        CategoryVO result = categoryService.getCategory(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("制冷配件");
        assertThat(result.getSlug()).isEqualTo("cooling");
        assertThat(result.getParentId()).isEqualTo(0L);
        assertThat(result.getCount()).isEqualTo(10);

        verify(categoryMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取分类详情 - 分类不存在")
    void testGetCategory_NotFound() {
        // Arrange
        when(categoryMapper.selectById(999L)).thenReturn(null);

        // Act & Assert - 实现在找不到分类时抛出BusinessException
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(
            BusinessException.class,
            () -> categoryService.getCategory(999L)
        );
        assertThat(exception.getCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND.getCode());

        verify(categoryMapper).selectById(999L);
    }

    @Test
    @DisplayName("获取分类详情 - 无slug")
    void testGetCategory_NoSlug() {
        // Arrange
        parentCategory.setSlug(null);
        when(categoryMapper.selectById(1L)).thenReturn(parentCategory);

        // Act
        CategoryVO result = categoryService.getCategory(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getSlug()).isNull();

        verify(categoryMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取分类详情 - 无图标")
    void testGetCategory_NoIcon() {
        // Arrange
        parentCategory.setIcon(null);
        when(categoryMapper.selectById(1L)).thenReturn(parentCategory);

        // Act
        CategoryVO result = categoryService.getCategory(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIcon()).isNull();

        verify(categoryMapper).selectById(1L);
    }
}
