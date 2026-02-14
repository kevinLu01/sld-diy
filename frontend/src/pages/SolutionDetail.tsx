import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Typography,
  Space,
  Button,
  Tag,
  Descriptions,
  Table,
  Tabs,
  Image,
  Breadcrumb,
  Statistic,
  Divider,
  message,
  Carousel,
  Empty,
  Spin,
} from 'antd';
import {
  HomeOutlined,
  SolutionOutlined,
  ShoppingCartOutlined,
  DownloadOutlined,
  CheckCircleOutlined,
  FireOutlined,
  EyeOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import type { Product } from '@/types';

const { Title, Paragraph, Text } = Typography;

// 临时mock数据服务
const getSolutionDetail = async (id: number) => {
  // 这里应该调用真实的API
  return {
    data: {
      id,
      title: '商超冷柜制冷系统解决方案',
      industry: 'retail',
      scenario: 'supermarket_freezer',
      description:
        '专为商超设计的冷柜制冷系统，采用节能环保的制冷技术，配备智能温控系统，确保食品安全的同时降低能耗30%。适用于各类商超的冷藏冷冻展示柜。',
      coverImage: '',
      images: [],
      temperatureRange: '-5°C ~ 0°C',
      capacityRange: '20-100m³',
      features: [
        '节能30%',
        '低噪音运行',
        '智能温控',
        '远程监控',
        '快速制冷',
        '环保制冷剂',
      ],
      totalPrice: 15680,
      viewCount: 5600,
      usageCount: 1200,
      products: [
        {
          productId: 1,
          quantity: 1,
          notes: '主压缩机',
          product: {
            id: 1,
            name: '松下 2P20S 涡旋压缩机',
            price: 3680,
            images: [],
            brand: { name: '松下' },
          },
        },
        {
          productId: 2,
          quantity: 2,
          notes: '冷凝器',
          product: {
            id: 2,
            name: '风冷翅片式冷凝器 FNH-60',
            price: 1580,
            images: [],
            brand: { name: '生利达' },
          },
        },
      ],
      technicalDocs: [
        { name: '系统原理图', url: '#' },
        { name: '安装指导手册', url: '#' },
        { name: '维护保养指南', url: '#' },
      ],
      cases: [
        {
          id: 1,
          projectName: '华润万家深圳门店',
          clientName: '华润万家',
          location: '深圳市',
          completionDate: '2024-03',
          images: [],
          results: '节能35%，温控精度±0.5°C，运行稳定',
        },
      ],
    },
  };
};

const SolutionDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data: solutionData, isLoading } = useQuery({
    queryKey: ['solution', id],
    queryFn: () => getSolutionDetail(Number(id)),
    enabled: !!id,
  });

  const solution = solutionData?.data;

  const handleUseSolution = () => {
    navigate('/diy', { state: { solutionId: solution?.id } });
  };

  const handleAddToCart = () => {
    message.success('方案已加入购物车');
    navigate('/cart');
  };

  if (isLoading) {
    return (
      <div style={{ textAlign: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!solution) {
    return (
      <div style={{ padding: 100 }}>
        <Empty description="方案未找到" />
      </div>
    );
  }

  const productColumns = [
    {
      title: '产品名称',
      dataIndex: 'product',
      key: 'name',
      render: (product: Product) => (
        <Space>
          <Image
            src={product.images?.[0] || 'https://via.placeholder.com/60x60'}
            width={60}
            height={60}
            style={{ borderRadius: 4 }}
            preview={false}
          />
          <div>
            <div style={{ fontWeight: 500 }}>{product.name}</div>
            {product.brand && (
              <Text type="secondary" style={{ fontSize: 12 }}>
                {product.brand.name}
              </Text>
            )}
          </div>
        </Space>
      ),
    },
    {
      title: '用途',
      dataIndex: 'notes',
      key: 'notes',
    },
    {
      title: '单价',
      dataIndex: 'product',
      key: 'price',
      render: (product: Product) => `¥${product.price.toFixed(2)}`,
    },
    {
      title: '数量',
      dataIndex: 'quantity',
      key: 'quantity',
    },
    {
      title: '小计',
      key: 'subtotal',
      render: (_: any, record: any) => (
        <Text strong style={{ color: '#ff4d4f' }}>
          ¥{(record.product.price * record.quantity).toFixed(2)}
        </Text>
      ),
    },
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        {/* 面包屑 */}
        <Breadcrumb
          style={{ marginBottom: 24 }}
          items={[
            { title: <HomeOutlined />, href: '/' },
            { title: <SolutionOutlined />, href: '/solutions' },
            { title: solution.title },
          ]}
        />

        {/* 方案概览 */}
        <Card>
          <Row gutter={[32, 32]}>
            <Col xs={24} md={12}>
              {solution.images && solution.images.length > 0 ? (
                <Carousel autoplay>
                  {solution.images.map((img: string, idx: number) => (
                    <div key={idx}>
                      <div
                        style={{
                          height: 400,
                          background: `url(${img}) center/cover`,
                        }}
                      />
                    </div>
                  ))}
                </Carousel>
              ) : (
                <div
                  style={{
                    height: 400,
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: '#fff',
                    fontSize: 32,
                    fontWeight: 'bold',
                    borderRadius: 8,
                  }}
                >
                  {solution.title}
                </div>
              )}
            </Col>

            <Col xs={24} md={12}>
              <Space direction="vertical" size={16} style={{ width: '100%' }}>
                <div>
                  <Title level={2}>{solution.title}</Title>
                  <Space size={8}>
                    <Tag color="blue">{solution.industry}</Tag>
                    <Tag>{solution.scenario}</Tag>
                  </Space>
                </div>

                <Paragraph>{solution.description}</Paragraph>

                <Row gutter={16}>
                  <Col span={8}>
                    <Statistic
                      title="浏览量"
                      value={solution.viewCount}
                      prefix={<EyeOutlined />}
                    />
                  </Col>
                  <Col span={8}>
                    <Statistic
                      title="使用次数"
                      value={solution.usageCount}
                      prefix={<FireOutlined />}
                    />
                  </Col>
                  <Col span={8}>
                    <Statistic
                      title="好评率"
                      value={98}
                      suffix="%"
                      prefix={<CheckCircleOutlined />}
                    />
                  </Col>
                </Row>

                <Divider />

                <Descriptions column={1}>
                  <Descriptions.Item label="温度范围">
                    {solution.temperatureRange}
                  </Descriptions.Item>
                  <Descriptions.Item label="适用容量">
                    {solution.capacityRange}
                  </Descriptions.Item>
                  <Descriptions.Item label="方案特点">
                    <Space wrap>
                      {solution.features.map((feature: string, idx: number) => (
                        <Tag key={idx} color="green" icon={<CheckCircleOutlined />}>
                          {feature}
                        </Tag>
                      ))}
                    </Space>
                  </Descriptions.Item>
                </Descriptions>

                <Divider />

                <div>
                  <Text type="secondary">方案总价：</Text>
                  <Title level={2} style={{ color: '#ff4d4f', display: 'inline', marginLeft: 8 }}>
                    ¥{solution.totalPrice.toFixed(2)}
                  </Title>
                </div>

                <Space size={16} style={{ width: '100%' }}>
                  <Button
                    type="primary"
                    size="large"
                    icon={<ShoppingCartOutlined />}
                    onClick={handleAddToCart}
                    style={{ flex: 1 }}
                  >
                    加入购物车
                  </Button>
                  <Button size="large" onClick={handleUseSolution} style={{ flex: 1 }}>
                    使用此方案
                  </Button>
                </Space>
              </Space>
            </Col>
          </Row>
        </Card>

        {/* 详细信息 */}
        <Card style={{ marginTop: 24 }}>
          <Tabs
            defaultActiveKey="products"
            items={[
              {
                key: 'products',
                label: '产品清单',
                children: (
                  <div>
                    <Table
                      dataSource={solution.products}
                      columns={productColumns}
                      rowKey="productId"
                      pagination={false}
                      summary={() => (
                        <Table.Summary fixed>
                          <Table.Summary.Row>
                            <Table.Summary.Cell index={0} colSpan={4}>
                              <Text strong>合计</Text>
                            </Table.Summary.Cell>
                            <Table.Summary.Cell index={1}>
                              <Text strong style={{ fontSize: 18, color: '#ff4d4f' }}>
                                ¥{solution.totalPrice.toFixed(2)}
                              </Text>
                            </Table.Summary.Cell>
                          </Table.Summary.Row>
                        </Table.Summary>
                      )}
                    />
                  </div>
                ),
              },
              {
                key: 'cases',
                label: `成功案例 (${solution.cases.length})`,
                children: (
                  <div>
                    {solution.cases.map((c: any) => (
                      <Card key={c.id} style={{ marginBottom: 16 }}>
                        <Row gutter={16}>
                          <Col xs={24} md={8}>
                            <div
                              style={{
                                height: 200,
                                background: c.images?.[0]
                                  ? `url(${c.images[0]}) center/cover`
                                  : '#f0f0f0',
                                borderRadius: 8,
                              }}
                            />
                          </Col>
                          <Col xs={24} md={16}>
                            <Title level={4}>{c.projectName}</Title>
                            <Descriptions column={2} size="small">
                              <Descriptions.Item label="客户">{c.clientName}</Descriptions.Item>
                              <Descriptions.Item label="地点">{c.location}</Descriptions.Item>
                              <Descriptions.Item label="完成时间">
                                {c.completionDate}
                              </Descriptions.Item>
                            </Descriptions>
                            <Divider style={{ margin: '12px 0' }} />
                            <div>
                              <Text strong>实施效果：</Text>
                              <Paragraph style={{ marginTop: 8 }}>{c.results}</Paragraph>
                            </div>
                          </Col>
                        </Row>
                      </Card>
                    ))}
                  </div>
                ),
              },
              {
                key: 'docs',
                label: '技术文档',
                children: (
                  <Space direction="vertical" size={12} style={{ width: '100%' }}>
                    {solution.technicalDocs.map((doc: any, idx: number) => (
                      <Card key={idx} size="small">
                        <Row justify="space-between" align="middle">
                          <Col>
                            <Text strong>{doc.name}</Text>
                          </Col>
                          <Col>
                            <Button type="link" icon={<DownloadOutlined />}>
                              下载
                            </Button>
                          </Col>
                        </Row>
                      </Card>
                    ))}
                  </Space>
                ),
              },
            ]}
          />
        </Card>

        {/* 相关推荐 */}
        <Card title="相关解决方案" style={{ marginTop: 24 }}>
          <Empty description="暂无相关方案" />
        </Card>
      </div>
    </div>
  );
};

export default SolutionDetailPage;
