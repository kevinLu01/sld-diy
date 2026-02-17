package com.sld.backend.modules.diy.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.diy.dto.request.DiyRecommendRequest;
import com.sld.backend.modules.diy.dto.request.SaveDiyProjectRequest;
import com.sld.backend.modules.diy.dto.response.DiyProjectVO;
import com.sld.backend.modules.diy.dto.response.DiyRecommendResponse;
import com.sld.backend.modules.diy.entity.DiyConfig;
import com.sld.backend.modules.diy.entity.DiyProject;
import com.sld.backend.modules.diy.entity.DiyRecommendation;
import com.sld.backend.modules.diy.mapper.DiyConfigMapper;
import com.sld.backend.modules.diy.mapper.DiyProjectMapper;
import com.sld.backend.modules.diy.mapper.DiyRecommendationMapper;
import com.sld.backend.modules.product.mapper.CompatibilityMapper;
import com.sld.backend.modules.product.mapper.ProductMapper;
import com.sld.backend.modules.diy.service.impl.DiyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * DIY服务单元测试
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DIY服务测试")
class DiyServiceTest {

    @Mock
    private DiyProjectMapper diyProjectMapper;

    @Mock
    private DiyConfigMapper diyConfigMapper;

    @Mock
    private DiyRecommendationMapper diyRecommendationMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CompatibilityMapper compatibilityMapper;

    @InjectMocks
    private DiyServiceImpl diyService;

    private DiyProject testProject;
    private DiyConfig testConfig;

    @BeforeEach
    void setUp() {
        // 初始化测试DIY项目 - 使用正确的字段名
        testProject = new DiyProject();
        testProject.setId(1L);
        testProject.setUserId(1L);
        testProject.setProjectName("我的制冷方案");
        testProject.setScenario("cold_storage");
        testProject.setTotalPrice(5000.0);
        testProject.setStatus("draft");
        testProject.setShared(false);
        testProject.setCreatedAt(LocalDateTime.now());
        testProject.setUpdatedAt(LocalDateTime.now());

        // 初始化测试配置 - 使用正确的字段名
        testConfig = new DiyConfig();
        testConfig.setId(1L);
        testConfig.setCategory("temperature");
        testConfig.setKey("temp_range");
        testConfig.setLabel("温度范围");
        testConfig.setValue("-20~-10");
        testConfig.setSortOrder(1);
    }

    @Test
    @DisplayName("获取DIY配置 - 成功")
    void testGetDiyConfig_Success() {
        // Arrange
        when(diyConfigMapper.selectAllActive()).thenReturn(List.of(testConfig));
        when(diyRecommendationMapper.selectAllActive()).thenReturn(List.of());

        // Act
        Map<String, Object> result = diyService.getDiyConfig();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("configs");
        assertThat(result).containsKey("recommendations");

        verify(diyConfigMapper).selectAllActive();
        verify(diyRecommendationMapper).selectAllActive();
    }

    @Test
    @DisplayName("保存DIY项目 - 成功")
    void testSaveProject_Success() {
        // Arrange
        SaveDiyProjectRequest request = new SaveDiyProjectRequest();
        request.setProjectName("新方案");
        request.setScenario("cold_storage");
        request.setRequirements(new HashMap<>());
        request.setTotalPrice(BigDecimal.valueOf(3000));

        when(diyProjectMapper.insert(any(DiyProject.class))).thenAnswer(invocation -> {
            DiyProject project = invocation.getArgument(0);
            project.setId(1L);
            return 1;
        });

        // Act
        DiyProjectVO result = diyService.saveProject(1L, request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProjectName()).isEqualTo("新方案");

        verify(diyProjectMapper).insert(any(DiyProject.class));
    }

    @Test
    @DisplayName("获取DIY项目列表 - 成功")
    void testListProjects_Success() {
        // Arrange
        Page<DiyProject> projectPage = new Page<>(1, 10);
        projectPage.setRecords(List.of(testProject));
        projectPage.setTotal(1);

        when(diyProjectMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
            .thenReturn(projectPage);

        // Act
        Page<DiyProjectVO> result = diyService.listProjects(1L, 1L, 10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getRecords().get(0).getProjectName()).isEqualTo("我的制冷方案");

        verify(diyProjectMapper).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    @DisplayName("获取DIY项目详情 - 成功")
    void testGetProject_Success() {
        // Arrange
        when(diyProjectMapper.selectById(1L)).thenReturn(testProject);

        // Act
        DiyProjectVO result = diyService.getProject(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectName()).isEqualTo("我的制冷方案");

        verify(diyProjectMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取DIY项目详情 - 不存在")
    void testGetProject_NotFound() {
        // Arrange
        when(diyProjectMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = assertThrows(
            BusinessException.class,
            () -> diyService.getProject(999L)
        );

        assertThat(exception.getCode()).isEqualTo(ErrorCode.DIY_PROJECT_NOT_FOUND.getCode());

        verify(diyProjectMapper).selectById(999L);
    }

    @Test
    @DisplayName("推荐产品 - 成功")
    void testRecommend_Success() {
        // Arrange
        DiyRecommendRequest request = new DiyRecommendRequest();
        request.setScenario("cold_storage");
        request.setTemperatureRange("-20~-10");

        DiyRecommendation recommendation = new DiyRecommendation();
        recommendation.setId(1L);
        recommendation.setScenario("cold_storage");
        recommendation.setProductType("compressor");
        recommendation.setCategoryId(1L);
        recommendation.setPriority(1);
        recommendation.setIsRequired(true);

        when(diyRecommendationMapper.selectByScenario("cold_storage"))
            .thenReturn(List.of(recommendation));
        when(productMapper.selectList(any())).thenReturn(List.of());

        // Act
        DiyRecommendResponse result = diyService.recommend(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getScenario()).isEqualTo("cold_storage");

        verify(diyRecommendationMapper).selectByScenario("cold_storage");
    }
}
