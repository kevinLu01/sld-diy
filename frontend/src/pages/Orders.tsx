import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Table,
  Tag,
  Button,
  Space,
  Typography,
  Tabs,
  Empty,
  Modal,
  Descriptions,
  Timeline,
  message,
  Popconfirm,
  Grid,
} from 'antd';
import {
  ClockCircleOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { orderService } from '@/services/order';
import type { Order } from '@/types';
import dayjs from 'dayjs';

const { Title, Text } = Typography;

const OrdersPage: React.FC = () => {
  const navigate = useNavigate();
  const screens = Grid.useBreakpoint();
  const isMobile = !screens.md;
  const queryClient = useQueryClient();
  const [statusFilter, setStatusFilter] = useState<string>('all');
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [detailVisible, setDetailVisible] = useState(false);

  const { data: ordersData, isLoading } = useQuery({
    queryKey: ['orders', statusFilter],
    queryFn: () =>
      orderService.getOrders({
        status: statusFilter === 'all' ? undefined : statusFilter,
        page: 1,
        limit: 20,
      }),
  });

  const cancelMutation = useMutation({
    mutationFn: (orderNo: string) => orderService.cancelOrder(orderNo),
    onSuccess: () => {
      message.success('订单已取消');
      queryClient.invalidateQueries({ queryKey: ['orders'] });
    },
    onError: () => {
      message.error('取消订单失败');
    },
  });

  const getStatusTag = (status: string) => {
    const statusMap: Record<
      string,
      { color: string; icon: React.ReactNode; text: string }
    > = {
      pending: {
        color: 'warning',
        icon: <ClockCircleOutlined />,
        text: '待支付',
      },
      paid: {
        color: 'processing',
        icon: <CheckCircleOutlined />,
        text: '已支付',
      },
      processing: {
        color: 'processing',
        icon: <CheckCircleOutlined />,
        text: '处理中',
      },
      shipped: {
        color: 'cyan',
        icon: <CheckCircleOutlined />,
        text: '已发货',
      },
      completed: {
        color: 'success',
        icon: <CheckCircleOutlined />,
        text: '已完成',
      },
      cancelled: {
        color: 'default',
        icon: <CloseCircleOutlined />,
        text: '已取消',
      },
    };
    const config = statusMap[status] || statusMap.pending;
    return (
      <Tag color={config.color} icon={config.icon}>
        {config.text}
      </Tag>
    );
  };

  const handleViewDetail = async (orderNo: string) => {
    try {
      const response = await orderService.getOrderDetail(orderNo);
      setSelectedOrder(response.data);
      setDetailVisible(true);
    } catch (error) {
      message.error('获取订单详情失败');
    }
  };

  const columns = [
    {
      title: '订单号',
      dataIndex: 'orderNo',
      key: 'orderNo',
      width: 180,
      render: (text: string) => <Text copyable>{text}</Text>,
    },
    {
      title: '下单时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 180,
      render: (text: string) => dayjs(text).format('YYYY-MM-DD HH:mm:ss'),
    },
    {
      title: '商品信息',
      key: 'items',
      render: (_: any, record: Order) => (
        <div>
          {record.items?.slice(0, 2).map((item) => (
            <div key={item.id} style={{ marginBottom: 4 }}>
              <Text ellipsis style={{ maxWidth: 200 }}>
                {item.productName} x {item.quantity}
              </Text>
            </div>
          ))}
          {record.items && record.items.length > 2 && (
            <Text type="secondary">等{record.items.length}件商品</Text>
          )}
        </div>
      ),
    },
    {
      title: '订单金额',
      dataIndex: 'finalAmount',
      key: 'finalAmount',
      width: 120,
      render: (amount: number) => (
        <Text strong style={{ color: '#ff4d4f' }}>
          ¥{amount.toFixed(2)}
        </Text>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status: string) => getStatusTag(status),
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_: any, record: Order) => (
        <Space>
          <Button type="link" size="small" onClick={() => handleViewDetail(record.orderNo)}>
            查看详情
          </Button>
          {record.status === 'pending' && (
            <Popconfirm
              title="确定要取消这个订单吗？"
              onConfirm={() => cancelMutation.mutate(record.orderNo)}
              okText="确定"
              cancelText="取消"
            >
              <Button type="link" size="small" danger>
                取消订单
              </Button>
            </Popconfirm>
          )}
          {record.status === 'pending' && (
            <Button type="link" size="small">
              去支付
            </Button>
          )}
        </Space>
      ),
    },
  ];

  const tabItems = [
    { key: 'all', label: '全部订单' },
    { key: 'pending', label: '待支付' },
    { key: 'paid', label: '已支付' },
    { key: 'processing', label: '处理中' },
    { key: 'shipped', label: '已发货' },
    { key: 'completed', label: '已完成' },
    { key: 'cancelled', label: '已取消' },
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: isMobile ? '12px 0' : '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: isMobile ? '0 12px' : '0 20px' }}>
        <Title level={2}>我的订单</Title>

        <Card>
          <Tabs
            activeKey={statusFilter}
            onChange={setStatusFilter}
            items={tabItems}
            style={{ marginBottom: 16 }}
          />

          {ordersData?.data?.items?.length ? (
            <Table
              dataSource={ordersData.data.items}
              columns={columns}
              rowKey="id"
              loading={isLoading}
              scroll={{ x: 920 }}
              pagination={{
                total: ordersData.data.total,
                pageSize: 20,
                showTotal: (total) => `共 ${total} 个订单`,
              }}
            />
          ) : (
            <Empty
              description={
                <Space direction="vertical">
                  <Text>暂无订单</Text>
                  <Button type="primary" onClick={() => navigate('/products')}>
                    去购物
                  </Button>
                </Space>
              }
              style={{ padding: 60 }}
            />
          )}
        </Card>

        {/* 订单详情Modal */}
        <Modal
          title="订单详情"
          open={detailVisible}
          onCancel={() => setDetailVisible(false)}
          footer={[
            <Button key="close" onClick={() => setDetailVisible(false)}>
              关闭
            </Button>,
          ]}
          width={isMobile ? '95%' : 800}
        >
          {selectedOrder && (
            <Space direction="vertical" size={24} style={{ width: '100%' }}>
              {/* 订单基本信息 */}
              <Card title="订单信息" size="small">
                <Descriptions column={2} size="small">
                  <Descriptions.Item label="订单号">{selectedOrder.orderNo}</Descriptions.Item>
                  <Descriptions.Item label="订单状态">
                    {getStatusTag(selectedOrder.status)}
                  </Descriptions.Item>
                  <Descriptions.Item label="下单时间">
                    {dayjs(selectedOrder.createdAt).format('YYYY-MM-DD HH:mm:ss')}
                  </Descriptions.Item>
                  {selectedOrder.paymentTime && (
                    <Descriptions.Item label="支付时间">
                      {dayjs(selectedOrder.paymentTime).format('YYYY-MM-DD HH:mm:ss')}
                    </Descriptions.Item>
                  )}
                </Descriptions>
              </Card>

              {/* 收货地址 */}
              <Card title="收货信息" size="small">
                <Descriptions column={1} size="small">
                  <Descriptions.Item label="收货人">
                    {selectedOrder.shippingAddress.recipient} {selectedOrder.shippingAddress.phone}
                  </Descriptions.Item>
                  <Descriptions.Item label="收货地址">
                    {selectedOrder.shippingAddress.province} {selectedOrder.shippingAddress.city}{' '}
                    {selectedOrder.shippingAddress.district}{' '}
                    {selectedOrder.shippingAddress.address}
                  </Descriptions.Item>
                </Descriptions>
              </Card>

              {/* 商品清单 */}
              <Card title="商品清单" size="small">
                <Table
                  dataSource={selectedOrder.items}
                  rowKey="id"
                  pagination={false}
                  size="small"
                  scroll={{ x: 620 }}
                  columns={[
                    {
                      title: '商品名称',
                      dataIndex: 'productName',
                      key: 'productName',
                    },
                    {
                      title: 'SKU',
                      dataIndex: 'productSku',
                      key: 'productSku',
                    },
                    {
                      title: '单价',
                      dataIndex: 'price',
                      key: 'price',
                      render: (price: number) => `¥${price.toFixed(2)}`,
                    },
                    {
                      title: '数量',
                      dataIndex: 'quantity',
                      key: 'quantity',
                    },
                    {
                      title: '小计',
                      dataIndex: 'subtotal',
                      key: 'subtotal',
                      render: (subtotal: number) => (
                        <Text strong style={{ color: '#ff4d4f' }}>
                          ¥{subtotal.toFixed(2)}
                        </Text>
                      ),
                    },
                  ]}
                />
              </Card>

              {/* 费用明细 */}
              <Card title="费用明细" size="small">
                <Descriptions column={1} size="small">
                  <Descriptions.Item label="商品总额">
                    ¥{selectedOrder.totalAmount.toFixed(2)}
                  </Descriptions.Item>
                  {selectedOrder.discountAmount > 0 && (
                    <Descriptions.Item label="优惠金额">
                      -¥{selectedOrder.discountAmount.toFixed(2)}
                    </Descriptions.Item>
                  )}
                  <Descriptions.Item label="运费">
                    ¥{selectedOrder.shippingFee.toFixed(2)}
                  </Descriptions.Item>
                  <Descriptions.Item label="实付金额">
                    <Text strong style={{ fontSize: 18, color: '#ff4d4f' }}>
                      ¥{selectedOrder.finalAmount.toFixed(2)}
                    </Text>
                  </Descriptions.Item>
                </Descriptions>
              </Card>

              {/* 订单状态跟踪 */}
              <Card title="订单跟踪" size="small">
                <Timeline
                  items={[
                    {
                      color: 'green',
                      children: (
                        <>
                          <div>订单已创建</div>
                          <div style={{ fontSize: 12, color: '#999' }}>
                            {dayjs(selectedOrder.createdAt).format('YYYY-MM-DD HH:mm:ss')}
                          </div>
                        </>
                      ),
                    },
                    ...(selectedOrder.status !== 'pending' ? [{
                      color: 'green',
                      children: (
                        <>
                          <div>支付成功</div>
                          {selectedOrder.paymentTime && (
                            <div style={{ fontSize: 12, color: '#999' }}>
                              {dayjs(selectedOrder.paymentTime).format('YYYY-MM-DD HH:mm:ss')}
                            </div>
                          )}
                        </>
                      ),
                    }] : []),
                    ...((selectedOrder.status === 'shipped' ||
                      selectedOrder.status === 'completed') ? [{
                      color: 'green',
                      children: (
                        <>
                          <div>商品已发货</div>
                          <div style={{ fontSize: 12, color: '#999' }}>待更新</div>
                        </>
                      ),
                    }] : []),
                    ...(selectedOrder.status === 'completed' ? [{
                      color: 'green',
                      children: (
                        <>
                          <div>订单已完成</div>
                          <div style={{ fontSize: 12, color: '#999' }}>待更新</div>
                        </>
                      ),
                    }] : []),
                  ]}
                />
              </Card>
            </Space>
          )}
        </Modal>
      </div>
    </div>
  );
};

export default OrdersPage;
