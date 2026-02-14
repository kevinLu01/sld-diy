import React, { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Input,
  Select,
  Slider,
  Button,
  Space,
  Typography,
  Pagination,
  Spin,
  Empty,
} from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { productService } from '@/services/product';
import { useCartStore } from '@/store/cart';
import { message } from 'antd';

const { Title } = Typography;
const { Search } = Input;

const ProductsPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const { addItem } = useCartStore();

  const [filters, setFilters] = useState({
    search: searchParams.get('search') || '',
    category: searchParams.get('category') || undefined,
    brand: searchParams.get('brand') || undefined,
    sort: searchParams.get('sort') || 'sales',
    page: Number(searchParams.get('page')) || 1,
    limit: 20,
    priceRange: [0, 10000] as [number, number],
  });

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: () => productService.getCategories(),
  });

  const { data: productsData, isLoading } = useQuery({
    queryKey: ['products', filters],
    queryFn: () =>
      productService.getProducts({
        search: filters.search,
        category: filters.category,
        brand: filters.brand,
        sort: filters.sort,
        page: filters.page,
        limit: filters.limit,
        priceMin: filters.priceRange[0],
        priceMax: filters.priceRange[1],
      }),
  });

  const handleFilterChange = (key: string, value: any) => {
    const newFilters = { ...filters, [key]: value, page: 1 };
    setFilters(newFilters);
    
    const params: any = {};
    if (newFilters.search) params.search = newFilters.search;
    if (newFilters.category) params.category = newFilters.category;
    if (newFilters.brand) params.brand = newFilters.brand;
    if (newFilters.sort) params.sort = newFilters.sort;
    if (newFilters.page > 1) params.page = newFilters.page.toString();
    
    setSearchParams(params);
  };

  const handleAddToCart = async (productId: number, e: React.MouseEvent) => {
    e.stopPropagation();
    try {
      await addItem(productId, 1);
      message.success('已添加到购物车');
    } catch (error) {
      message.error('添加失败，请先登录');
    }
  };

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <Title level={2}>产品中心</Title>

        <Row gutter={24}>
          {/* 侧边筛选 */}
          <Col xs={24} md={6}>
            <Card title="筛选条件" style={{ marginBottom: 16 }}>
              <Space direction="vertical" style={{ width: '100%' }} size={16}>
                <div>
                  <div style={{ marginBottom: 8, fontWeight: 500 }}>分类</div>
                  <Select
                    style={{ width: '100%' }}
                    placeholder="全部分类"
                    allowClear
                    value={filters.category}
                    onChange={(value) => handleFilterChange('category', value)}
                  >
                    {categories?.data?.map((cat) => (
                      <Select.Option key={cat.slug} value={cat.slug}>
                        {cat.name}
                      </Select.Option>
                    ))}
                  </Select>
                </div>

                <div>
                  <div style={{ marginBottom: 8, fontWeight: 500 }}>价格范围</div>
                  <Slider
                    range
                    min={0}
                    max={10000}
                    step={100}
                    value={filters.priceRange}
                    onChange={(value) =>
                      setFilters({ ...filters, priceRange: value as [number, number] })
                    }
                    onAfterChange={(value) =>
                      handleFilterChange('priceRange', value as [number, number])
                    }
                  />
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: 12 }}>
                    <span>¥{filters.priceRange[0]}</span>
                    <span>¥{filters.priceRange[1]}</span>
                  </div>
                </div>

                <Button
                  block
                  onClick={() => {
                    setFilters({
                      search: '',
                      category: undefined,
                      brand: undefined,
                      sort: 'sales',
                      page: 1,
                      limit: 20,
                      priceRange: [0, 10000],
                    });
                    setSearchParams({});
                  }}
                >
                  重置筛选
                </Button>
              </Space>
            </Card>
          </Col>

          {/* 产品列表 */}
          <Col xs={24} md={18}>
            <Card style={{ marginBottom: 16 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Search
                  placeholder="搜索产品..."
                  allowClear
                  style={{ width: 300 }}
                  value={filters.search}
                  onChange={(e) => setFilters({ ...filters, search: e.target.value })}
                  onSearch={(value) => handleFilterChange('search', value)}
                />
                <Space>
                  <span>排序：</span>
                  <Select
                    style={{ width: 120 }}
                    value={filters.sort}
                    onChange={(value) => handleFilterChange('sort', value)}
                  >
                    <Select.Option value="sales">销量优先</Select.Option>
                    <Select.Option value="price">价格升序</Select.Option>
                    <Select.Option value="new">最新上架</Select.Option>
                  </Select>
                </Space>
              </div>
            </Card>

            {isLoading ? (
              <div style={{ textAlign: 'center', padding: 60 }}>
                <Spin size="large" />
              </div>
            ) : productsData?.data?.items?.length ? (
              <>
                <Row gutter={[16, 16]}>
                  {productsData.data.items.map((product) => (
                    <Col key={product.id} xs={24} sm={12} lg={8}>
                      <Card
                        hoverable
                        cover={
                          <img
                            alt={product.name}
                            src={product.images?.[0] || 'https://via.placeholder.com/300x200'}
                            style={{ height: 200, objectFit: 'cover', cursor: 'pointer' }}
                            onClick={() => navigate(`/products/${product.id}`)}
                          />
                        }
                        actions={[
                          <Button
                            type="primary"
                            icon={<ShoppingCartOutlined />}
                            onClick={(e) => handleAddToCart(product.id, e)}
                          >
                            加入购物车
                          </Button>,
                        ]}
                      >
                        <Card.Meta
                          title={
                            <div
                              onClick={() => navigate(`/products/${product.id}`)}
                              style={{ cursor: 'pointer' }}
                            >
                              {product.name}
                            </div>
                          }
                          description={
                            <Space direction="vertical" style={{ width: '100%' }}>
                              <div>{product.brand?.name}</div>
                              <div style={{ color: '#ff4d4f', fontSize: 20, fontWeight: 'bold' }}>
                                ¥{product.price.toFixed(2)}
                                {product.originalPrice && (
                                  <span
                                    style={{
                                      textDecoration: 'line-through',
                                      fontSize: 14,
                                      color: '#999',
                                      marginLeft: 8,
                                    }}
                                  >
                                    ¥{product.originalPrice.toFixed(2)}
                                  </span>
                                )}
                              </div>
                              <div style={{ fontSize: 12, color: '#999' }}>
                                库存: {product.stockQuantity} | 销量: {product.salesCount}
                              </div>
                            </Space>
                          }
                        />
                      </Card>
                    </Col>
                  ))}
                </Row>

                <div style={{ marginTop: 24, textAlign: 'center' }}>
                  <Pagination
                    current={filters.page}
                    total={productsData?.data?.total || 0}
                    pageSize={filters.limit}
                    showSizeChanger={false}
                    showTotal={(total) => `共 ${total} 件产品`}
                    onChange={(page) => handleFilterChange('page', page)}
                  />
                </div>
              </>
            ) : (
              <Empty description="暂无产品" style={{ padding: 60 }} />
            )}
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default ProductsPage;
