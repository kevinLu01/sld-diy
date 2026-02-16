package com.sld.backend.modules.search.controller;

import com.sld.backend.common.result.Result;
import com.sld.backend.modules.search.dto.SearchResultDTO;
import com.sld.backend.modules.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 搜索控制器
 */
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "搜索管理", description = "搜索相关接口")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    @Operation(summary = "全局搜索")
    public Result<SearchResultDTO> search(
        @Parameter(description = "搜索关键词") @RequestParam String q,
        @Parameter(description = "搜索类型：products,solutions,articles") @RequestParam(required = false) String type,
        @RequestParam(defaultValue = "1") Long page,
        @RequestParam(defaultValue = "20") Long limit
    ) {
        return Result.success(searchService.search(q, type, page, limit));
    }

    @GetMapping("/suggestions")
    @Operation(summary = "搜索建议")
    public Result<List<String>> getSuggestions(
        @Parameter(description = "搜索关键词") @RequestParam String q
    ) {
        return Result.success(searchService.getSuggestions(q));
    }
}
