import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Layout, Menu, Input, Badge, Avatar, Dropdown, Space } from 'antd';
import {
  HomeOutlined,
  AppstoreOutlined,
  ToolOutlined,
  ShoppingCartOutlined,
  UserOutlined,
  LogoutOutlined,
  LoginOutlined,
  BulbOutlined,
  SolutionOutlined,
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { useUserStore } from '@/store/user';
import { useCartStore } from '@/store/cart';

const { Header: AntHeader } = Layout;
const { Search } = Input;

const AppHeader: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAuthenticated, logout } = useUserStore();
  const { totalQuantity } = useCartStore();

  const menuItems: MenuProps['items'] = [
    {
      key: 'home',
      icon: <HomeOutlined />,
      label: <Link to="/">首页</Link>,
    },
    {
      key: 'products',
      icon: <AppstoreOutlined />,
      label: <Link to="/products">产品中心</Link>,
    },
    {
      key: 'diy',
      icon: <ToolOutlined />,
      label: <Link to="/diy">DIY配套</Link>,
    },
    {
      key: 'solutions',
      icon: <SolutionOutlined />,
      label: <Link to="/solutions">解决方案</Link>,
    },
    {
      key: 'knowledge',
      icon: <BulbOutlined />,
      label: <Link to="/knowledge">知识库</Link>,
    },
  ];

  const userMenuItems: MenuProps['items'] = isAuthenticated
    ? [
        {
          key: 'profile',
          icon: <UserOutlined />,
          label: '个人中心',
          onClick: () => navigate('/user/profile'),
        },
        {
          key: 'orders',
          icon: <ShoppingCartOutlined />,
          label: '我的订单',
          onClick: () => navigate('/user/orders'),
        },
        {
          key: 'diy-projects',
          icon: <ToolOutlined />,
          label: '我的DIY方案',
          onClick: () => navigate('/user/diy-projects'),
        },
        {
          type: 'divider',
        },
        {
          key: 'logout',
          icon: <LogoutOutlined />,
          label: '退出登录',
          onClick: logout,
        },
      ]
    : [
        {
          key: 'login',
          icon: <LoginOutlined />,
          label: '登录',
          onClick: () => navigate('/login'),
        },
      ];

  const handleSearch = (value: string) => {
    if (value.trim()) {
      navigate(`/products?search=${encodeURIComponent(value)}`);
    }
  };

  return (
    <AntHeader style={{ background: '#fff', padding: '0 50px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', flex: 1 }}>
          <Link to="/" style={{ marginRight: 40 }}>
            <Space size={12}>
              <div style={{ fontSize: 24, color: '#1890ff' }}>❄️</div>
              <span style={{ fontSize: 20, fontWeight: 'bold', color: '#1890ff' }}>生利达</span>
            </Space>
          </Link>

          <Menu
            mode="horizontal"
            items={menuItems}
            style={{ border: 'none', flex: 1, minWidth: 400 }}
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: 20 }}>
          <Search
            placeholder="搜索产品..."
            onSearch={handleSearch}
            style={{ width: 250 }}
            allowClear
          />

          <Link to="/cart">
            <Badge count={totalQuantity} offset={[0, 5]}>
              <ShoppingCartOutlined style={{ fontSize: 24, color: '#1890ff', cursor: 'pointer' }} />
            </Badge>
          </Link>

          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
            <Space style={{ cursor: 'pointer' }}>
              <Avatar
                icon={<UserOutlined />}
                src={user?.avatar}
                style={{ backgroundColor: '#1890ff' }}
              />
              <span>{user?.username || '未登录'}</span>
            </Space>
          </Dropdown>
        </div>
      </div>
    </AntHeader>
  );
};

export default AppHeader;
