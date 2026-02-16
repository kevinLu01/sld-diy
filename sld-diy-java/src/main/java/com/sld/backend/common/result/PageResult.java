package com.sld.backend.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页响应结果
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应结果")
public class PageResult<T> {

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页码")
    private Long page;

    @Schema(description = "每页数量")
    private Long limit;

    @Schema(description = "数据列表")
    private List<T> items;

    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(
            page.getTotal(),
            page.getCurrent(),
            page.getSize(),
            page.getRecords()
        );
    }

    public static <T> PageResult<T> of(Long total, Long page, Long limit, List<T> items) {
        return new PageResult<>(total, page, limit, items);
    }
}
