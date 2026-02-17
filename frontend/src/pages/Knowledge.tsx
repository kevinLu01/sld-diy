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
import { knowledgeService } from '@/services/knowledge';

const { Title, Paragraph, Text } = Typography;
const { Search } = Input;

const KnowledgePage: React.FC = () => {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('all');
  const [searchText, setSearchText] = useState('');
  const [page, setPage] = useState(1);

  const { data: knowledgeData, isLoading } = useQuery({
    queryKey: ['knowledge', activeTab, searchText, page],
    queryFn: () =>
      knowledgeService.getArticles({
        category: activeTab === 'all' ? undefined : activeTab,
        q: searchText || undefined,
        page,
        limit: 10,
      }),
  });

  const categories = [
    { key: 'all', label: '全部', icon: <FileTextOutlined /> },
    { key: 'technical', label: '技术文档', icon: <BulbOutlined /> },
    { key: 'tutorial', label: '安装教程', icon: <VideoCameraOutlined /> },
    { key: 'troubleshooting', label: '故障排查', icon: <QuestionCircleOutlined /> },
    { key: 'case', label: '案例分享', icon: <FileTextOutlined /> },
  ];

  const popularTags = [
    '压缩机',
    '冷凝器',
    '蒸发器',
    '制冷剂',
    '温控系统',
    '故障排查',
    '节能',
    '维护保养',
    '选型指南',
    '安装',
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <div style={{ marginBottom: 32 }}>
          <Title level={2}>知识库</Title>
          <Paragraph type="secondary">提供专业的制冷空调技术文档、安装教程和常见问题解答</Paragraph>
        </div>

        <Card style={{ marginBottom: 24 }}>
          <Search
            placeholder="搜索技术文档、教程、常见问题..."
            allowClear
            enterButton={<SearchOutlined />}
            size="large"
            onSearch={(value) => {
              setSearchText(value);
              setPage(1);
            }}
          />
        </Card>

        <Row gutter={24}>
          <Col xs={24} md={6}>
            <Card title="分类" style={{ marginBottom: 16 }}>
              <Space direction="vertical" style={{ width: '100%' }} size={8}>
                {categories.map((cat) => (
                  <div
                    key={cat.key}
                    onClick={() => {
                      setActiveTab(cat.key);
                      setPage(1);
                    }}
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
                    onClick={() => {
                      setSearchText(tag);
                      setPage(1);
                    }}
                  >
                    {tag}
                  </Tag>
                ))}
              </Space>
            </Card>
          </Col>

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
                    current: page,
                    pageSize: 10,
                    total: knowledgeData.data.total,
                    showTotal: (total) => `共 ${total} 篇文章`,
                    onChange: (nextPage) => setPage(nextPage),
                  }}
                  renderItem={(item: any) => (
                    <List.Item
                      key={item.id}
                      actions={[
                        <Space key="views">
                          <EyeOutlined />
                          <Text type="secondary">{item.viewCount || 0}</Text>
                        </Space>,
                        <Space key="helpful">
                          <LikeOutlined />
                          <Text type="secondary">{item.helpfulCount || 0}</Text>
                        </Space>,
                        <Text key="date" type="secondary">
                          {dayjs(item.publishTime || item.createTime).format('YYYY-MM-DD')}
                        </Text>,
                      ]}
                      extra={<Button type="primary" onClick={() => navigate(`/knowledge/${item.id}`)}>阅读全文</Button>}
                    >
                      <List.Item.Meta
                        avatar={<Avatar icon={<FileTextOutlined />} style={{ background: '#1890ff' }} size={48} />}
                        title={
                          <div onClick={() => navigate(`/knowledge/${item.id}`)} style={{ cursor: 'pointer', fontSize: 18 }}>
                            {item.title}
                          </div>
                        }
                        description={
                          <Space direction="vertical" size={8} style={{ width: '100%' }}>
                            <Paragraph ellipsis={{ rows: 2 }} type="secondary" style={{ marginBottom: 0 }}>
                              {item.summary || item.content}
                            </Paragraph>
                            <Space wrap>
                              {(item.tags || []).map((tag: string) => (
                                <Tag key={tag}>{tag}</Tag>
                              ))}
                            </Space>
                            <Text type="secondary" style={{ fontSize: 12 }}>
                              作者: {item.author || '-'}
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
      </div>
    </div>
  );
};

export default KnowledgePage;
