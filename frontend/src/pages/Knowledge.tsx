import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Input,
  Typography,
  Space,
  Tag,
  List,
  Avatar,
  Button,
  Empty,
  Spin,
} from 'antd';
import {
  FileTextOutlined,
  VideoCameraOutlined,
  QuestionCircleOutlined,
  BulbOutlined,
  SearchOutlined,
  EyeOutlined,
  LikeOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import dayjs from 'dayjs';

const { Title, Paragraph, Text } = Typography;
const { Search } = Input;

// 临时mock数据
const getKnowledgeData = async (_params: any) => {
  return {
    data: {
      items: [
        {
          id: 1,
          title: '制冷系统基础知识：压缩机的选型与配置',
          category: 'technical',
          content: '压缩机是制冷系统的核心部件，正确选型对系统性能至关重要...',
          tags: ['压缩机', '选型', '基础知识'],
          viewCount: 3200,
          helpfulCount: 156,
          createdAt: '2024-02-10',
          author: '技术团队',
        },
        {
          id: 2,
          title: '商超冷柜常见故障排查指南',
          category: 'troubleshooting',
          content: '本文详细介绍商超冷柜的常见故障及排查方法，包括制冷效果差、噪音过大等...',
          tags: ['故障排查', '商超冷柜', '维护'],
          viewCount: 2800,
          helpfulCount: 142,
          createdAt: '2024-02-08',
          author: '售后团队',
        },
        {
          id: 3,
          title: '如何选择合适的制冷剂？R134a vs R404A',
          category: 'tutorial',
          content: '制冷剂的选择直接影响制冷效果和能耗，本文对比分析常用制冷剂的特性...',
          tags: ['制冷剂', '选择指南', '对比'],
          viewCount: 2100,
          helpfulCount: 98,
          createdAt: '2024-02-05',
          author: '技术专家',
        },
        {
          id: 4,
          title: '冷库节能改造方案及实施步骤',
          category: 'case',
          content: '通过实际案例分享冷库节能改造的经验，包括设备更换、系统优化等...',
          tags: ['节能', '冷库', '改造'],
          viewCount: 1850,
          helpfulCount: 87,
          createdAt: '2024-02-01',
          author: '工程团队',
        },
      ],
      total: 48,
    },
  };
};

const KnowledgePage: React.FC = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('all');
  const [searchText, setSearchText] = useState('');

  const { data: knowledgeData, isLoading } = useQuery({
    queryKey: ['knowledge', activeTab, searchText],
    queryFn: () => getKnowledgeData({ category: activeTab, search: searchText }),
  });

  const categories = [
    { key: 'all', label: '全部', icon: <FileTextOutlined />, count: 48 },
    { key: 'technical', label: '技术文档', icon: <BulbOutlined />, count: 18 },
    { key: 'tutorial', label: '安装教程', icon: <VideoCameraOutlined />, count: 12 },
    { key: 'troubleshooting', label: '故障排查', icon: <QuestionCircleOutlined />, count: 10 },
    { key: 'case', label: '案例分享', icon: <FileTextOutlined />, count: 8 },
  ];

  const popularTags = [
    '压缩机', '冷凝器', '蒸发器', '制冷剂', '温控系统',
    '故障排查', '节能', '维护保养', '选型指南', '安装',
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <div style={{ marginBottom: 32 }}>
          <Title level={2}>知识库</Title>
          <Paragraph type="secondary">
            提供专业的制冷空调技术文档、安装教程和常见问题解答
          </Paragraph>
        </div>

        {/* 搜索框 */}
        <Card style={{ marginBottom: 24 }}>
          <Search
            placeholder="搜索技术文档、教程、常见问题..."
            allowClear
            enterButton={<SearchOutlined />}
            size="large"
            onSearch={setSearchText}
          />
        </Card>

        <Row gutter={24}>
          {/* 左侧分类和标签 */}
          <Col xs={24} md={6}>
            <Card title="分类" style={{ marginBottom: 16 }}>
              <Space direction="vertical" style={{ width: '100%' }} size={8}>
                {categories.map((cat) => (
                  <div
                    key={cat.key}
                    onClick={() => setActiveTab(cat.key)}
                    style={{
                      padding: '8px 12px',
                      borderRadius: 4,
                      cursor: 'pointer',
                      background: activeTab === cat.key ? '#e6f7ff' : 'transparent',
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                    }}
                  >
                    <Space>
                      {cat.icon}
                      <Text strong={activeTab === cat.key}>{cat.label}</Text>
                    </Space>
                    <Text type="secondary">{cat.count}</Text>
                  </div>
                ))}
              </Space>
            </Card>

            <Card title="热门标签">
              <Space wrap>
                {popularTags.map((tag) => (
                  <Tag
                    key={tag}
                    color="blue"
                    style={{ cursor: 'pointer' }}
                    onClick={() => setSearchText(tag)}
                  >
                    {tag}
                  </Tag>
                ))}
              </Space>
            </Card>
          </Col>

          {/* 右侧文章列表 */}
          <Col xs={24} md={18}>
            <Card>
              {isLoading ? (
                <div style={{ textAlign: 'center', padding: 60 }}>
                  <Spin size="large" />
                </div>
              ) : knowledgeData?.data?.items?.length ? (
                <List
                  itemLayout="vertical"
                  dataSource={knowledgeData.data.items}
                  pagination={{
                    pageSize: 10,
                    total: knowledgeData.data.total,
                    showTotal: (total) => `共 ${total} 篇文章`,
                  }}
                  renderItem={(item: any) => (
                    <List.Item
                      key={item.id}
                      actions={[
                        <Space>
                          <EyeOutlined />
                          <Text type="secondary">{item.viewCount}</Text>
                        </Space>,
                        <Space>
                          <LikeOutlined />
                          <Text type="secondary">{item.helpfulCount}</Text>
                        </Space>,
                        <Text type="secondary">{dayjs(item.createdAt).format('YYYY-MM-DD')}</Text>,
                      ]}
                      extra={
                        <Button type="primary" onClick={() => navigate(`/knowledge/${item.id}`)}>
                          阅读全文
                        </Button>
                      }
                    >
                      <List.Item.Meta
                        avatar={
                          <Avatar
                            icon={<FileTextOutlined />}
                            style={{ background: '#1890ff' }}
                            size={48}
                          />
                        }
                        title={
                          <div
                            onClick={() => navigate(`/knowledge/${item.id}`)}
                            style={{ cursor: 'pointer', fontSize: 18 }}
                          >
                            {item.title}
                          </div>
                        }
                        description={
                          <Space direction="vertical" size={8} style={{ width: '100%' }}>
                            <Paragraph
                              ellipsis={{ rows: 2 }}
                              type="secondary"
                              style={{ marginBottom: 0 }}
                            >
                              {item.content}
                            </Paragraph>
                            <Space wrap>
                              {item.tags.map((tag: string) => (
                                <Tag key={tag}>{tag}</Tag>
                              ))}
                            </Space>
                            <Text type="secondary" style={{ fontSize: 12 }}>
                              作者: {item.author}
                            </Text>
                          </Space>
                        }
                      />
                    </List.Item>
                  )}
                />
              ) : (
                <Empty description="暂无相关文章" style={{ padding: 60 }} />
              )}
            </Card>
          </Col>
        </Row>

        {/* 推荐阅读 */}
        <Card title="推荐阅读" style={{ marginTop: 24 }}>
          <Row gutter={[16, 16]}>
            <Col xs={24} sm={12} md={6}>
              <Card
                hoverable
                cover={
                  <div
                    style={{
                      height: 120,
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <FileTextOutlined style={{ fontSize: 48, color: '#fff' }} />
                  </div>
                }
              >
                <Card.Meta
                  title="新手入门"
                  description="从零开始了解制冷系统"
                />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                hoverable
                cover={
                  <div
                    style={{
                      height: 120,
                      background: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <VideoCameraOutlined style={{ fontSize: 48, color: '#fff' }} />
                  </div>
                }
              >
                <Card.Meta
                  title="视频教程"
                  description="手把手教你安装调试"
                />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                hoverable
                cover={
                  <div
                    style={{
                      height: 120,
                      background: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <QuestionCircleOutlined style={{ fontSize: 48, color: '#fff' }} />
                  </div>
                }
              >
                <Card.Meta
                  title="常见问题"
                  description="快速解决使用疑问"
                />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                hoverable
                cover={
                  <div
                    style={{
                      height: 120,
                      background: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <BulbOutlined style={{ fontSize: 48, color: '#fff' }} />
                  </div>
                }
              >
                <Card.Meta
                  title="技术文档"
                  description="深入了解技术细节"
                />
              </Card>
            </Col>
          </Row>
        </Card>
      </div>
    </div>
  );
};

export default KnowledgePage;
