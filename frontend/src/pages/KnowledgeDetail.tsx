import React from 'react';
import { useParams } from 'react-router-dom';
import {
  Card,
  Typography,
  Space,
  Tag,
  Breadcrumb,
  Button,
  Divider,
  Empty,
  Spin,
  message,
} from 'antd';
import {
  HomeOutlined,
  BulbOutlined,
  LikeOutlined,
  EyeOutlined,
  ClockCircleOutlined,
} from '@ant-design/icons';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import dayjs from 'dayjs';
import { knowledgeService } from '@/services/knowledge';

const { Title, Paragraph, Text } = Typography;

const KnowledgeDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const queryClient = useQueryClient();

  const articleId = Number(id);

  const { data: articleData, isLoading } = useQuery({
    queryKey: ['article', articleId],
    queryFn: () => knowledgeService.getArticleDetail(articleId),
    enabled: Number.isFinite(articleId) && articleId > 0,
  });

  const helpfulMutation = useMutation({
    mutationFn: () => knowledgeService.markHelpful(articleId),
    onSuccess: async () => {
      message.success('感谢反馈');
      await queryClient.invalidateQueries({ queryKey: ['article', articleId] });
    },
    onError: () => message.error('提交失败，请稍后重试'),
  });

  const article = articleData?.data;

  if (isLoading) {
    return (
      <div style={{ textAlign: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!article) {
    return (
      <div style={{ padding: 100 }}>
        <Empty description="文章未找到" />
      </div>
    );
  }

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1000, margin: '0 auto', padding: '0 20px' }}>
        <Breadcrumb
          style={{ marginBottom: 24 }}
          items={[
            { title: <HomeOutlined />, href: '/' },
            { title: <BulbOutlined />, href: '/knowledge' },
            { title: article.title },
          ]}
        />

        <Card style={{ marginBottom: 24 }}>
          <Title level={2}>{article.title}</Title>

          <Space wrap style={{ marginBottom: 16 }}>
            {(article.tags || []).map((tag: string) => (
              <Tag key={tag} color="blue">
                {tag}
              </Tag>
            ))}
          </Space>

          <Space size={24} style={{ color: '#666' }}>
            <Space>
              <ClockCircleOutlined />
              <Text type="secondary">发布于 {dayjs(article.publishTime || article.createTime).format('YYYY-MM-DD')}</Text>
            </Space>
            <Space>
              <EyeOutlined />
              <Text type="secondary">{article.viewCount || 0} 阅读</Text>
            </Space>
            <Space>
              <LikeOutlined />
              <Text type="secondary">{article.helpfulCount || 0} 点赞</Text>
            </Space>
            <Text type="secondary">作者: {article.author || '-'}</Text>
          </Space>

          <Divider />

          <Space>
            <Button
              icon={<LikeOutlined />}
              loading={helpfulMutation.isPending}
              onClick={() => helpfulMutation.mutate()}
            >
              觉得有用
            </Button>
          </Space>
        </Card>

        <Card>
          {article.summary ? <Paragraph>{article.summary}</Paragraph> : null}
          <div
            style={{
              fontSize: 16,
              lineHeight: 1.8,
              color: '#333',
              whiteSpace: 'pre-wrap',
            }}
          >
            {article.content}
          </div>
        </Card>
      </div>
    </div>
  );
};

export default KnowledgeDetailPage;
