import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Tag,
  Select,
  Typography,
  Space,
  Button,
  Statistic,
  Empty,
  Spin,
  Input,
} from 'antd';
import {
  CheckCircleOutlined,
  FireOutlined,
  EyeOutlined,
  ShoppingCartOutlined,
  SearchOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { diyService } from '@/services/diy';

const { Title, Paragraph, Text } = Typography;
const { Search } = Input;

const SolutionsPage: React.FC = () => {
  const navigate = useNavigate();
  const [filters, setFilters] = useState({
    industry: undefined as string | undefined,
    scenario: undefined as string | undefined,
    search: '',
  });

  const { data: solutionsData, isLoading } = useQuery({
    queryKey: ['solutions', filters],
    queryFn: () => diyService.getSolutions(filters),
  });

  const industries = [
    { value: 'retail', label: '零售商超', icon: '🏪' },
    { value: 'warehouse', label: '仓储物流', icon: '📦' },
    { value: 'industrial', label: '工业制造', icon: '🏭' },
    { value: 'medical', label: '医疗医药', icon: '🏥' },
    { value: 'food', label: '食品加工', icon: '🍽️' },
    { value: 'agriculture', label: '农业养殖', icon: '🌾' },
  ];

  const scenarios = [
    { value: 'cold_storage', label: '冷库制冷' },
    { value: 'supermarket_freezer', label: '商超冷柜' },
    { value: 'cold_chain', label: '冷链运输' },
    { value: 'air_conditioning', label: '空调系统' },
    { value: 'quick_freeze', label: '速冻设备' },
    { value: 'fresh_keeping', label: '保鲜设备' },
  ];

  const handleSearch = (value: string) => {
    setFilters({ ...filters, search: value });
  };

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        {/* 页面标题和描述 */}
        <div style={{ marginBottom: 32 }}>
          <Title level={2}>解决方案中心</Title>
          <Paragraph type="secondary">
            为不同行业提供专业的制冷空调解决方案，经过实际验证的成熟方案，一键采购
          </Paragraph>
        </div>

        {/* 数据统计 */}
        <Card style={{ marginBottom: 24 }}>
          <Row gutter={[32, 16]}>
            <Col xs={24} sm={8}>
              <Statistic
                title="方案总数"
                value={solutionsData?.data?.length || 0}
                suffix="个"
                prefix={<CheckCircleOutlined />}
              />
            </Col>
            <Col xs={24} sm={8}>
              <Statistic
                title="服务客户"
                value={5000}
                suffix="+"
                prefix={<FireOutlined />}
              />
            </Col>
            <Col xs={24} sm={8}>
              <Statistic
                title="成功案例"
                value={800}
                suffix="+"
                prefix={<EyeOutlined />}
              />
            </Col>
          </Row>
        </Card>

        {/* 筛选区域 */}
        <Card style={{ marginBottom: 24 }}>
          <Space direction="vertical" size={16} style={{ width: '100%' }}>
            <div>
              <Text strong style={{ marginRight: 16 }}>
                行业分类：
              </Text>
              <Space wrap>
                <Tag
                  color={!filters.industry ? 'blue' : 'default'}
                  style={{ cursor: 'pointer' }}
                  onClick={() => setFilters({ ...filters, industry: undefined })}
                >
                  全部
                </Tag>
                {industries.map((ind) => (
                  <Tag
                    key={ind.value}
                    color={filters.industry === ind.value ? 'blue' : 'default'}
                    style={{ cursor: 'pointer' }}
                    onClick={() => setFilters({ ...filters, industry: ind.value })}
                  >
                    {ind.icon} {ind.label}
                  </Tag>
                ))}
              </Space>
            </div>

            <div>
              <Text strong style={{ marginRight: 16 }}>
                应用场景：
              </Text>
              <Space wrap>
                <Tag
                  color={!filters.scenario ? 'blue' : 'default'}
                  style={{ cursor: 'pointer' }}
                  onClick={() => setFilters({ ...filters, scenario: undefined })}
                >
                  全部
                </Tag>
                {scenarios.map((s) => (
                  <Tag
                    key={s.value}
                    color={filters.scenario === s.value ? 'blue' : 'default'}
                    style={{ cursor: 'pointer' }}
                    onClick={() => setFilters({ ...filters, scenario: s.value })}
                  >
                    {s.label}
                  </Tag>
                ))}
              </Space>
            </div>

            <Search
              placeholder="搜索解决方案..."
              allowClear
              enterButton={<SearchOutlined />}
              size="large"
              onSearch={handleSearch}
              style={{ maxWidth: 400 }}
            />
          </Space>
        </Card>

        {/* 方案列表 */}
        {isLoading ? (
          <div style={{ textAlign: 'center', padding: 60 }}>
            <Spin size="large" />
          </div>
        ) : solutionsData?.data?.length ? (
          <Row gutter={[24, 24]}>
            {solutionsData.data.map((solution: any) => (
              <Col xs={24} sm={12} lg={8} key={solution.id}>
                <Card
                  hoverable
                  cover={
                    <div
                      style={{
                        height: 200,
                        background: solution.coverImage
                          ? `url(${solution.coverImage}) center/cover`
                          : 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: '#fff',
                        fontSize: 24,
                        fontWeight: 'bold',
                      }}
                    >
                      {!solution.coverImage && solution.title}
                    </div>
                  }
                  actions={[
                    <Button
                      type="link"
                      onClick={() => navigate(`/solutions/${solution.id}`)}
                    >
                      查看详情
                    </Button>,
                    <Button
                      type="link"
                      icon={<ShoppingCartOutlined />}
                      onClick={() =>
                        navigate('/diy', { state: { solutionId: solution.id } })
                      }
                    >
                      使用方案
                    </Button>,
                  ]}
                >
                  <Card.Meta
                    title={
                      <div style={{ marginBottom: 8 }}>
                        <div style={{ fontSize: 16, fontWeight: 'bold' }}>
                          {solution.title}
                        </div>
                        <Space size={4} style={{ marginTop: 8 }}>
                          {solution.industry && (
                            <Tag color="blue">
                              {industries.find((i) => i.value === solution.industry)?.label}
                            </Tag>
                          )}
                          {solution.scenario && <Tag>{solution.scenario}</Tag>}
                        </Space>
                      </div>
                    }
                    description={
                      <Space direction="vertical" style={{ width: '100%' }} size={12}>
                        <Paragraph
                          ellipsis={{ rows: 2 }}
                          style={{ marginBottom: 0, minHeight: 44 }}
                        >
                          {solution.description || '专业的制冷空调解决方案'}
                        </Paragraph>

                        {solution.temperatureRange && (
                          <div>
                            <Text type="secondary">温度范围：</Text>
                            <Text strong>{solution.temperatureRange}</Text>
                          </div>
                        )}

                        {solution.features && solution.features.length > 0 && (
                          <div>
                            <Space wrap size={4}>
                              {solution.features.slice(0, 3).map((feature: string, idx: number) => (
                                <Tag key={idx} color="green" icon={<CheckCircleOutlined />}>
                                  {feature}
                                </Tag>
                              ))}
                            </Space>
                          </div>
                        )}

                        <div
                          style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                            paddingTop: 8,
                            borderTop: '1px solid #f0f0f0',
                          }}
                        >
                          <div>
                            <Text type="secondary">方案价格：</Text>
                            <Text
                              strong
                              style={{ fontSize: 18, color: '#ff4d4f', marginLeft: 4 }}
                            >
                              ¥{solution.totalPrice?.toFixed(2) || '0.00'}
                            </Text>
                          </div>
                          <Space size={16}>
                            <Text type="secondary">
                              <EyeOutlined /> {solution.viewCount || 0}
                            </Text>
                            <Text type="secondary">
                              <FireOutlined /> {solution.usageCount || 0}
                            </Text>
                          </Space>
                        </div>
                      </Space>
                    }
                  />
                </Card>
              </Col>
            ))}
          </Row>
        ) : (
          <Card>
            <Empty
              description="暂无符合条件的解决方案"
              style={{ padding: 60 }}
            >
              <Button type="primary" onClick={() => navigate('/diy')}>
                自定义配套方案
              </Button>
            </Empty>
          </Card>
        )}

        {/* CTA区域 */}
        <Card
          style={{
            marginTop: 32,
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            border: 'none',
            color: '#fff',
          }}
        >
          <Row align="middle" justify="space-between">
            <Col xs={24} md={16}>
              <Title level={3} style={{ color: '#fff', marginBottom: 8 }}>
                找不到合适的方案？
              </Title>
              <Paragraph style={{ color: '#fff', marginBottom: 0, fontSize: 16 }}>
                使用我们的智能DIY工具，根据您的具体需求定制专属解决方案
              </Paragraph>
            </Col>
            <Col xs={24} md={8} style={{ textAlign: 'right' }}>
              <Button
                type="primary"
                size="large"
                onClick={() => navigate('/diy')}
                style={{ background: '#fff', color: '#667eea', border: 'none' }}
              >
                开始DIY配套
              </Button>
            </Col>
          </Row>
        </Card>
      </div>
    </div>
  );
};

export default SolutionsPage;
