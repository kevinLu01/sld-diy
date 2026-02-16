package com.sld.backend.modules.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sld.backend.modules.knowledge.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识文章 Mapper
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
