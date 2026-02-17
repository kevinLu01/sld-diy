import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Form, Input, Button, Card, Tabs, message, Space } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined, PhoneOutlined } from '@ant-design/icons';
import { useUserStore } from '@/store/user';
import { authService } from '@/services/auth';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { setUser, setToken } = useUserStore();
  const [loading, setLoading] = useState(false);

  const handleLogin = async (values: { account: string; password: string }) => {
    setLoading(true);
    console.log('🔍 Login Form Values:', values); // 调试日志
    console.log('🔍 Account:', values.account); // 调试日志
    console.log('🔍 Password:', values.password); // 调试日志
    try {
      const response = await authService.login(values);
      const { user, token } = response.data;
      
      setUser(user);
      setToken(token);
      
      message.success('登录成功！');
      navigate('/');
    } catch (error: any) {
      console.error('❌ Login Error:', error); // 调试日志
      console.error('❌ Error Response:', error.response); // 调试日志
      message.error(error.response?.data?.message || '登录失败');
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (values: {
    username: string;
    email: string;
    password: string;
    phone?: string;
    userType: 'personal' | 'business';
  }) => {
    setLoading(true);
    try {
      const response = await authService.register(values);
      const { token } = response.data;
      
      setToken(token);
      message.success('注册成功！正在跳转...');
      
      // 获取用户信息
      const userResponse = await authService.getCurrentUser();
      setUser(userResponse.data);
      
      setTimeout(() => navigate('/'), 1000);
    } catch (error: any) {
      message.error(error.response?.data?.message || '注册失败');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        padding: '20px',
      }}
    >
      <Card
        style={{
          width: '100%',
          maxWidth: 450,
          boxShadow: '0 10px 40px rgba(0,0,0,0.2)',
        }}
      >
        <div style={{ textAlign: 'center', marginBottom: 24 }}>
          <Space direction="vertical" size={8}>
            <div style={{ fontSize: 32, color: '#1890ff' }}>❄️</div>
            <h1 style={{ fontSize: 28, fontWeight: 'bold', color: '#1890ff', margin: 0 }}>
              生利达
            </h1>
            <p style={{ color: '#666', margin: 0 }}>冷冻空调配件DIY商城</p>
          </Space>
        </div>

        <Tabs
          defaultActiveKey="login"
          centered
          items={[
            {
              key: 'login',
              label: '登录',
              children: (
                <Form onFinish={handleLogin} size="large" autoComplete="off">
                  <Form.Item
                    name="account"
                    rules={[
                      { required: true, message: '请输入邮箱或用户名' },
                    ]}
                  >
                    <Input prefix={<MailOutlined />} placeholder="邮箱或用户名" />
                  </Form.Item>

                  <Form.Item
                    name="password"
                    rules={[{ required: true, message: '请输入密码' }]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>

                  <Form.Item>
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                      <a href="#">忘记密码?</a>
                    </div>
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit" block loading={loading}>
                      登录
                    </Button>
                  </Form.Item>

                  <div style={{ textAlign: 'center' }}>
                    <Link to="/">返回首页</Link>
                  </div>
                </Form>
              ),
            },
            {
              key: 'register',
              label: '注册',
              children: (
                <Form onFinish={handleRegister} size="large" autoComplete="off">
                  <Form.Item
                    name="username"
                    rules={[
                      { required: true, message: '请输入用户名' },
                      { min: 3, message: '用户名至少3个字符' },
                    ]}
                  >
                    <Input prefix={<UserOutlined />} placeholder="用户名" />
                  </Form.Item>

                  <Form.Item
                    name="email"
                    rules={[
                      { required: true, message: '请输入邮箱' },
                      { type: 'email', message: '请输入有效的邮箱' },
                    ]}
                  >
                    <Input prefix={<MailOutlined />} placeholder="邮箱" />
                  </Form.Item>

                  <Form.Item
                    name="password"
                    rules={[
                      { required: true, message: '请输入密码' },
                      { min: 6, message: '密码至少6个字符' },
                    ]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>

                  <Form.Item
                    name="confirm"
                    dependencies={['password']}
                    rules={[
                      { required: true, message: '请确认密码' },
                      ({ getFieldValue }) => ({
                        validator(_, value) {
                          if (!value || getFieldValue('password') === value) {
                            return Promise.resolve();
                          }
                          return Promise.reject(new Error('两次密码输入不一致'));
                        },
                      }),
                    ]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="确认密码" />
                  </Form.Item>

                  <Form.Item name="phone">
                    <Input prefix={<PhoneOutlined />} placeholder="手机号(可选)" />
                  </Form.Item>

                  <Form.Item name="userType" initialValue="personal">
                    <div>
                      <div style={{ marginBottom: 8 }}>用户类型</div>
                      <Button.Group style={{ width: '100%' }}>
                        <Button style={{ width: '50%' }}>个人用户</Button>
                        <Button style={{ width: '50%' }}>企业用户</Button>
                      </Button.Group>
                    </div>
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit" block loading={loading}>
                      注册
                    </Button>
                  </Form.Item>

                  <div style={{ textAlign: 'center' }}>
                    <Link to="/">返回首页</Link>
                  </div>
                </Form>
              ),
            },
          ]}
        />
      </Card>
    </div>
  );
};

export default LoginPage;
