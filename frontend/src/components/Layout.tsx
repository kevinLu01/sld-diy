import React from 'react';
import { Layout } from 'antd';
import { Outlet } from 'react-router-dom';
import AppHeader from './Header';
import AppFooter from './Footer';

const { Content } = Layout;

const MainLayout: React.FC = () => {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      <AppHeader />
      <Content style={{ marginTop: 0 }}>
        <Outlet />
      </Content>
      <AppFooter />
    </Layout>
  );
};

export default MainLayout;
