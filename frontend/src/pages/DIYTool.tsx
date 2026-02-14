import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Steps,
  Card,
  Form,
  Select,
  InputNumber,
  Button,
  Space,
  Typography,
  Row,
  Col,
  Checkbox,
  Table,
  Image,
  Tag,
  message,
  Alert,
  Divider,
} from 'antd';
import {
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  SaveOutlined,
  ShoppingCartOutlined,
} from '@ant-design/icons';
import { useMutation } from '@tanstack/react-query';
import { diyService } from '@/services/diy';
import type { Product } from '@/types';

const { Title, Text, Paragraph } = Typography;
const { Step } = Steps;

interface SelectedProduct {
  productId: number;
  product: Product;
  quantity: number;
  category: string;
}

const DIYToolPage: React.FC = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [currentStep, setCurrentStep] = useState(0);
  const [selectedProducts, setSelectedProducts] = useState<SelectedProduct[]>([]);
  const [recommendedProducts, setRecommendedProducts] = useState<Record<string, Product[]>>({});
  const [compatibilityResult, setCompatibilityResult] = useState<any>(null);

  // 获取推荐的产品
  const recommendMutation = useMutation({
    mutationFn: (data: any) => diyService.recommend(data),
    onSuccess: (response) => {
      setRecommendedProducts(response.data.products || {});
      setCurrentStep(2);
      message.success('推荐方案已生成');
    },
    onError: () => {
      message.error('生成推荐失败，请稍后重试');
    },
  });

  // 验证兼容性
  const validateMutation = useMutation({
    mutationFn: (productIds: number[]) => diyService.validateCompatibility(productIds),
    onSuccess: (response) => {
      setCompatibilityResult(response.data);
      if (response.data.compatible) {
        message.success('所选配件完全兼容');
      } else if (response.data.errors.length > 0) {
        message.error('检测到不兼容的配件');
      } else {
        message.warning('配件可以使用，但有一些建议');
      }
    },
  });

  // 保存方案
  const saveMutation = useMutation({
    mutationFn: (data: any) => diyService.saveProject(data),
    onSuccess: () => {
      message.success('方案已保存');
      navigate('/user/diy-projects');
    },
  });

  const scenarios = [
    { value: 'cold_storage', label: '冷库制冷', description: '适用于各类冷库' },
    { value: 'supermarket_freezer', label: '商超冷柜', description: '超市冷藏冷冻柜' },
    { value: 'cold_chain', label: '冷链运输', description: '冷藏车、保温车' },
    { value: 'air_conditioning', label: '空调系统', description: '中央空调、分体空调' },
  ];

  const handleStepSubmit = () => {
    if (currentStep === 0) {
      // 选择场景
      const values = form.getFieldsValue();
      if (!values.scenario) {
        message.warning('请选择应用场景');
        return;
      }
      setCurrentStep(1);
    } else if (currentStep === 1) {
      // 配置需求
      form.validateFields().then((values) => {
        recommendMutation.mutate(values);
      });
    } else if (currentStep === 2) {
      // 选择配件，验证兼容性
      if (selectedProducts.length === 0) {
        message.warning('请至少选择一个配件');
        return;
      }
      const productIds = selectedProducts.map((item) => item.productId);
      validateMutation.mutate(productIds);
      setCurrentStep(3);
    }
  };

  const handleProductSelect = (product: Product, category: string, checked: boolean) => {
    if (checked) {
      setSelectedProducts((prev) => [
        ...prev,
        { productId: product.id, product, quantity: 1, category },
      ]);
    } else {
      setSelectedProducts((prev) => prev.filter((item) => item.productId !== product.id));
    }
  };

  const handleQuantityChange = (productId: number, quantity: number) => {
    setSelectedProducts((prev) =>
      prev.map((item) => (item.productId === productId ? { ...item, quantity } : item))
    );
  };

  const handleSaveProject = () => {
    form.validateFields().then((values) => {
      const projectData = {
        projectName: `DIY方案_${new Date().toLocaleDateString()}`,
        ...values,
        selectedProducts: selectedProducts.map((item) => ({
          productId: item.productId,
          quantity: item.quantity,
          notes: '',
        })),
      };
      saveMutation.mutate(projectData);
    });
  };

  const totalPrice = selectedProducts.reduce(
    (sum, item) => sum + item.product.price * item.quantity,
    0
  );

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <Title level={2}>智能DIY配套工具</Title>
        <Paragraph type="secondary">
          根据您的需求，智能推荐最合适的配件组合，确保兼容性和性能
        </Paragraph>

        {/* 步骤条 */}
        <Card style={{ marginBottom: 24 }}>
          <Steps current={currentStep}>
            <Step title="选择场景" description="选择应用场景" />
            <Step title="配置需求" description="输入具体参数" />
            <Step title="选择配件" description="查看推荐并选择" />
            <Step title="确认方案" description="验证兼容性" />
          </Steps>
        </Card>

        <Form form={form} layout="vertical">
          {/* 步骤 0: 选择场景 */}
          {currentStep === 0 && (
            <Card title="第一步：选择应用场景">
              <Form.Item
                name="scenario"
                label="应用场景"
                rules={[{ required: true, message: '请选择应用场景' }]}
              >
                <Row gutter={[16, 16]}>
                  {scenarios.map((s) => (
                    <Col xs={24} sm={12} md={6} key={s.value}>
                      <Card
                        hoverable
                        onClick={() => form.setFieldValue('scenario', s.value)}
                        style={{
                          border:
                            form.getFieldValue('scenario') === s.value
                              ? '2px solid #1890ff'
                              : '1px solid #d9d9d9',
                        }}
                      >
                        <Title level={4}>{s.label}</Title>
                        <Text type="secondary">{s.description}</Text>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </Form.Item>
            </Card>
          )}

          {/* 步骤 1: 配置需求 */}
          {currentStep === 1 && (
            <Card title="第二步：配置需求参数">
              <Row gutter={16}>
                <Col xs={24} md={12}>
                  <Form.Item
                    name="temperatureRange"
                    label="温度范围"
                    rules={[{ required: true, message: '请输入温度范围' }]}
                  >
                    <Select placeholder="选择温度范围">
                      <Select.Option value="-5~0">-5°C ~ 0°C (冷藏)</Select.Option>
                      <Select.Option value="-18~-15">-18°C ~ -15°C (冷冻)</Select.Option>
                      <Select.Option value="-25~-18">-25°C ~ -18°C (速冻)</Select.Option>
                      <Select.Option value="15~25">15°C ~ 25°C (空调)</Select.Option>
                    </Select>
                  </Form.Item>
                </Col>
                <Col xs={24} md={12}>
                  <Form.Item
                    name="coolingCapacity"
                    label="制冷量"
                    rules={[{ required: true, message: '请输入制冷量' }]}
                  >
                    <InputNumber
                      min={1}
                      max={1000}
                      placeholder="输入制冷量"
                      style={{ width: '100%' }}
                      addonAfter={
                        <Form.Item name="capacityUnit" noStyle initialValue="kW">
                          <Select style={{ width: 80 }}>
                            <Select.Option value="kW">kW</Select.Option>
                            <Select.Option value="HP">HP</Select.Option>
                          </Select>
                        </Form.Item>
                      }
                    />
                  </Form.Item>
                </Col>
              </Row>

              <Row gutter={16}>
                <Col xs={24} md={12}>
                  <Form.Item name="volume" label="空间体积（可选）">
                    <InputNumber
                      min={1}
                      placeholder="输入体积"
                      style={{ width: '100%' }}
                      addonAfter="m³"
                    />
                  </Form.Item>
                </Col>
                <Col xs={24} md={12}>
                  <Form.Item name="ambientTemp" label="环境温度">
                    <Select placeholder="选择环境温度">
                      <Select.Option value="10~20">10°C ~ 20°C</Select.Option>
                      <Select.Option value="20~30">20°C ~ 30°C</Select.Option>
                      <Select.Option value="30~40">30°C ~ 40°C</Select.Option>
                      <Select.Option value=">40">&gt; 40°C</Select.Option>
                    </Select>
                  </Form.Item>
                </Col>
              </Row>

              <Form.Item label="其他选项">
                <Space>
                  <Form.Item name={['options', 'energySaving']} valuePropName="checked" noStyle>
                    <Checkbox>节能优先</Checkbox>
                  </Form.Item>
                  <Form.Item name={['options', 'lowNoise']} valuePropName="checked" noStyle>
                    <Checkbox>低噪音</Checkbox>
                  </Form.Item>
                  <Form.Item name={['options', 'smartControl']} valuePropName="checked" noStyle>
                    <Checkbox>智能控制</Checkbox>
                  </Form.Item>
                </Space>
              </Form.Item>
            </Card>
          )}

          {/* 步骤 2: 选择配件 */}
          {currentStep === 2 && (
            <Card
              title="第三步：选择配件"
              extra={<Text>已选择 {selectedProducts.length} 个配件</Text>}
              loading={recommendMutation.isPending}
            >
              {Object.entries(recommendedProducts).map(([category, products]) => (
                <div key={category} style={{ marginBottom: 24 }}>
                  <Title level={4}>{category}</Title>
                  <Row gutter={[16, 16]}>
                    {products.map((product: Product) => {
                      const isSelected = selectedProducts.some(
                        (item) => item.productId === product.id
                      );
                      return (
                        <Col xs={24} sm={12} md={8} key={product.id}>
                          <Card
                            hoverable
                            cover={
                              <Image
                                src={product.images?.[0] || 'https://via.placeholder.com/300x200'}
                                height={150}
                                style={{ objectFit: 'cover' }}
                                preview={false}
                              />
                            }
                            style={{
                              border: isSelected ? '2px solid #52c41a' : '1px solid #d9d9d9',
                            }}
                          >
                            <Checkbox
                              checked={isSelected}
                              onChange={(e) =>
                                handleProductSelect(product, category, e.target.checked)
                              }
                            >
                              <Text strong>{product.name}</Text>
                            </Checkbox>
                            <div style={{ marginTop: 8 }}>
                              <Text type="danger" strong style={{ fontSize: 16 }}>
                                ¥{product.price.toFixed(2)}
                              </Text>
                              {product.brand && (
                                <Tag color="blue" style={{ marginLeft: 8 }}>
                                  {product.brand.name}
                                </Tag>
                              )}
                            </div>
                            <div style={{ marginTop: 8, fontSize: 12, color: '#999' }}>
                              库存: {product.stockQuantity} {product.unit}
                            </div>
                          </Card>
                        </Col>
                      );
                    })}
                  </Row>
                  <Divider />
                </div>
              ))}
            </Card>
          )}

          {/* 步骤 3: 确认方案 */}
          {currentStep === 3 && (
            <>
              {/* 兼容性检查结果 */}
              {compatibilityResult && (
                <Alert
                  message={
                    compatibilityResult.compatible
                      ? '配件兼容性检查通过'
                      : '发现兼容性问题'
                  }
                  description={
                    <Space direction="vertical" style={{ width: '100%' }}>
                      {compatibilityResult.warnings.map((warning: any, idx: number) => (
                        <Text key={idx} type="warning">
                          <ExclamationCircleOutlined /> {warning.message}
                        </Text>
                      ))}
                      {compatibilityResult.errors.map((error: any, idx: number) => (
                        <Text key={idx} type="danger">
                          <ExclamationCircleOutlined /> {error.message}
                        </Text>
                      ))}
                    </Space>
                  }
                  type={compatibilityResult.compatible ? 'success' : 'warning'}
                  showIcon
                  icon={
                    compatibilityResult.compatible ? (
                      <CheckCircleOutlined />
                    ) : (
                      <ExclamationCircleOutlined />
                    )
                  }
                  style={{ marginBottom: 24 }}
                />
              )}

              {/* 已选配件列表 */}
              <Card title="第四步：确认方案">
                <Table
                  dataSource={selectedProducts}
                  rowKey="productId"
                  pagination={false}
                  columns={[
                    {
                      title: '产品',
                      key: 'product',
                      render: (_, record) => (
                        <Space>
                          <Image
                            src={
                              record.product.images?.[0] ||
                              'https://via.placeholder.com/60x60'
                            }
                            width={60}
                            height={60}
                            style={{ borderRadius: 4 }}
                            preview={false}
                          />
                          <div>
                            <div>{record.product.name}</div>
                            <Text type="secondary" style={{ fontSize: 12 }}>
                              {record.category}
                            </Text>
                          </div>
                        </Space>
                      ),
                    },
                    {
                      title: '单价',
                      key: 'price',
                      render: (_, record) => `¥${record.product.price.toFixed(2)}`,
                    },
                    {
                      title: '数量',
                      key: 'quantity',
                      render: (_, record) => (
                        <InputNumber
                          min={1}
                          max={record.product.stockQuantity}
                          value={record.quantity}
                          onChange={(value) =>
                            handleQuantityChange(record.productId, value || 1)
                          }
                          style={{ width: 80 }}
                        />
                      ),
                    },
                    {
                      title: '小计',
                      key: 'subtotal',
                      render: (_, record) => (
                        <Text strong style={{ color: '#ff4d4f' }}>
                          ¥{(record.product.price * record.quantity).toFixed(2)}
                        </Text>
                      ),
                    },
                  ]}
                  summary={() => (
                    <Table.Summary fixed>
                      <Table.Summary.Row>
                        <Table.Summary.Cell index={0} colSpan={3}>
                          <Text strong>总计</Text>
                        </Table.Summary.Cell>
                        <Table.Summary.Cell index={1}>
                          <Text strong style={{ fontSize: 18, color: '#ff4d4f' }}>
                            ¥{totalPrice.toFixed(2)}
                          </Text>
                        </Table.Summary.Cell>
                      </Table.Summary.Row>
                    </Table.Summary>
                  )}
                />
              </Card>
            </>
          )}
        </Form>

        {/* 底部操作按钮 */}
        <Card style={{ marginTop: 24 }}>
          <Space style={{ width: '100%', justifyContent: 'space-between' }}>
            <Button onClick={() => currentStep > 0 && setCurrentStep(currentStep - 1)}>
              上一步
            </Button>
            <Space>
              {currentStep === 3 && (
                <>
                  <Button icon={<SaveOutlined />} onClick={handleSaveProject}>
                    保存方案
                  </Button>
                  <Button
                    type="primary"
                    icon={<ShoppingCartOutlined />}
                    onClick={() => navigate('/cart')}
                  >
                    加入购物车
                  </Button>
                </>
              )}
              {currentStep < 3 && (
                <Button
                  type="primary"
                  onClick={handleStepSubmit}
                  loading={recommendMutation.isPending || validateMutation.isPending}
                >
                  {currentStep === 2 ? '验证兼容性' : '下一步'}
                </Button>
              )}
            </Space>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default DIYToolPage;
