package com.sld.backend.modules.knowledge.controller;

import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.result.Result;
import com.sld.backend.modules.knowledge.dto.response.ArticleVO;
import com.sld.backend.modules.knowledge.service.KnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 知识库控制器
 */
@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
@Tag(name = "知识库管理", description = "知识库相关接口")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @GetMapping
    @Operation(summary = "搜索知识库")
    public Result<PageResult<ArticleVO>> searchArticles(
        @Parameter(description = "搜索关键词") @RequestParam(required = false) String q,
        @Parameter(description = "分类") @RequestParam(required = false) String category,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "10") Long limit
    ) {
        return Result.success(knowledgeService.searchArticles(q, category, page, limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public Result<ArticleVO> getArticle(@PathVariable Long id) {
        return Result.success(knowledgeService.getArticle(id));
    }

    @PostMapping("/{id}/helpful")
    @Operation(summary = "标记文章有用")
    public Result<Void> markHelpful(@PathVariable Long id) {
        knowledgeService.markHelpful(id);
        return Result.success();
    }
}
