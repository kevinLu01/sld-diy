import apiClient from './api';
import type { ApiResponse, User } from '@/types';

export const authService = {
  // 用户登录
  login: (data: { account: string; password: string }) => {
    return apiClient.post<any, ApiResponse<{ user: User; token: string }>>('/auth/login', data);
  },

  // 用户注册
  register: (data: {
    username: string;
    email: string;
    password: string;
    phone?: string;
    userType: 'personal' | 'business';
  }) => {
    return apiClient.post<any, ApiResponse<{ userId: number; username: string; token: string }>>(
      '/auth/register',
      data
    );
  },

  // 获取当前用户信息
  getCurrentUser: () => {
    return apiClient.get<any, ApiResponse<User>>('/users/profile');
  },

  // 退出登录
  logout: () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
  },
};
