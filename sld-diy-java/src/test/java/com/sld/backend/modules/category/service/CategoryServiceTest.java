package com.sld.backend.modules.category.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.modules.product.entity.Category;
import com.sld.backend.modules.product.mapper.CategoryMapper;
import com.sld.backend.modules.category.service.impl.CategoryServiceImpl;
import com.sld.backend.modules.category.dto.response.CategoryVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        // 初始化父分类 - 使用正确的字段名
        parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("制冷设备");
        parentCategory.setParentId(0L);
        parentCategory.setIsActive(true);
        parentCategory.setSortOrder(1);
        parentCategory.setCreatedAt(LocalDateTime.now());

        // 初始化子分类1 - 使用正确的字段名
        childCategory1 = new Category();
        childCategory1.setId(2L);
        childCategory1.setName("压缩机");
        childCategory1.setParentId(1L);
        childCategory1.setIsActive(true);
        childCategory1.setSortOrder(1);
        childCategory1.setCreatedAt(LocalDateTime.now());

        // 初始化子分类2 - 使用正确的字段名
        childCategory2 = new Category();
        childCategory2.setId(3L);
        childCategory2.setName("冷凝器");
        childCategory2.setParentId(1L);
        childCategory2.setIsActive(true);
        childCategory2.setSortOrder(2);
        childCategory2.setCreatedAt(LocalDateTime.now());
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
        assertThat(result).hasSize(1); // 1个父分类
        assertThat(result.get(0).getName()).isEqualTo("制冷设备");
        assertThat(result.get(0).getChildren()).hasSize(2); // 2个子分类

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取分类树 - 空结果")
    void testGetCategoryTree_EmptyResult() {
        // Arrange
        when(categoryMapper.selectList(any(LambdaQueryWrapper.class)))
            .thenReturn(List.of());

        // Act
        List<CategoryVO> result = categoryService.getCategoryTree();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(categoryMapper).selectList(any(LambdaQueryWrapper.class));
    }
}