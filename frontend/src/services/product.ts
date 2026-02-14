import apiClient from './api';
import type { ApiResponse, PaginatedResponse, Product, Category } from '@/types';

export const productService = {
  // 获取产品分类
  getCategories: () => {
    return apiClient.get<any, ApiResponse<Category[]>>('/categories');
  },

  // 获取产品列表
  getProducts: (params: {
    category?: string;
    page?: number;
    limit?: number;
    sort?: string;
    brand?: string;
    priceMin?: number;
    priceMax?: number;
    search?: string;
  }) => {
    return apiClient.get<any, ApiResponse<PaginatedResponse<Product>>>('/products', { params });
  },

  // 获取产品详情
  getProductDetail: (id: number) => {
    return apiClient.get<any, ApiResponse<Product>>(`/products/${id}`);
  },

  // 获取产品兼容性
  getProductCompatibility: (id: number) => {
    return apiClient.get<any, ApiResponse<any>>(`/products/${id}/compatibility`);
  },
};
