import React from 'react';
import { Layout, Row, Col, Space } from 'antd';
import {
  PhoneOutlined,
  MailOutlined,
  EnvironmentOutlined,
  WechatOutlined,
  QqOutlined,
} from '@ant-design/icons';

const { Footer: AntFooter } = Layout;

const AppFooter: React.FC = () => {
  return (
    <AntFooter style={{ background: '#001529', color: '#fff', padding: '40px 50px' }}>
      <Row gutter={[32, 32]}>
        <Col xs={24} sm={12} md={6}>
          <h3 style={{ color: '#fff', marginBottom: 16 }}>关于我们</h3>
          <Space direction="vertical" size={8}>
            <div>生利达冷冻设备有限公司</div>
            <div style={{ color: '#8c8c8c', fontSize: 14 }}>
              专注于冷冻空调配件销售和技术服务
            </div>
          </Space>
        </Col>

        <Col xs={24} sm={12} md={6}>
          <h3 style={{ color: '#fff', marginBottom: 16 }}>快速链接</h3>
          <Space direction="vertical" size={8}>
            <a href="/products" style={{ color: '#8c8c8c' }}>产品中心</a>
            <a href="/diy" style={{ color: '#8c8c8c' }}>DIY配套</a>
            <a href="/solutions" style={{ color: '#8c8c8c' }}>解决方案</a>
            <a href="/knowledge" style={{ color: '#8c8c8c' }}>知识库</a>
          </Space>
        </Col>

        <Col xs={24} sm={12} md={6}>
          <h3 style={{ color: '#fff', marginBottom: 16 }}>联系我们</h3>
          <Space direction="vertical" size={12}>
            <Space>
              <PhoneOutlined />
              <span style={{ color: '#8c8c8c' }}>400-123-4567</span>
            </Space>
            <Space>
              <MailOutlined />
              <span style={{ color: '#8c8c8c' }}>service@sld-mall.com</span>
            </Space>
            <Space>
              <EnvironmentOutlined />
              <span style={{ color: '#8c8c8c' }}>广东省东莞市虎门镇</span>
            </Space>
          </Space>
        </Col>

        <Col xs={24} sm={12} md={6}>
          <h3 style={{ color: '#fff', marginBottom: 16 }}>关注我们</h3>
          <Space size={16}>
            <WechatOutlined style={{ fontSize: 24, color: '#52c41a', cursor: 'pointer' }} />
            <QqOutlined style={{ fontSize: 24, color: '#1890ff', cursor: 'pointer' }} />
          </Space>
          <div style={{ marginTop: 16, color: '#8c8c8c', fontSize: 12 }}>
            扫码关注获取最新优惠
          </div>
        </Col>
      </Row>

      <div
        style={{
          marginTop: 32,
          paddingTop: 24,
          borderTop: '1px solid #303030',
          textAlign: 'center',
          color: '#8c8c8c',
          fontSize: 14,
        }}
      >
        <div>© 2024 生利达冷冻设备有限公司 版权所有</div>
        <div style={{ marginTop: 8 }}>
          <a href="#" style={{ color: '#8c8c8c', marginRight: 16 }}>隐私政策</a>
          <a href="#" style={{ color: '#8c8c8c', marginRight: 16 }}>服务条款</a>
          <a href="#" style={{ color: '#8c8c8c' }}>粤ICP备XXXXXXXX号</a>
        </div>
      </div>
    </AntFooter>
  );
};

export default AppFooter;
