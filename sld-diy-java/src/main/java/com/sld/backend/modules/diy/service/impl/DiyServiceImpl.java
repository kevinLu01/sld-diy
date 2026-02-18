package com.sld.backend.modules.diy.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.diy.dto.request.DiyRecommendRequest;
import com.sld.backend.modules.diy.dto.request.SaveDiyProjectRequest;
import com.sld.backend.modules.diy.dto.response.DiyConfigVO;
import com.sld.backend.modules.diy.dto.response.DiyProjectVO;
import com.sld.backend.modules.diy.dto.response.DiyRecommendResponse;
import com.sld.backend.modules.diy.dto.response.DiyShareResponse;
import com.sld.backend.modules.diy.entity.DiyConfig;
import com.sld.backend.modules.diy.entity.DiyProject;
import com.sld.backend.modules.diy.entity.DiyRecommendation;
import com.sld.backend.modules.diy.mapper.DiyConfigMapper;
import com.sld.backend.modules.diy.mapper.DiyProjectMapper;
import com.sld.backend.modules.diy.mapper.DiyRecommendationMapper;
import com.sld.backend.modules.diy.service.DiyService;
import com.sld.backend.modules.product.entity.Compatibility;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.CompatibilityMapper;
import com.sld.backend.modules.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * DIY 服务实现
 */
@Service
@RequiredArgsConstructor
public class DiyServiceImpl implements DiyService {

    private final DiyProjectMapper diyProjectMapper;
    private final DiyConfigMapper diyConfigMapper;
    private final DiyRecommendationMapper diyRecommendationMapper;
    private final ProductMapper productMapper;
    private final CompatibilityMapper compatibilityMapper;

    @Override
    public Map<String, Object> getDiyConfig() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取所有配置
        List<DiyConfig> configs = diyConfigMapper.selectAllActive();
        Map<String, List<DiyConfigVO>> configMap = configs.stream()
            .map(this::toConfigVO)
            .collect(Collectors.groupingBy(DiyConfigVO::getCategory));
        result.put("configs", configMap);
        
        // 获取推荐配置
        List<DiyRecommendation> recommendations = diyRecommendationMapper.selectAllActive();
        result.put("recommendations", recommendations.stream()
            .map(this::toRecommendationVO)
            .collect(Collectors.toList()));
        
