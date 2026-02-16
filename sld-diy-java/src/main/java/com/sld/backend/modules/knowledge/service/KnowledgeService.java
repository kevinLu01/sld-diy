package com.sld.backend.modules.knowledge.service;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.knowledge.dto.response.ArticleVO;

/**
 * 知识库服务接口
 */
public interface KnowledgeService {

    /**
     * 搜索文章
     */
    PageResult<ArticleVO> searchArticles(String q, String category, Long page, Long limit);

    /**
     * 获取文章详情
     */
    ArticleVO getArticle(Long id);

    /**
     * 标记文章有用
     */
    void markHelpful(Long id);
}
