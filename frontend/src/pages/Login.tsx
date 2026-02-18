import React, { useEffect, useRef, useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import { Form, Input, Button, Card, Tabs, message, Space, Divider, Grid } from 'antd';
import { UserOutlined, LockOutlined, MailOutlined, PhoneOutlined, WechatOutlined } from '@ant-design/icons';
import { useUserStore } from '@/store/user';
import { authService } from '@/services/auth';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const screens = Grid.useBreakpoint();
  const isMobile = !screens.md;
  const { setUser, setToken } = useUserStore();
  const [loading, setLoading] = useState(false);
  const wechatCallbackHandled = useRef(false);

  const handleLogin = async (values: { account: string; password: string }) => {
    setLoading(true);
    try {
      const response = await authService.login(values);
      const { token } = response.data;
      setToken(token);

      // 登录接口仅返回最小用户信息，统一拉取 profile 保证前端用户态完整
      const profile = await authService.getCurrentUser();
      setUser(profile.data);
      
      message.success('登录成功！');
      navigate('/');
    } catch (error: any) {
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

  const handleWechatLogin = async () => {
    try {
      const response = await authService.getWechatAuthorizeUrl();
      const authorizeUrl = response.data.authorizeUrl;
      if (!authorizeUrl) {
        message.error('暂未获取到微信授权地址');
        return;
      }
      window.location.href = authorizeUrl;
    } catch (error: any) {
      message.error(error.response?.data?.message || '微信登录暂不可用');
    }
  };

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const code = params.get('code');
    const state = params.get('state');

    if (!code || !state || wechatCallbackHandled.current) {
      return;
    }

    wechatCallbackHandled.current = true;
    setLoading(true);
    authService
      .wechatLogin({ code, state })
      .then(async (response) => {
        setToken(response.data.token);
        const profile = await authService.getCurrentUser();
        setUser(profile.data);
        message.success('微信登录成功');
        navigate('/', { replace: true });
      })
      .catch((error: any) => {
        message.error(error.response?.data?.message || '微信登录失败');
      })
      .finally(() => {
        setLoading(false);
      });
  }, [location.search, navigate, setToken, setUser]);

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

                  <Divider plain style={{ margin: '12px 0 16px' }}>
                    其他登录方式
                  </Divider>

                  <Form.Item>
                    <Button
                      block
                      icon={<WechatOutlined />}
                      onClick={handleWechatLogin}
                      loading={loading}
                    >
                      微信登录
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
                      <Button.Group style={{ width: '100%', display: 'flex' }}>
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
        {isMobile && (
          <div style={{ marginTop: 10, textAlign: 'center', color: '#999', fontSize: 12 }}>
            建议使用微信或账号登录继续下单流程
          </div>
        )}
      </Card>
    </div>
  );
};

export default LoginPage;
