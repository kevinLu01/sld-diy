import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Table,
  Button,
  InputNumber,
  Image,
  Space,
  Typography,
  Empty,
  Popconfirm,
  message,
  Row,
  Col,
  Divider,
  Checkbox,
} from 'antd';
import { DeleteOutlined, ShoppingOutlined } from '@ant-design/icons';
import { useCartStore } from '@/store/cart';
import type { CartItem } from '@/types';

const { Title, Text } = Typography;

const CartPage: React.FC = () => {
  const navigate = useNavigate();
  const { items, loading, fetchCart, updateItem, removeItem, clearCart } =
    useCartStore();
  const [selectedItems, setSelectedItems] = useState<number[]>([]);

  useEffect(() => {
    fetchCart();
  }, [fetchCart]);

  const handleQuantityChange = async (itemId: number, quantity: number) => {
    if (quantity < 1) return;
    try {
      await updateItem(itemId, quantity);
      message.success('已更新数量');
    } catch (error) {
      message.error('更新失败');
    }
  };

  const handleRemove = async (itemId: number) => {
    try {
      await removeItem(itemId);
      message.success('已移除商品');
      setSelectedItems((prev) => prev.filter((id) => id !== itemId));
    } catch (error) {
      message.error('移除失败');
    }
  };

  const handleSelectAll = (checked: boolean) => {
    if (checked) {
      setSelectedItems(items.map((item) => item.id));
    } else {
      setSelectedItems([]);
    }
  };

  const handleSelectItem = (itemId: number, checked: boolean) => {
    if (checked) {
      setSelectedItems((prev) => [...prev, itemId]);
    } else {
      setSelectedItems((prev) => prev.filter((id) => id !== itemId));
    }
  };

  const selectedTotal = items
    .filter((item) => selectedItems.includes(item.id))
    .reduce((sum, item) => sum + item.price * item.quantity, 0);

  const handleCheckout = () => {
    if (selectedItems.length === 0) {
      message.warning('请先选择要结算的商品');
      return;
    }
    // 这里会跳转到结算页面
    navigate('/checkout', {
      state: {
        items: items.filter((item) => selectedItems.includes(item.id)),
      },
    });
  };

  const columns = [
    {
      title: (
        <Checkbox
          checked={selectedItems.length === items.length && items.length > 0}
          indeterminate={selectedItems.length > 0 && selectedItems.length < items.length}
          onChange={(e) => handleSelectAll(e.target.checked)}
        >
          全选
        </Checkbox>
      ),
      key: 'select',
      width: 80,
      render: (_: any, record: CartItem) => (
        <Checkbox
          checked={selectedItems.includes(record.id)}
          onChange={(e) => handleSelectItem(record.id, e.target.checked)}
        />
      ),
    },
    {
      title: '商品信息',
      key: 'product',
      render: (_: any, record: CartItem) => (
        <Space size={16}>
          <Image
            src={record.product.images?.[0] || 'https://via.placeholder.com/80x80'}
            width={80}
            height={80}
            style={{ borderRadius: 4, objectFit: 'cover' }}
            preview={false}
          />
          <div style={{ maxWidth: 300 }}>
            <div
              style={{ fontWeight: 500, cursor: 'pointer' }}
              onClick={() => navigate(`/products/${record.product.id}`)}
            >
              {record.product.name}
            </div>
            <div style={{ color: '#999', fontSize: 12, marginTop: 4 }}>
              SKU: {record.product.sku}
            </div>
            {record.product.brand && (
              <div style={{ color: '#999', fontSize: 12 }}>
                品牌: {record.product.brand.name}
              </div>
            )}
          </div>
        </Space>
      ),
    },
    {
      title: '单价',
      key: 'price',
      width: 120,
      render: (_: any, record: CartItem) => (
        <Text strong style={{ color: '#ff4d4f' }}>
          ¥{record.price.toFixed(2)}
        </Text>
      ),
    },
    {
      title: '数量',
      key: 'quantity',
      width: 150,
      render: (_: any, record: CartItem) => (
        <InputNumber
          min={1}
          max={record.product.stockQuantity}
          value={record.quantity}
          onChange={(value) => handleQuantityChange(record.id, value || 1)}
          style={{ width: 100 }}
        />
      ),
    },
    {
      title: '小计',
      key: 'subtotal',
      width: 120,
      render: (_: any, record: CartItem) => (
        <Text strong style={{ fontSize: 16, color: '#ff4d4f' }}>
          ¥{(record.price * record.quantity).toFixed(2)}
        </Text>
      ),
    },
    {
      title: '操作',
      key: 'action',
      width: 100,
      render: (_: any, record: CartItem) => (
        <Popconfirm
          title="确定要删除这件商品吗？"
          onConfirm={() => handleRemove(record.id)}
          okText="确定"
          cancelText="取消"
        >
          <Button type="link" danger icon={<DeleteOutlined />}>
            删除
          </Button>
        </Popconfirm>
      ),
    },
  ];

  if (items.length === 0 && !loading) {
    return (
      <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '60px 0' }}>
        <div style={{ maxWidth: 1200, margin: '0 auto' }}>
          <Card>
            <Empty
              description="购物车是空的"
              image={Empty.PRESENTED_IMAGE_SIMPLE}
              style={{ padding: 60 }}
            >
              <Button type="primary" onClick={() => navigate('/products')}>
                去逛逛
              </Button>
            </Empty>
          </Card>
        </div>
      </div>
    );
  }

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <Title level={2}>购物车</Title>

        <Card style={{ marginBottom: 16 }}>
          <Table
            dataSource={items}
            columns={columns}
            rowKey="id"
            loading={loading}
            pagination={false}
          />
        </Card>

        {/* 底部操作栏 */}
        <Card>
          <Row justify="space-between" align="middle">
            <Col>
              <Space size={24}>
                <Checkbox
                  checked={selectedItems.length === items.length && items.length > 0}
                  indeterminate={selectedItems.length > 0 && selectedItems.length < items.length}
                  onChange={(e) => handleSelectAll(e.target.checked)}
                >
                  全选
                </Checkbox>
                <Popconfirm
                  title="确定要清空购物车吗？"
                  onConfirm={async () => {
                    try {
                      await clearCart();
                      message.success('已清空购物车');
                      setSelectedItems([]);
                    } catch (error) {
                      message.error('操作失败');
                    }
                  }}
                  okText="确定"
                  cancelText="取消"
                >
                  <Button type="link" danger>
                    清空购物车
                  </Button>
                </Popconfirm>
                <Button type="link" onClick={() => navigate('/products')}>
                  继续购物
                </Button>
              </Space>
            </Col>

            <Col>
              <Space size={32} align="center">
                <div>
                  <Text>
                    已选商品 <Text strong>{selectedItems.length}</Text> 件
                  </Text>
                  <Divider type="vertical" />
                  <Text>
                    总计（不含运费）：
                    <Text strong style={{ fontSize: 20, color: '#ff4d4f' }}>
                      ¥{selectedTotal.toFixed(2)}
                    </Text>
                  </Text>
                </div>
                <Button
                  type="primary"
                  size="large"
                  onClick={handleCheckout}
                  disabled={selectedItems.length === 0}
                  icon={<ShoppingOutlined />}
                >
                  去结算
                </Button>
              </Space>
            </Col>
          </Row>
        </Card>

        {/* 购物提示 */}
        <Card style={{ marginTop: 16, background: '#f0f5ff', border: 'none' }}>
          <Space direction="vertical" size={8}>
            <Text strong>购物提示：</Text>
            <Text type="secondary">1. 请在下单前仔细核对商品信息和数量</Text>
            <Text type="secondary">2. 部分产品支持批量优惠，购买数量越多价格越优惠</Text>
            <Text type="secondary">3. 订单满500元免运费</Text>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default CartPage;
