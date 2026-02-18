import apiClient from './api';
import type {
  ApiResponse,
  DiyProject,
  DiyRecommendation,
  CompatibilityCheck,
  PaginatedResponse,
} from '@/types';

export const diyService = {
  // 获取DIY配置
  getConfig: () => {
    return apiClient.get<any, ApiResponse<any>>('/diy/config');
  },

  // 智能推荐配件
  recommend: (data: {
    scenario: string;
    temperatureRange?: string;
    coolingCapacity?: number;
    capacityUnit?: string;
    volume?: number;
    volumeUnit?: string;
    ambientTemp?: string;
    options?: Record<string, boolean>;
  }) => {
    return apiClient.post<any, ApiResponse<DiyRecommendation>>('/diy/recommend', data);
  },

  // 验证配件兼容性
  validateCompatibility: (productIds: number[]) => {
    return apiClient.post<any, ApiResponse<CompatibilityCheck>>('/diy/validate-compatibility', productIds);
  },

  // 保存DIY项目
  saveProject: (data: {
    projectName: string;
    solutionId?: number;
    scenario?: string;
    temperatureRange?: string;
    coolingCapacity?: number;
    capacityUnit?: string;
    volume?: number;
    volumeUnit?: string;
    ambientTemp?: string;
    options?: Record<string, boolean>;
    selectedProducts: Array<{
      productId: number;
      quantity: number;
      notes?: string;
    }>;
  }) => {
    return apiClient.post<any, ApiResponse<DiyProject>>('/diy/projects', data);
  },

  // 获取用户的DIY项目列表
  getProjects: (params?: { page?: number; limit?: number }) => {
    return apiClient.get<any, ApiResponse<PaginatedResponse<DiyProject>>>('/diy/projects', {
      params,
    });
  },

  // 获取DIY项目详情
  getProjectDetail: (id: number) => {
    return apiClient.get<any, ApiResponse<DiyProject>>(`/diy/projects/${id}`);
  },

  // 分享DIY项目
  shareProject: (
    id: number,
    payload?: {
      shareMode?: 'public' | 'private_offer';
      discountRate?: number;
      discountAmount?: number;
      expiresAt?: string;
      privateNote?: string;
    }
  ) => {
    return apiClient.post<
      any,
      ApiResponse<{
        shareUrl: string;
        shareToken: string;
        qrCode: string;
        shareMode?: string;
        quotedTotalPrice?: number;
        discountRate?: number;
        discountAmount?: number;
        shareExpiresAt?: string;
      }>
    >(`/diy/projects/${id}/share`, payload || {});
  },

  // 获取解决方案列表
  getSolutions: (params?: { industry?: string; scenario?: string }) => {
    return apiClient.get<any, ApiResponse<any[]>>('/diy/solutions', { params });
  },
};
