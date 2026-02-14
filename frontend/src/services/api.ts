import axios from 'axios';
import { message } from 'antd';

const apiClient = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    const { data } = response;
    if (data.code === 200) {
      return data;
    } else {
      message.error(data.message || '请求失败');
      return Promise.reject(data);
    }
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response;
      
      switch (status) {
        case 401:
          message.error('未授权，请先登录');
          localStorage.removeItem('token');
          window.location.href = '/login';
          break;
        case 403:
          message.error('没有权限访问');
          break;
        case 404:
          message.error('请求的资源不存在');
          break;
        case 500:
          message.error('服务器错误，请稍后重试');
          break;
        default:
          message.error(data?.message || '请求失败');
      }
    } else if (error.request) {
      message.error('网络错误，请检查网络连接');
    } else {
      message.error('请求配置错误');
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;
