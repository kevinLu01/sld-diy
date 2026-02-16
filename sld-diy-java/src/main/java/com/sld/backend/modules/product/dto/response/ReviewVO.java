package com.sld.backend.modules.product.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评价VO
 */
@Data
public class ReviewVO {

    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private Integer rating;
    private String content;
    private List<String> images;
    private Boolean isAnonymous;
    private LocalDateTime createTime;
}