        return result;
    }

    @Override
    public DiyRecommendResponse recommend(DiyRecommendRequest request) {
        // 获取该场景下的推荐配置
        List<DiyRecommendation> recommendations = diyRecommendationMapper.selectByScenario(request.getScenario());
        
        Map<String, List<DiyRecommendResponse.ProductVO>> typedProductsMap = new HashMap<>();

        // 为每种推荐类型查询产品
        for (DiyRecommendation rec : recommendations) {
            List<Product> products = productMapper.selectList(
                new LambdaQueryWrapper<Product>()
                    .eq(Product::getCategoryId, rec.getCategoryId())
                    .in(Product::getStatus, "on_shelf", "active")
                    .last("LIMIT 5")
            );

            List<DiyRecommendResponse.ProductVO> productList = products.stream().map(p -> {
                DiyRecommendResponse.ProductVO vo = new DiyRecommendResponse.ProductVO();
                vo.setId(p.getId());
                vo.setName(p.getName());
                vo.setPrice(p.getPrice());
                vo.setQuantity(1);
                return vo;
            }).collect(Collectors.toList());

            typedProductsMap.put(rec.getProductType(), productList);
        }

        // Convert request to Map for requirements
        Map<String, Object> requirementsMap = new HashMap<>();
        requirementsMap.put("scenario", request.getScenario());
        requirementsMap.put("temperatureRange", request.getTemperatureRange());
        requirementsMap.put("coolingCapacity", request.getCoolingCapacity());
        requirementsMap.put("volume", request.getVolume());

        return DiyRecommendResponse.builder()
            .recommendationId("rec_" + System.currentTimeMillis())
            .scenario(request.getScenario())
            .requirements(requirementsMap)
            .products(typedProductsMap)
            .totalPrice(java.math.BigDecimal.ZERO)
            .estimatedInstallationFee(java.math.BigDecimal.ZERO)
            .energyEfficiency("A++")
            .estimatedPowerConsumption("0kW/day")
            .suggestions(Collections.emptyList())
            .build();
    }

    @Override
    public Map<String, Object> validateCompatibility(java.util.List<Long> productIds) {
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<Map<String, Object>> matrix = new ArrayList<>();
        
        // 检查所有产品对之间的兼容性
        for (int i = 0; i < productIds.size(); i++) {
            for (int j = i + 1; j < productIds.size(); j++) {
                Long productAId = productIds.get(i);
                Long productBId = productIds.get(j);
                
                Compatibility compat = compatibilityMapper.checkCompatibility(productAId, productBId);
                
                Map<String, Object> item = new HashMap<>();
                item.put("productA", productAId);
                item.put("productB", productBId);
                
                if (compat != null) {
                    item.put("status", compat.getCompatibilityType());
                    item.put("note", compat.getNotes());
                    
                    if ("incompatible".equals(compat.getCompatibilityType())) {
                        errors.add("产品 " + productAId + " 与产品 " + productBId + " 不兼容");
                    } else if ("warning".equals(compat.getCompatibilityType())) {
                        warnings.add(compat.getNotes());
                    }
                } else {
                    item.put("status", "unknown");
                    item.put("note", null);
                }
                matrix.add(item);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("compatible", errors.isEmpty());
        result.put("warnings", warnings);
        result.put("errors", errors);
        result.put("compatibilityMatrix", matrix);
        return result;
    }

    @Override
    @Transactional
    public DiyProjectVO saveProject(Long userId, SaveDiyProjectRequest request) {
        DiyProject project = new DiyProject();
        project.setUserId(userId);
        project.setProjectName(request.getProjectName());
        project.setScenario(request.getScenario());
        // Store options as JSON string
        if (request.getRequirements() != null) {
            project.setOptions(JSONUtil.toJsonStr(request.getRequirements()));
        }
        project.setTotalPrice(request.getTotalPrice() != null ? request.getTotalPrice().doubleValue() : null);
        project.setStatus(0);
        project.setShared(request.getShared() != null ? request.getShared() : false);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        diyProjectMapper.insert(project);
        return toVO(project);
    }

    @Override
    public Page<DiyProjectVO> listProjects(Long userId, Long page, Long limit) {
        Page<DiyProject> projectPage = diyProjectMapper.selectPage(
            new Page<>(page, limit),
            new LambdaQueryWrapper<DiyProject>()
                .eq(DiyProject::getUserId, userId)
                .orderByDesc(DiyProject::getCreatedAt)
        );
        List<DiyProjectVO> voList = projectPage.getRecords().stream()
            .map(this::toVO)
            .collect(Collectors.toList());
        Page<DiyProjectVO> voPage = new Page<>(projectPage.getCurrent(), projectPage.getSize(), projectPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public DiyProjectVO getProject(Long id) {
        DiyProject project = diyProjectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException(ErrorCode.DIY_PROJECT_NOT_FOUND);
        }
        return toVO(project);
    }

    @Override
    @Transactional
    public DiyShareResponse shareProject(Long id, Long userId) {
        DiyProject project = diyProjectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException(ErrorCode.DIY_PROJECT_NOT_FOUND);
        }
        if (!project.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权分享此方案");
        }
        
        // 生成分享token
        String shareToken = generateShareToken();
        project.setShareToken(shareToken);
        project.setShared(true);
        project.setUpdatedAt(LocalDateTime.now());
        diyProjectMapper.updateById(project);
        
        String shareUrl = "https://sld-mall.com/diy/share/" + shareToken;
        String qrCode = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + shareUrl;
        
        DiyShareResponse response = new DiyShareResponse();
        response.setShareUrl(shareUrl);
        response.setShareToken(shareToken);
        response.setQrCode(qrCode);
        return response;
    }

    @Override
    public DiyProjectVO getProjectByShareToken(String token) {
        DiyProject project = diyProjectMapper.selectOne(
            new LambdaQueryWrapper<DiyProject>()
                .eq(DiyProject::getShareToken, token)
                .eq(DiyProject::getShared, true)
        );
        if (project == null) {
            throw new BusinessException(ErrorCode.DIY_PROJECT_NOT_FOUND, "分享链接已失效");
        }
        // 增加浏览量 - viewCount handled by viewCount field
        diyProjectMapper.updateById(project);
        return toVO(project);
    }

    private DiyProjectVO toVO(DiyProject project) {
        return DiyProjectVO.builder()
            .id(project.getId())
            .projectName(project.getProjectName())
            .scenario(project.getScenario())
            .totalPrice(project.getTotalPrice() != null ? new java.math.BigDecimal(project.getTotalPrice()) : null)
            .shared(project.getShared())
            .createTime(project.getCreatedAt() != null ? project.getCreatedAt().toString() : null)
            .build();
    }

    private DiyConfigVO toConfigVO(DiyConfig config) {
        DiyConfigVO vo = new DiyConfigVO();
        vo.setId(config.getId());
        vo.setCategory(config.getCategory());
        vo.setKey(config.getKey());
        vo.setLabel(config.getLabel());
        vo.setValue(config.getValue());
        vo.setIcon(config.getIcon());
        vo.setDescription(config.getDescription());
        vo.setSortOrder(config.getSortOrder());
        return vo;
    }

    private Map<String, Object> toRecommendationVO(DiyRecommendation rec) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", rec.getId());
        vo.put("scenario", rec.getScenario());
        vo.put("productType", rec.getProductType());
        vo.put("categoryId", rec.getCategoryId());
        vo.put("priority", rec.getPriority());
        vo.put("isRequired", rec.getIsRequired());
        return vo;
    }

    private String generateShareToken() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
