import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Row,
  Col,
  Image,
  Button,
  InputNumber,
  Tabs,
  Card,
  Descriptions,
  Rate,
  Space,
  Typography,
  Tag,
  Breadcrumb,
  message,
  Spin,
  Empty,
  Divider,
} from 'antd';
import {
  ShoppingCartOutlined,
  HomeOutlined,
  AppstoreOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { productService } from '@/services/product';
import { useCartStore } from '@/store/cart';

const { Title, Paragraph, Text } = Typography;

const ProductDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { addItem } = useCartStore();
  const [quantity, setQuantity] = useState(1);
  const [selectedImage, setSelectedImage] = useState(0);

  const { data: productData, isLoading } = useQuery({
    queryKey: ['product', id],
    queryFn: () => productService.getProductDetail(Number(id)),
    enabled: !!id,
  });

  const product = productData?.data;

  const handleAddToCart = async () => {
    if (!product) return;
    try {
      await addItem(product.id, quantity);
      message.success('已添加到购物车');
    } catch (error) {
      message.error('请先登录');
    }
  };

  const handleBuyNow = async () => {
    await handleAddToCart();
    navigate('/cart');
  };

  if (isLoading) {
    return (
      <div style={{ textAlign: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!product) {
    return (
      <div style={{ padding: 100 }}>
        <Empty description="产品未找到" />
      </div>
    );
  }

  const images = product.images || ['https://via.placeholder.com/600x600'];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        {/* 面包屑导航 */}
        <Breadcrumb
          style={{ marginBottom: 24 }}
          items={[
            { title: <HomeOutlined />, href: '/' },
            { title: <AppstoreOutlined />, href: '/products' },
            { title: product.category?.name || '产品' },
            { title: product.name },
          ]}
        />

        <Card>
          <Row gutter={[32, 32]}>
            {/* 左侧：产品图片 */}
            <Col xs={24} md={12}>
              <div>
                <Image
                  src={images[selectedImage]}
                  alt={product.name}
                  style={{ width: '100%', borderRadius: 8 }}
                  preview={{ src: images[selectedImage] }}
                />
                {images.length > 1 && (
                  <div
                    style={{
                      display: 'flex',
                      gap: 8,
                      marginTop: 16,
                      overflowX: 'auto',
                    }}
                  >
                    {images.map((img, idx) => (
                      <img
                        key={idx}
                        src={img}
                        alt={`${product.name} ${idx + 1}`}
                        style={{
                          width: 80,
                          height: 80,
                          objectFit: 'cover',
                          borderRadius: 4,
                          cursor: 'pointer',
                          border: selectedImage === idx ? '2px solid #1890ff' : '1px solid #d9d9d9',
                        }}
                        onClick={() => setSelectedImage(idx)}
                      />
                    ))}
                  </div>
                )}
              </div>
            </Col>

            {/* 右侧：产品信息 */}
            <Col xs={24} md={12}>
              <Space direction="vertical" size={16} style={{ width: '100%' }}>
                {/* 产品标题 */}
                <div>
                  <Title level={2} style={{ marginBottom: 8 }}>
                    {product.name}
                  </Title>
                  <Space>
                    {product.brand && <Tag color="blue">{product.brand.name}</Tag>}
                    <Tag>SKU: {product.sku}</Tag>
                    {product.status === 'active' ? (
                      <Tag icon={<CheckCircleOutlined />} color="success">
                        有货
                      </Tag>
                    ) : (
                      <Tag icon={<CloseCircleOutlined />} color="error">
                        缺货
                      </Tag>
                    )}
                  </Space>
                </div>

                {/* 评分 */}
                {product.rating && (
                  <div>
                    <Rate disabled value={product.rating} />
                    <Text type="secondary" style={{ marginLeft: 8 }}>
                      {product.rating.toFixed(1)} 分
                    </Text>
                  </div>
                )}

                {/* 价格 */}
                <div
                  style={{
                    background: '#f5f5f5',
                    padding: 16,
                    borderRadius: 8,
                  }}
                >
                  <Space align="baseline">
                    <Text type="secondary">价格：</Text>
                    <Title level={2} style={{ color: '#ff4d4f', margin: 0 }}>
                      ¥{product.price.toFixed(2)}
                    </Title>
                    {product.originalPrice && product.originalPrice > product.price && (
                      <Text delete type="secondary">
                        ¥{product.originalPrice.toFixed(2)}
                      </Text>
                    )}
                  </Space>
                </div>

                {/* 简要信息 */}
                <Descriptions column={1} size="small">
                  <Descriptions.Item label="库存">
                    {product.stockQuantity} {product.unit}
                  </Descriptions.Item>
                  <Descriptions.Item label="已售">{product.salesCount}</Descriptions.Item>
                  {product.brand && (
                    <Descriptions.Item label="品牌">{product.brand.name}</Descriptions.Item>
                  )}
                  {product.category && (
                    <Descriptions.Item label="分类">{product.category.name}</Descriptions.Item>
                  )}
                </Descriptions>

                <Divider />

                {/* 购买操作 */}
                <div>
                  <Space size={16} align="center">
                    <Text strong>数量：</Text>
                    <InputNumber
                      min={1}
                      max={product.stockQuantity}
                      value={quantity}
                      onChange={(value) => setQuantity(value || 1)}
                      style={{ width: 120 }}
                    />
                    <Text type="secondary">
                      库存：{product.stockQuantity} {product.unit}
                    </Text>
                  </Space>
                </div>

                <Space size={16} style={{ width: '100%' }}>
                  <Button
                    type="primary"
                    size="large"
                    icon={<ShoppingCartOutlined />}
                    onClick={handleAddToCart}
                    disabled={product.stockQuantity === 0}
                    style={{ flex: 1 }}
                  >
                    加入购物车
                  </Button>
                  <Button
                    type="default"
                    size="large"
                    onClick={handleBuyNow}
                    disabled={product.stockQuantity === 0}
                    style={{ flex: 1 }}
                  >
                    立即购买
                  </Button>
                </Space>
              </Space>
            </Col>
          </Row>
        </Card>

        {/* 详细信息标签页 */}
        <Card style={{ marginTop: 24 }}>
          <Tabs
            defaultActiveKey="details"
            items={[
              {
                key: 'details',
                label: '产品详情',
                children: (
                  <div style={{ padding: 16 }}>
                    {product.description ? (
                      <Paragraph>{product.description}</Paragraph>
                    ) : (
                      <Empty description="暂无详细描述" />
                    )}
                  </div>
                ),
              },
              {
                key: 'specs',
                label: '规格参数',
                children: (
                  <div style={{ padding: 16 }}>
                    {product.specifications && product.specifications.length > 0 ? (
                      <Descriptions bordered column={2}>
                        {product.specifications.map((spec) => (
                          <Descriptions.Item key={spec.id} label={spec.specKey}>
                            {spec.specValue} {spec.unit || ''}
                          </Descriptions.Item>
                        ))}
                      </Descriptions>
                    ) : (
                      <Empty description="暂无规格参数" />
                    )}
                  </div>
                ),
              },
              {
                key: 'reviews',
                label: '用户评价',
                children: (
                  <div style={{ padding: 16 }}>
                    <Empty description="暂无评价" />
                  </div>
                ),
              },
            ]}
          />
        </Card>

        {/* 相关产品推荐 */}
        <Card title="相关产品推荐" style={{ marginTop: 24 }}>
          <Empty description="暂无推荐产品" />
        </Card>
      </div>
    </div>
  );
};

export default ProductDetailPage;
