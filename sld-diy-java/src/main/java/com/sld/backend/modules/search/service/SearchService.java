package com.sld.backend.modules.search.service;

import com.sld.backend.modules.search.dto.SearchResultDTO;

import java.util.List;

/**
 * 搜索服务接口
 */
public interface SearchService {

    /**
     * 全局搜索
     */
    SearchResultDTO search(String keyword, String type, Long page, Long limit);

    /**
     * 获取搜索建议
     */
    List<String> getSuggestions(String keyword);
}
