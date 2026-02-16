package com.sld.backend.modules.search.dto;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.knowledge.dto.response.ArticleVO;
import com.sld.backend.modules.product.dto.response.ProductVO;
import com.sld.backend.modules.solution.dto.response.SolutionVO;
import lombok.Data;

/**
 * 搜索结果 DTO
 */
@Data
public class SearchResultDTO {

    private PageResult<ProductVO> products;
    private PageResult<SolutionVO> solutions;
    private PageResult<ArticleVO> articles;
}
