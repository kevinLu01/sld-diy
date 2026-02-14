import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Carousel, Card, Row, Col, Button, Typography, Space, Statistic } from 'antd';
import {
  ThunderboltOutlined,
  SafetyOutlined,
  CustomerServiceOutlined,
  RocketOutlined,
  AppstoreOutlined,
  ToolOutlined,
  SolutionOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { productService } from '@/services/product';

const { Title, Paragraph } = Typography;

const HomePage: React.FC = () => {
  const navigate = useNavigate();

  useQuery({
    queryKey: ['categories'],
    queryFn: () => productService.getCategories(),
  });

  const { data: hotProducts } = useQuery({
    queryKey: ['products', 'hot'],
    queryFn: () => productService.getProducts({ sort: 'sales', limit: 8 }),
  });

  const banners = [
    {
      title: '智能DIY配套系统',
      description: '一站式配件选型，让制冷系统搭建更简单',
      image: 'https://via.placeholder.com/1200x400/1890ff/ffffff?text=DIY智能配套',
      action: () => navigate('/diy'),
    },
    {
      title: '专业解决方案',
      description: '20年行业经验，为您提供定制化解决方案',
      image: 'https://via.placeholder.com/1200x400/52c41a/ffffff?text=专业解决方案',
      action: () => navigate('/solutions'),
    },
    {
      title: '优质配件供应',
      description: '原厂正品，质量保证，全国包邮',
      image: 'https://via.placeholder.com/1200x400/fa8c16/ffffff?text=优质配件',
      action: () => navigate('/products'),
    },
  ];

  const features = [
    {
      icon: <ThunderboltOutlined style={{ fontSize: 48, color: '#1890ff' }} />,
      title: '极速发货',
      description: '现货库存，当天下单，次日达',
    },
    {
      icon: <SafetyOutlined style={{ fontSize: 48, color: '#52c41a' }} />,
      title: '品质保证',
      description: '原厂正品，假一赔十',
    },
    {
      icon: <CustomerServiceOutlined style={{ fontSize: 48, color: '#fa8c16' }} />,
      title: '专业服务',
      description: '7×24小时技术支持',
    },
    {
      icon: <RocketOutlined style={{ fontSize: 48, color: '#eb2f96' }} />,
      title: '智能推荐',
      description: 'AI智能配套，精准匹配',
    },
  ];

  return (
    <div>
      {/* Banner轮播 */}
      <Carousel autoplay>
        {banners.map((banner, index) => (
          <div key={index}>
            <div
              style={{
                height: 400,
                background: `url(${banner.image}) center/cover`,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                color: '#fff',
              }}
            >
              <div style={{ textAlign: 'center' }}>
                <Title level={1} style={{ color: '#fff', fontSize: 48, marginBottom: 16 }}>
                  {banner.title}
                </Title>
                <Paragraph style={{ fontSize: 20, color: '#fff', marginBottom: 32 }}>
                  {banner.description}
                </Paragraph>
                <Button type="primary" size="large" onClick={banner.action}>
                  立即体验
                </Button>
              </div>
            </div>
          </div>
        ))}
      </Carousel>

      {/* 快速入口 */}
      <div style={{ maxWidth: 1200, margin: '60px auto', padding: '0 20px' }}>
        <Title level={2} style={{ textAlign: 'center', marginBottom: 40 }}>
          快速入口
        </Title>
        <Row gutter={[24, 24]}>
          <Col xs={24} sm={12} md={8}>
            <Card
              hoverable
              onClick={() => navigate('/products')}
              style={{ textAlign: 'center', height: '100%' }}
            >
              <AppstoreOutlined style={{ fontSize: 64, color: '#1890ff', marginBottom: 16 }} />
              <Title level={3}>产品中心</Title>
              <Paragraph>浏览全系列冷冻空调配件</Paragraph>
            </Card>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <Card
              hoverable
              onClick={() => navigate('/diy')}
              style={{ textAlign: 'center', height: '100%' }}
            >
              <ToolOutlined style={{ fontSize: 64, color: '#52c41a', marginBottom: 16 }} />
              <Title level={3}>DIY配套</Title>
              <Paragraph>智能配件推荐，一键生成方案</Paragraph>
            </Card>
          </Col>
          <Col xs={24} sm={12} md={8}>
            <Card
              hoverable
              onClick={() => navigate('/solutions')}
              style={{ textAlign: 'center', height: '100%' }}
            >
              <SolutionOutlined style={{ fontSize: 64, color: '#fa8c16', marginBottom: 16 }} />
              <Title level={3}>解决方案</Title>
              <Paragraph>查看行业成熟解决方案</Paragraph>
            </Card>
          </Col>
        </Row>
      </div>

      {/* 核心优势 */}
      <div style={{ background: '#f5f5f5', padding: '60px 0' }}>
        <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
          <Title level={2} style={{ textAlign: 'center', marginBottom: 40 }}>
            我们的优势
          </Title>
          <Row gutter={[32, 32]}>
            {features.map((feature, index) => (
              <Col key={index} xs={24} sm={12} md={6}>
                <Card style={{ textAlign: 'center', height: '100%' }}>
                  <div style={{ marginBottom: 16 }}>{feature.icon}</div>
                  <Title level={4}>{feature.title}</Title>
                  <Paragraph style={{ color: '#666' }}>{feature.description}</Paragraph>
                </Card>
              </Col>
            ))}
          </Row>
        </div>
      </div>

      {/* 热销产品 */}
      <div style={{ maxWidth: 1200, margin: '60px auto', padding: '0 20px' }}>
        <Title level={2} style={{ textAlign: 'center', marginBottom: 40 }}>
          热销产品
        </Title>
        <Row gutter={[24, 24]}>
          {hotProducts?.data?.items?.slice(0, 8).map((product) => (
            <Col key={product.id} xs={24} sm={12} md={6}>
              <Card
                hoverable
                cover={
                  <img
                    alt={product.name}
                    src={product.images?.[0] || 'https://via.placeholder.com/300x200'}
                    style={{ height: 200, objectFit: 'cover' }}
                  />
                }
                onClick={() => navigate(`/products/${product.id}`)}
              >
                <Card.Meta
                  title={product.name}
                  description={
                    <Space direction="vertical" style={{ width: '100%' }}>
                      <div style={{ color: '#ff4d4f', fontSize: 20, fontWeight: 'bold' }}>
                        ¥{product.price.toFixed(2)}
                      </div>
                      <div style={{ color: '#999', fontSize: 12 }}>
                        销量 {product.salesCount}
                      </div>
                    </Space>
                  }
                />
              </Card>
            </Col>
          ))}
        </Row>
        <div style={{ textAlign: 'center', marginTop: 40 }}>
          <Button type="primary" size="large" onClick={() => navigate('/products')}>
            查看更多产品
          </Button>
        </div>
      </div>

      {/* 数据统计 */}
      <div style={{ background: '#1890ff', color: '#fff', padding: '60px 0' }}>
        <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
          <Row gutter={[32, 32]}>
            <Col xs={24} sm={12} md={6}>
              <Statistic
                title={<span style={{ color: '#fff' }}>产品种类</span>}
                value={2000}
                suffix="+"
                valueStyle={{ color: '#fff' }}
              />
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Statistic
                title={<span style={{ color: '#fff' }}>服务客户</span>}
                value={5000}
                suffix="+"
                valueStyle={{ color: '#fff' }}
              />
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Statistic
                title={<span style={{ color: '#fff' }}>解决方案</span>}
                value={200}
                suffix="+"
                valueStyle={{ color: '#fff' }}
              />
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Statistic
                title={<span style={{ color: '#fff' }}>行业经验</span>}
                value={20}
                suffix="年"
                valueStyle={{ color: '#fff' }}
              />
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
