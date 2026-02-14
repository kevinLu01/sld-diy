import apiClient from './api';
import type { ApiResponse, Solution, PaginatedResponse } from '@/types';

export const solutionService = {
  // 获取解决方案列表
  getSolutions: (params?: { industry?: string; scenario?: string; page?: number; limit?: number }) => {
    return apiClient.get<any, ApiResponse<PaginatedResponse<Solution>>>('/solutions', { params });
  },

  // 获取解决方案详情
  getSolutionDetail: (id: number) => {
    return apiClient.get<any, ApiResponse<Solution>>(`/solutions/${id}`);
  },

  // 基于解决方案创建订单
  createOrderFromSolution: (solutionId: number, data: any) => {
    return apiClient.post<any, ApiResponse<any>>(`/solutions/${solutionId}/create-order`, data);
  },
};
