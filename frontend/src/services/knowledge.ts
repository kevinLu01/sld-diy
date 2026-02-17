import apiClient from './api';
import type { ApiResponse, PaginatedResponse } from '@/types';

export interface KnowledgeArticleApi {
  id: number;
  title: string;
  summary?: string;
  content: string;
  coverImage?: string;
  category?: string;
  tags?: string[];
  author?: string;
  viewCount?: number;
  helpfulCount?: number;
  publishTime?: string;
  createTime?: string;
}

export const knowledgeService = {
  getArticles: (params?: { q?: string; category?: string; page?: number; limit?: number }) =>
    apiClient.get<any, ApiResponse<PaginatedResponse<KnowledgeArticleApi>>>('/knowledge', { params }),

  getArticleDetail: (id: number) =>
    apiClient.get<any, ApiResponse<KnowledgeArticleApi>>(`/knowledge/${id}`),

  markHelpful: (id: number) =>
    apiClient.post<any, ApiResponse<void>>(`/knowledge/${id}/helpful`),
};

