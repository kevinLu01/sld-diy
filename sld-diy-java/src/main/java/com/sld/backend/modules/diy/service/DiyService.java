package com.sld.backend.modules.diy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.diy.dto.request.DiyRecommendRequest;
import com.sld.backend.modules.diy.dto.request.SaveDiyProjectRequest;
import com.sld.backend.modules.diy.dto.request.ShareDiyProjectRequest;
import com.sld.backend.modules.diy.dto.response.DiyProjectVO;
import com.sld.backend.modules.diy.dto.response.DiyRecommendResponse;
import com.sld.backend.modules.diy.dto.response.DiyShareResponse;

import java.util.Map;

/**
 * DIY 服务接口
 */
public interface DiyService {

    /**
     * 获取DIY配置
     */
    Map<String, Object> getDiyConfig();

    /**
     * 获取场景蓝图（场景 -> 组件类型 -> 规格选项）
     */
    Map<String, Object> getSceneBlueprints(String sceneCode);

    /**
     * 智能推荐配件
     */
    DiyRecommendResponse recommend(DiyRecommendRequest request);

    /**
     * 验证配件兼容性
     */
    Map<String, Object> validateCompatibility(java.util.List<Long> productIds);

    /**
     * 保存DIY方案
     */
    DiyProjectVO saveProject(Long userId, SaveDiyProjectRequest request);

    /**
     * 获取用户DIY方案列表
     */
    Page<DiyProjectVO> listProjects(Long userId, Long page, Long limit);

    /**
     * 获取DIY方案详情
     */
    DiyProjectVO getProject(Long id);

    /**
     * 分享DIY方案
     */
    DiyShareResponse shareProject(Long id, Long userId, ShareDiyProjectRequest request);

    /**
     * 通过分享Token获取DIY方案
     */
    DiyProjectVO getProjectByShareToken(String token);
}
