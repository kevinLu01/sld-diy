import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Table,
  Button,
  Space,
  Typography,
  Tag,
  Modal,
  Descriptions,
  Popconfirm,
  message,
  Empty,
  QRCode,
} from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  ShareAltOutlined,
  ShoppingCartOutlined,
  EyeOutlined,
} from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { diyService } from '@/services/diy';
import dayjs from 'dayjs';

const { Title, Text, Paragraph } = Typography;

const DIYProjectsPage: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [shareModalVisible, setShareModalVisible] = useState(false);
  const [shareData, setShareData] = useState<any>(null);

  const { data: projectsData, isLoading } = useQuery({
    queryKey: ['diy-projects'],
    queryFn: () => diyService.getProjects({ page: 1, limit: 20 }),
  });

  const shareMutation = useMutation({
    mutationFn: (projectId: number) => diyService.shareProject(projectId),
    onSuccess: (data) => {
      setShareData(data.data);
      setShareModalVisible(true);
    },
    onError: () => {
      message.error('分享失败');
    },
  });

  const handleShare = (projectId: number) => {
    shareMutation.mutate(projectId);
  };

  const handleCopyLink = () => {
    if (shareData?.shareUrl) {
      navigator.clipboard.writeText(shareData.shareUrl);
      message.success('链接已复制到剪贴板');
    }
  };

  const columns = [
    {
      title: '方案名称',
      dataIndex: 'projectName',
      key: 'projectName',
      render: (text: string, record: any) => (
        <div>
          <div style={{ fontWeight: 500 }}>{text}</div>
          <Text type="secondary" style={{ fontSize: 12 }}>
            {record.scenario && `场景: ${record.scenario}`}
          </Text>
        </div>
      ),
    },
    {
      title: '需求参数',
      key: 'requirements',
      render: (_: any, record: any) => (
        <Space direction="vertical" size={4}>
          {record.temperatureRange && (
            <Text type="secondary">温度: {record.temperatureRange}</Text>
          )}
          {record.coolingCapacity && (
            <Text type="secondary">
              制冷量: {record.coolingCapacity} {record.capacityUnit}
            </Text>
          )}
        </Space>
      ),
    },
    {
      title: '配件数量',
      key: 'itemCount',
      render: (_: any, record: any) => (
        <Text>{record.selectedProducts?.length || 0} 个</Text>
      ),
    },
    {
      title: '总价',
      dataIndex: 'totalPrice',
      key: 'totalPrice',
      render: (price: number) => (
        <Text strong style={{ color: '#ff4d4f' }}>
          ¥{price?.toFixed(2) || '0.00'}
        </Text>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => {
        const statusMap: Record<string, { color: string; text: string }> = {
          draft: { color: 'default', text: '草稿' },
          saved: { color: 'blue', text: '已保存' },
          ordered: { color: 'green', text: '已下单' },
        };
        const config = statusMap[status] || statusMap.draft;
        return <Tag color={config.color}>{config.text}</Tag>;
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => dayjs(date).format('YYYY-MM-DD HH:mm'),
    },
    {
      title: '操作',
      key: 'action',
      width: 220,
      render: (_: any, record: any) => (
        <Space size={0}>
          <Button
            type="link"
            size="small"
            icon={<EyeOutlined />}
            onClick={() => navigate(`/diy?projectId=${record.id}`)}
          >
            查看
          </Button>
          <Button
            type="link"
            size="small"
            icon={<EditOutlined />}
            onClick={() => navigate(`/diy?projectId=${record.id}&mode=edit`)}
          >
            编辑
          </Button>
          <Button
            type="link"
            size="small"
            icon={<ShareAltOutlined />}
            onClick={() => handleShare(record.id)}
          >
            分享
          </Button>
          {record.status !== 'ordered' && (
            <Popconfirm
              title="确定要删除这个方案吗？"
              onConfirm={() => message.success('删除成功')}
              okText="确定"
              cancelText="取消"
            >
              <Button type="link" size="small" danger icon={<DeleteOutlined />}>
                删除
              </Button>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <div style={{ marginBottom: 24, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Title level={2}>我的DIY方案</Title>
          <Button type="primary" onClick={() => navigate('/diy')}>
            创建新方案
          </Button>
        </div>

        <Card>
          {projectsData?.data?.items?.length ? (
            <Table
              dataSource={projectsData.data.items}
              columns={columns}
              rowKey="id"
              loading={isLoading}
              pagination={{
                total: projectsData.data.total,
                pageSize: 20,
                showTotal: (total) => `共 ${total} 个方案`,
              }}
            />
          ) : (
            <Empty
              description={
                <Space direction="vertical">
                  <Text>还没有保存的方案</Text>
                  <Button type="primary" onClick={() => navigate('/diy')}>
                    立即创建
                  </Button>
                </Space>
              }
              style={{ padding: 60 }}
            />
          )}
        </Card>

        {/* 分享弹窗 */}
        <Modal
          title="分享方案"
          open={shareModalVisible}
          onCancel={() => setShareModalVisible(false)}
          footer={[
            <Button key="copy" type="primary" onClick={handleCopyLink}>
              复制链接
            </Button>,
            <Button key="close" onClick={() => setShareModalVisible(false)}>
              关闭
            </Button>,
          ]}
        >
          {shareData && (
            <Space direction="vertical" size={24} style={{ width: '100%', textAlign: 'center' }}>
              <div>
                <QRCode value={shareData.shareUrl} size={200} />
              </div>
              <div>
                <Paragraph copyable={{ text: shareData.shareUrl }}>
                  {shareData.shareUrl}
                </Paragraph>
              </div>
              <div style={{ fontSize: 12, color: '#999' }}>
                扫描二维码或复制链接分享给朋友
              </div>
            </Space>
          )}
        </Modal>

        {/* 提示卡片 */}
        <Card style={{ marginTop: 16, background: '#f0f5ff', border: 'none' }}>
          <Space direction="vertical" size={8}>
            <Text strong>💡 方案管理提示：</Text>
            <Text type="secondary">1. 保存的方案可以随时编辑和修改</Text>
            <Text type="secondary">2. 分享方案链接给客户，方便沟通和确认</Text>
            <Text type="secondary">3. 已下单的方案不能删除，但可以查看详情</Text>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default DIYProjectsPage;
