package com.sld.backend.modules.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.knowledge.dto.response.ArticleVO;
import com.sld.backend.modules.knowledge.entity.Article;
import com.sld.backend.modules.knowledge.mapper.ArticleMapper;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import com.sld.backend.modules.search.dto.SearchResultDTO;
import com.sld.backend.modules.search.service.SearchService;
import com.sld.backend.modules.solution.dto.response.SolutionVO;
import com.sld.backend.modules.solution.entity.Solution;
import com.sld.backend.modules.solution.mapper.SolutionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索服务实现
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductMapper productMapper;
    private final SolutionMapper solutionMapper;
    private final ArticleMapper articleMapper;

    @Override
    public SearchResultDTO search(String keyword, String type, Long page, Long limit) {
        SearchResultDTO result = new SearchResultDTO();

        // 搜索产品
        if (type == null || type.contains("products")) {
            LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
            productWrapper.like(Product::getName, keyword)
                          .or()
                          .like(Product::getSku, keyword)
                          .or()
                          .like(Product::getDescription, keyword);
            Page<Product> productPage = productMapper.selectPage(new Page<>(page, limit), productWrapper);
            List<ProductVO> productVOs = productPage.getRecords().stream()
                .map(this::convertToProductVO)
                .collect(Collectors.toList());
            // 这里简化处理，实际需要创建 PageResult
        }

        // 搜索解决方案
        if (type == null || type.contains("solutions")) {
            LambdaQueryWrapper<Solution> solutionWrapper = new LambdaQueryWrapper<>();
            solutionWrapper.like(Solution::getTitle, keyword)
                           .or()
                           .like(Solution::getDescription, keyword);
            Page<Solution> solutionPage = solutionMapper.selectPage(new Page<>(page, limit), solutionWrapper);
        }

        // 搜索文章
        if (type == null || type.contains("articles")) {
            LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
            articleWrapper.like(Article::getTitle, keyword)
                          .or()
                          .like(Article::getContent, keyword);
            Page<Article> articlePage = articleMapper.selectPage(new Page<>(page, limit), articleWrapper);
        }

        return result;
    }

    @Override
    public List<String> getSuggestions(String keyword) {
        List<String> suggestions = new ArrayList<>();
        
        // 从产品名称获取建议
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.like(Product::getName, keyword).last("LIMIT 5");
        List<Product> products = productMapper.selectList(productWrapper);
        suggestions.addAll(products.stream().map(Product::getName).collect(Collectors.toList()));

        return suggestions;
    }

    private ProductVO convertToProductVO(Product product) {
        ProductVO vo = new ProductVO();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setSku(product.getSku());
        vo.setPrice(product.getPrice());
        return vo;
    }
}
