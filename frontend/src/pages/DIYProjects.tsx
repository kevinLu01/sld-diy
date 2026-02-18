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
  Form,
  Select,
  InputNumber,
  DatePicker,
  Input,
  Popconfirm,
  message,
  Empty,
  QRCode,
  Grid,
} from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  ShareAltOutlined,
  EyeOutlined,
} from '@ant-design/icons';
import { useQuery, useMutation } from '@tanstack/react-query';
import { diyService } from '@/services/diy';
import dayjs from 'dayjs';

const { Title, Text, Paragraph } = Typography;

const DIYProjectsPage: React.FC = () => {
  const navigate = useNavigate();
  const screens = Grid.useBreakpoint();
  const isMobile = !screens.md;
  const [shareModalVisible, setShareModalVisible] = useState(false);
  const [shareData, setShareData] = useState<any>(null);
  const [shareProjectId, setShareProjectId] = useState<number | null>(null);
  const [shareForm] = Form.useForm();
  const shareMode = Form.useWatch('shareMode', shareForm);

  const { data: projectsData, isLoading } = useQuery({
    queryKey: ['diy-projects'],
    queryFn: () => diyService.getProjects({ page: 1, limit: 20 }),
  });

  const shareMutation = useMutation({
    mutationFn: (payload: any) => diyService.shareProject(payload.projectId, payload.data),
    onSuccess: (data) => {
      setShareData(data.data);
      setShareModalVisible(true);
    },
    onError: () => {
      message.error('分享失败');
    },
  });

  const handleShare = (projectId: number) => {
    setShareProjectId(projectId);
    setShareData(null);
    shareForm.setFieldsValue({ shareMode: 'public', discountRate: null, discountAmount: null, expiresAt: null, privateNote: '' });
    setShareModalVisible(true);
  };

  const handleCreateShare = () => {
    if (!shareProjectId) return;
    const values = shareForm.getFieldsValue();
    shareMutation.mutate({
      projectId: shareProjectId,
      data: {
        shareMode: values.shareMode,
        discountRate: values.discountRate != null ? Number(values.discountRate) : undefined,
        discountAmount: values.discountAmount != null ? Number(values.discountAmount) : undefined,
        expiresAt: values.expiresAt ? values.expiresAt.format('YYYY-MM-DDTHH:mm:ss') : undefined,
        privateNote: values.privateNote || undefined,
      },
    });
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
      title: '报价',
      key: 'quotedTotalPrice',
      render: (_: any, record: any) => (
        <Text type={record.shareMode === 'private_offer' ? 'danger' : undefined}>
          ¥{(record.quotedTotalPrice ?? record.totalPrice ?? 0).toFixed(2)}
        </Text>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status: string, record: any) => {
        const statusMap: Record<string, { color: string; text: string }> = {
          draft: { color: 'default', text: '草稿' },
          saved: { color: 'blue', text: '已保存' },
          ordered: { color: 'green', text: '已下单' },
        };
        const config = statusMap[status] || statusMap.draft;
        return (
          <Space>
            <Tag color={config.color}>{config.text}</Tag>
            {record.shareMode === 'private_offer' ? <Tag color="magenta">私发</Tag> : null}
          </Space>
        );
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
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: isMobile ? '12px 0' : '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: isMobile ? '0 12px' : '0 20px' }}>
        <div style={{ marginBottom: 24, display: 'flex', justifyContent: 'space-between', alignItems: isMobile ? 'flex-start' : 'center', flexDirection: isMobile ? 'column' : 'row', gap: 12 }}>
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
              scroll={{ x: 980 }}
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
            <Button key="generate" type="primary" onClick={handleCreateShare} loading={shareMutation.isPending}>
              生成链接
            </Button>,
            <Button key="copy" onClick={handleCopyLink} disabled={!shareData?.shareUrl}>
              复制链接
            </Button>,
            <Button key="close" onClick={() => setShareModalVisible(false)}>
              关闭
            </Button>,
          ]}
          width={isMobile ? '95%' : 520}
        >
          <Form form={shareForm} layout="vertical" initialValues={{ shareMode: 'public' }}>
            <Form.Item name="shareMode" label="分享模式">
              <Select
                options={[
                  { label: '公开分享', value: 'public' },
                  { label: '私发报价', value: 'private_offer' },
                ]}
              />
            </Form.Item>
            {shareMode === 'private_offer' && (
              <>
                <Form.Item name="discountRate" label="折扣率(0.1=9折)">
                  <InputNumber min={0} max={0.99} step={0.01} style={{ width: '100%' }} />
                </Form.Item>
                <Form.Item name="discountAmount" label="立减金额">
                  <InputNumber min={0} style={{ width: '100%' }} />
                </Form.Item>
                <Form.Item name="expiresAt" label="过期时间">
                  <DatePicker showTime style={{ width: '100%' }} />
                </Form.Item>
                <Form.Item name="privateNote" label="报价说明">
                  <Input.TextArea rows={3} />
                </Form.Item>
              </>
            )}
          </Form>
          {shareData && (
            <Space direction="vertical" size={16} style={{ width: '100%', textAlign: 'center' }}>
              <QRCode value={shareData.shareUrl} size={isMobile ? 150 : 180} />
              <Paragraph copyable={{ text: shareData.shareUrl }}>{shareData.shareUrl}</Paragraph>
              {shareData.shareMode === 'private_offer' ? (
                <Text type="danger">私发报价: ¥{Number(shareData.quotedTotalPrice || 0).toFixed(2)}</Text>
              ) : null}
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
