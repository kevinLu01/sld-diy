package com.sld.backend.modules.knowledge.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.modules.knowledge.dto.response.ArticleVO;
import com.sld.backend.modules.knowledge.entity.Article;
import com.sld.backend.modules.knowledge.mapper.ArticleMapper;
import com.sld.backend.modules.knowledge.service.KnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库服务实现
 */
@Service
@RequiredArgsConstructor
public class KnowledgeServiceImpl implements KnowledgeService {

    private final ArticleMapper articleMapper;

    @Override
    public PageResult<ArticleVO> searchArticles(String q, String category, Long page, Long limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        // 只查询已发布的文章
        wrapper.eq(Article::getPublishStatus, 1);

        if (q != null && !q.isEmpty()) {
            wrapper.and(w -> w.like(Article::getTitle, q)
                .or()
                .like(Article::getContent, q));
        }
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Article::getCategory, category);
        }
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = articleMapper.selectPage(new Page<>(page, limit), wrapper);
        List<ArticleVO> voList = articlePage.getRecords().stream()
            .map(this::toVO)
            .collect(Collectors.toList());

        return PageResult.of(articlePage.getTotal(), articlePage.getCurrent(), articlePage.getSize(), voList);
    }

    @Override
    public ArticleVO getArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }
        // 增加浏览量
        article.setViewCount(article.getViewCount() != null ? article.getViewCount() + 1 : 1);
        articleMapper.updateById(article);
        return toVO(article);
    }

    @Override
    public void markHelpful(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }
        article.setHelpfulCount(article.getHelpfulCount() != null ? article.getHelpfulCount() + 1 : 1);
        articleMapper.updateById(article);
    }

    private ArticleVO toVO(Article article) {
        List<String> tags = Collections.emptyList();
        if (article.getTags() != null && !article.getTags().isEmpty()) {
            try {
                tags = JSONUtil.toList(article.getTags(), String.class);
            } catch (Exception e) {
                // ignore parse errors
            }
        }

        return ArticleVO.builder()
            .id(article.getId())
            .title(article.getTitle())
            .summary(article.getSummary())
            .content(article.getContent())
            .coverImage(article.getCoverImage())
            .category(article.getCategory())
            .tags(tags)
            .author(article.getAuthor())
            .viewCount(article.getViewCount())
            .helpfulCount(article.getHelpfulCount())
            .publishTime(article.getPublishTime())
            .createTime(article.getCreateTime())
            .build();
    }
}

