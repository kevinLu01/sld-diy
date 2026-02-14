import React, { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ConfigProvider, Spin } from 'antd';
import zhCN from 'antd/locale/zh_CN';
import MainLayout from './components/Layout';

// 懒加载页面组件
const HomePage = lazy(() => import('./pages/Home'));
const ProductsPage = lazy(() => import('./pages/Products'));
const ProductDetailPage = lazy(() => import('./pages/ProductDetail'));
const CartPage = lazy(() => import('./pages/Cart'));
const DIYToolPage = lazy(() => import('./pages/DIYTool'));
const OrdersPage = lazy(() => import('./pages/Orders'));
const LoginPage = lazy(() => import('./pages/Login'));

// 创建 React Query 客户端
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5分钟
    },
  },
});

// 加载中组件
const PageLoader = () => (
  <div style={{ textAlign: 'center', padding: '100px 0' }}>
    <Spin size="large" />
  </div>
);

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <ConfigProvider
        locale={zhCN}
        theme={{
          token: {
            colorPrimary: '#1890ff',
            borderRadius: 4,
          },
        }}
      >
        <BrowserRouter>
          <Suspense fallback={<PageLoader />}>
            <Routes>
              {/* 登录页面 */}
              <Route path="/login" element={<LoginPage />} />

              {/* 主布局页面 */}
              <Route path="/" element={<MainLayout />}>
                <Route index element={<HomePage />} />
                <Route path="products" element={<ProductsPage />} />
                <Route path="products/:id" element={<ProductDetailPage />} />
                <Route path="cart" element={<CartPage />} />
                <Route path="diy" element={<DIYToolPage />} />
                <Route path="user/orders" element={<OrdersPage />} />
                <Route
                  path="solutions"
                  element={
                    <div style={{ padding: 40, textAlign: 'center' }}>
                      <h2>解决方案页面开发中...</h2>
                    </div>
                  }
                />
                <Route
                  path="knowledge"
                  element={
                    <div style={{ padding: 40, textAlign: 'center' }}>
                      <h2>知识库页面开发中...</h2>
                    </div>
                  }
                />
                <Route
                  path="user/profile"
                  element={
                    <div style={{ padding: 40, textAlign: 'center' }}>
                      <h2>个人中心开发中...</h2>
                    </div>
                  }
                />
                <Route
                  path="*"
                  element={
                    <div style={{ padding: 40, textAlign: 'center' }}>
                      <h1>404</h1>
                      <p>页面未找到</p>
                    </div>
                  }
                />
              </Route>
            </Routes>
          </Suspense>
        </BrowserRouter>
      </ConfigProvider>
    </QueryClientProvider>
  );
};

export default App;
