import apiClient from './api';
import type { ApiResponse, CartItem, Order, PaginatedResponse, ShippingAddress } from '@/types';

export const cartService = {
  // 获取购物车
  getCart: () => {
    return apiClient.get<any, ApiResponse<CartItem[]>>('/cart');
  },

  // 添加到购物车
  addToCart: (productId: number, quantity: number = 1) => {
    return apiClient.post<any, ApiResponse<CartItem>>('/cart', { productId, quantity });
  },

  // 更新购物车商品数量
  updateCartItem: (itemId: number, quantity: number) => {
    return apiClient.put<any, ApiResponse<CartItem>>(`/cart/${itemId}`, { quantity });
  },

  // 删除购物车商品
  removeFromCart: (itemId: number) => {
    return apiClient.delete<any, ApiResponse<void>>(`/cart/${itemId}`);
  },

  // 清空购物车
  clearCart: () => {
    return apiClient.delete<any, ApiResponse<void>>('/cart');
  },
};

export const orderService = {
  // 创建订单
  createOrder: (data: {
    items: Array<{
      productId: number;
      quantity: number;
      price: number;
    }>;
    diyProjectId?: number;
    shippingAddress: ShippingAddress;
    notes?: string;
    needInstallation?: boolean;
  }) => {
    return apiClient.post<
      any,
      ApiResponse<{
        orderId: number;
        orderNo: string;
        totalAmount: number;
        shippingFee: number;
        installationFee?: number;
        finalAmount: number;
        paymentUrl?: string;
      }>
    >('/orders', data);
  },

  // 获取订单列表
  getOrders: (params?: { status?: string; page?: number; limit?: number }) => {
    return apiClient.get<any, ApiResponse<PaginatedResponse<Order>>>('/orders', { params });
  },

  // 获取订单详情
  getOrderDetail: (orderNo: string) => {
    return apiClient.get<any, ApiResponse<Order>>(`/orders/${orderNo}`);
  },

  // 取消订单
  cancelOrder: (orderNo: string, reason?: string) => {
    return apiClient.post<any, ApiResponse<void>>(`/orders/${orderNo}/cancel`, { reason });
  },
};
