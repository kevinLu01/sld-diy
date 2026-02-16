package com.sld.backend.modules.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.category.dto.response.CategoryVO;
import com.sld.backend.modules.category.service.CategoryService;
import com.sld.backend.modules.product.entity.Category;
import com.sld.backend.modules.product.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> listCategories(Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (parentId == null) {
            wrapper.isNull(Category::getParentId);
        } else {
            wrapper.eq(Category::getParentId, parentId);
        }
        wrapper.eq(Category::getStatus, 1)
            .orderByAsc(Category::getSortOrder);

        return categoryMapper.selectList(wrapper).stream()
            .map(this::toVO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CategoryVO> getCategoryTree() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1)
            .orderByAsc(Category::getSortOrder);

        List<Category> allCategories = categoryMapper.selectList(wrapper);
        return buildTree(allCategories, 0L);
    }

    @Override
    public CategoryVO getCategory(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        CategoryVO vo = toVO(category);
        // 查找子分类
        List<Category> children = categoryMapper.selectList(
            new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSortOrder)
        );
        vo.setChildren(children.stream().map(this::toVO).collect(Collectors.toList()));
        return vo;
    }

    private List<CategoryVO> buildTree(List<Category> categories, Long parentId) {
        List<CategoryVO> tree = new ArrayList<>();
        for (Category category : categories) {
            if ((parentId == 0L && category.getParentId() == null) ||
                (category.getParentId() != null && category.getParentId().equals(parentId))) {
                CategoryVO vo = toVO(category);
                vo.setChildren(buildTree(categories, category.getId()));
                tree.add(vo);
            }
        }
        return tree;
    }

    private CategoryVO toVO(Category category) {
        return CategoryVO.builder()
            .id(category.getId())
            .name(category.getName())
            .slug(category.getSlug())
            .icon(category.getIcon())
            .count(category.getCount())
            .description(category.getDescription())
            .parentId(category.getParentId())
            .build();
    }
}
