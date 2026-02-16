package com.sld.backend.modules.diy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.diy.dto.request.DiyRecommendRequest;
import com.sld.backend.modules.diy.dto.request.SaveDiyProjectRequest;
import com.sld.backend.modules.diy.dto.response.DiyProjectVO;
import com.sld.backend.modules.diy.dto.response.DiyRecommendResponse;
import com.sld.backend.modules.diy.dto.response.DiyShareResponse;
import com.sld.backend.modules.diy.entity.DiyConfig;
import com.sld.backend.modules.diy.entity.DiyProject;
import com.sld.backend.modules.diy.entity.DiyRecommendation;
import com.sld.backend.modules.diy.mapper.DiyConfigMapper;
import com.sld.backend.modules.diy.mapper.DiyProjectMapper;
import com.sld.backend.modules.diy.mapper.DiyRecommendationMapper;
import com.sld.backend.modules.diy.service.impl.DiyServiceImpl;
import com.sld.backend.modules.product.entity.Compatibility;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.CompatibilityMapper;
import com.sld.backend.modules.product.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    private SaveDiyProjectRequest saveRequest;
    private DiyRecommendRequest recommendRequest;

    @BeforeEach
    void setUp() {
        testProject = new DiyProject();
        testProject.setId(1L);
        testProject.setUserId(1L);
        testProject.setProjectName("My DIY Project");
        testProject.setScenario("cold_storage");
        testProject.setTotalPrice(new BigDecimal("5000.00"));
        testProject.setStatus("0");
        testProject.setShared(false);
        testProject.setViewCount(0);
        testProject.setUsageCount(0);

        saveRequest = new SaveDiyProjectRequest();
        saveRequest.setProjectName("New DIY Project");
        saveRequest.setScenario("supermarket");
        Map<String, Object> requirements = new HashMap<>();
        requirements.put("temperatureRange", "-5~0");
        requirements.put("coolingCapacity", 50.0);
        requirements.put("capacityUnit", "kW");
        requirements.put("volume", 100.0);
        requirements.put("volumeUnit", "m3");
        saveRequest.setRequirements(requirements);
        saveRequest.setTotalPrice(new BigDecimal("8000.00"));

        recommendRequest = new DiyRecommendRequest();
        recommendRequest.setScenario("cold_storage");
        recommendRequest.setTemperatureRange("-18~-15");
        recommendRequest.setCoolingCapacity(new BigDecimal("100.0"));
        recommendRequest.setCapacityUnit("kW");
        recommendRequest.setVolume(new BigDecimal("500.0"));
        recommendRequest.setVolumeUnit("m3");
    }

    @Test
    @DisplayName("获取DIY配置 - 成功")
    void testGetDiyConfig_Success() {
        // Arrange
        List<DiyConfig> configs = new ArrayList<>();
        DiyConfig config = new DiyConfig();
        config.setId(1L);
        config.setCategory("scenario");
        config.setConfigKey("cold_storage");
        config.setLabel("Cold Storage");
        config.setIsActive(true);
        configs.add(config);

        List<DiyRecommendation> recommendations = new ArrayList<>();
        DiyRecommendation rec = new DiyRecommendation();
        rec.setId(1L);
        rec.setScenario("cold_storage");
        rec.setProductType("compressor");
        rec.setCategoryId(1L);
        rec.setIsActive(true);
        recommendations.add(rec);

        when(diyConfigMapper.selectAllActive()).thenReturn(configs);
        when(diyRecommendationMapper.selectAllActive()).thenReturn(recommendations);

        // Act
        Map<String, Object> result = diyService.getDiyConfig();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("configs")).isNotNull();
        assertThat(result.get("recommendations")).isNotNull();

        verify(diyConfigMapper).selectAllActive();
        verify(diyRecommendationMapper).selectAllActive();
    }

    @Test
    @DisplayName("智能推荐配件 - 成功")
    void testRecommend_Success() {
        // Arrange
        List<DiyRecommendation> recommendations = new ArrayList<>();
        DiyRecommendation rec = new DiyRecommendation();
        rec.setId(1L);
        rec.setScenario("cold_storage");
        rec.setProductType("compressor");
        rec.setCategoryId(1L);
        rec.setIsActive(true);
        recommendations.add(rec);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        product.setName("Compressor Model A");
        product.setSku("COMP001");
        product.setPrice(new BigDecimal("2000.00"));
        products.add(product);

        when(diyRecommendationMapper.selectByScenario("cold_storage")).thenReturn(recommendations);
        when(productMapper.selectList(any())).thenReturn(products);

        // Act
        DiyRecommendResponse result = diyService.recommend(recommendRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getScenario()).isEqualTo("cold_storage");

        verify(diyRecommendationMapper).selectByScenario("cold_storage");
    }

    @Test
    @DisplayName("验证配件兼容性 - 全部兼容")
    void testValidateCompatibility_AllCompatible() {
        // Arrange
        List<Long> productIds = Arrays.asList(1L, 2L, 3L);

        when(compatibilityMapper.checkCompatibility(anyLong(), anyLong())).thenReturn(null);

        // Act
        Map<String, Object> result = diyService.validateCompatibility(productIds);

        // Assert
        assertThat(result.get("compatible")).isEqualTo(true);
        assertThat((List<?>) result.get("errors")).isEmpty();
    }

    @Test
    @DisplayName("验证配件兼容性 - 存在不兼容")
    void testValidateCompatibility_Incompatible() {
        // Arrange
        List<Long> productIds = Arrays.asList(1L, 2L);

        Compatibility compat = new Compatibility();
        compat.setProductAId(1L);
        compat.setProductBId(2L);
        compat.setCompatibilityType("incompatible");

        when(compatibilityMapper.checkCompatibility(1L, 2L)).thenReturn(compat);

        // Act
        Map<String, Object> result = diyService.validateCompatibility(productIds);

        // Assert
        assertThat(result.get("compatible")).isEqualTo(false);
        assertThat((List<?>) result.get("errors")).isNotEmpty();
    }

    @Test
    @DisplayName("保存DIY方案 - 成功")
    void testSaveProject_Success() {
        // Arrange
        when(diyProjectMapper.insert(any(DiyProject.class))).thenAnswer(invocation -> {
            DiyProject project = invocation.getArgument(0);
            project.setId(1L);
            return 1;
        });

        // Act
        DiyProjectVO result = diyService.saveProject(1L, saveRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProjectName()).isEqualTo("New DIY Project");

        verify(diyProjectMapper).insert(any(DiyProject.class));
    }

    @Test
    @DisplayName("获取DIY方案列表 - 成功")
    void testListProjects_Success() {
        // Arrange
        List<DiyProject> projects = new ArrayList<>();
        projects.add(testProject);

        Page<DiyProject> projectPage = new Page<>(1, 10, 1);
        projectPage.setRecords(projects);

        when(diyProjectMapper.selectPage(any(Page.class), any())).thenReturn(projectPage);

        // Act
        Page<DiyProjectVO> result = diyService.listProjects(1L, 1L, 10L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getRecords()).hasSize(1);

        verify(diyProjectMapper).selectPage(any(Page.class), any());
    }

    @Test
    @DisplayName("获取DIY方案详情 - 成功")
    void testGetProject_Success() {
        // Arrange
        when(diyProjectMapper.selectById(1L)).thenReturn(testProject);

        // Act
        DiyProjectVO result = diyService.getProject(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getProjectName()).isEqualTo("My DIY Project");

        verify(diyProjectMapper).selectById(1L);
    }

    @Test
    @DisplayName("获取DIY方案详情 - 不存在")
    void testGetProject_NotFound() {
        // Arrange
        when(diyProjectMapper.selectById(1L)).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> diyService.getProject(1L));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.DIY_PROJECT_NOT_FOUND.getCode());
    }

    @Test
    @DisplayName("分享DIY方案 - 成功")
    void testShareProject_Success() {
        // Arrange
        when(diyProjectMapper.selectById(1L)).thenReturn(testProject);
        when(diyProjectMapper.updateById(any(DiyProject.class))).thenReturn(1);

        // Act
        DiyShareResponse result = diyService.shareProject(1L, 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getShareToken()).isNotNull();
        assertThat(result.getShareUrl()).contains("/diy/share/");

        verify(diyProjectMapper).selectById(1L);
        verify(diyProjectMapper).updateById(any(DiyProject.class));
    }

    @Test
    @DisplayName("分享DIY方案 - 无权分享")
    void testShareProject_Forbidden() {
        // Arrange
        testProject.setUserId(2L);
        when(diyProjectMapper.selectById(1L)).thenReturn(testProject);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> diyService.shareProject(1L, 1L));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.FORBIDDEN.getCode());
    }

    @Test
    @DisplayName("通过分享Token获取方案 - 成功")
    void testGetProjectByShareToken_Success() {
        // Arrange
        testProject.setShared(true);
        testProject.setShareToken("abc123");
        when(diyProjectMapper.selectOne(any())).thenReturn(testProject);
        when(diyProjectMapper.updateById(any(DiyProject.class))).thenReturn(1);

        // Act
        DiyProjectVO result = diyService.getProjectByShareToken("abc123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(diyProjectMapper).selectOne(any());
        verify(diyProjectMapper).updateById(any(DiyProject.class));
    }

    @Test
    @DisplayName("通过分享Token获取方案 - 链接已失效")
    void testGetProjectByShareToken_Invalid() {
        // Arrange
        when(diyProjectMapper.selectOne(any())).thenReturn(null);

        // Act & Assert
        BusinessException exception = org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
            () -> diyService.getProjectByShareToken("invalid"));

        assertThat(exception.getCode()).isEqualTo(ErrorCode.DIY_PROJECT_NOT_FOUND.getCode());
    }
}
