package com.sld.backend.modules.category.service;

import com.sld.backend.modules.category.dto.response.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 获取分类列表
     */
    List<CategoryVO> listCategories(Long parentId);

    /**
     * 获取分类树
     */
    List<CategoryVO> getCategoryTree();

    /**
     * 获取分类详情
     */
    CategoryVO getCategory(Long id);
}
