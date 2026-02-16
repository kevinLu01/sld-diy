package com.sld.backend.modules.solution.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 解决方案案例VO
 */
@Data
public class SolutionCaseVO {

    private Long id;
    private String projectName;
    private String clientName;
    private String location;
    private LocalDate completionDate;
    private String description;
    private List<String> images;
    private String results;
}
