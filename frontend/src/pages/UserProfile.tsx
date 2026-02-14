import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Card,
  Row,
  Col,
  Typography,
  Form,
  Input,
  Button,
  Upload,
  Avatar,
  message,
  Tabs,
  Table,
  Tag,
  Space,
  Modal,
  Select,
} from 'antd';
import {
  UserOutlined,
  MailOutlined,
  PhoneOutlined,
  EnvironmentOutlined,
  EditOutlined,
  PlusOutlined,
  DeleteOutlined,
} from '@ant-design/icons';
import { useUserStore } from '@/store/user';
import type { UploadFile } from 'antd';

const { Title, Text } = Typography;

interface Address {
  id: number;
  recipient: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  address: string;
  isDefault: boolean;
}

const UserProfilePage: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useUserStore();
  const [activeTab, setActiveTab] = useState('profile');
  const [addressModalVisible, setAddressModalVisible] = useState(false);
  const [editingAddress, setEditingAddress] = useState<Address | null>(null);
  const [form] = Form.useForm();
  const [addressForm] = Form.useForm();

  // Mock 地址数据
  const [addresses, setAddresses] = useState<Address[]>([
    {
      id: 1,
      recipient: '张三',
      phone: '13800138000',
      province: '广东省',
      city: '深圳市',
      district: '南山区',
      address: '科技园南区深圳湾科技生态园10栋A座',
      isDefault: true,
    },
  ]);

  const handleProfileUpdate = async (values: any) => {
    try {
      // 这里应该调用API更新用户信息
      message.success('个人信息更新成功');
    } catch (error) {
      message.error('更新失败，请重试');
    }
  };

  const handleAddressSubmit = async (values: any) => {
    try {
      if (editingAddress) {
        // 更新地址
        setAddresses(
          addresses.map((addr) =>
            addr.id === editingAddress.id ? { ...addr, ...values } : addr
          )
        );
        message.success('地址更新成功');
      } else {
        // 新增地址
        const newAddress: Address = {
          id: Date.now(),
          ...values,
          isDefault: addresses.length === 0,
        };
        setAddresses([...addresses, newAddress]);
        message.success('地址添加成功');
      }
      setAddressModalVisible(false);
      addressForm.resetFields();
      setEditingAddress(null);
    } catch (error) {
      message.error('操作失败，请重试');
    }
  };

  const handleSetDefaultAddress = (id: number) => {
    setAddresses(
      addresses.map((addr) => ({
        ...addr,
        isDefault: addr.id === id,
      }))
    );
    message.success('默认地址设置成功');
  };

  const handleDeleteAddress = (id: number) => {
    setAddresses(addresses.filter((addr) => addr.id !== id));
    message.success('地址删除成功');
  };

  const addressColumns = [
    {
      title: '收货人',
      dataIndex: 'recipient',
      key: 'recipient',
    },
    {
      title: '联系电话',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: '收货地址',
      key: 'address',
      render: (_: any, record: Address) =>
        `${record.province} ${record.city} ${record.district} ${record.address}`,
    },
    {
      title: '标签',
      key: 'tag',
      render: (_: any, record: Address) =>
        record.isDefault && <Tag color="blue">默认</Tag>,
    },
    {
      title: '操作',
      key: 'action',
      width: 200,
      render: (_: any, record: Address) => (
        <Space>
          {!record.isDefault && (
            <Button
              type="link"
              size="small"
              onClick={() => handleSetDefaultAddress(record.id)}
            >
              设为默认
            </Button>
          )}
          <Button
            type="link"
            size="small"
            icon={<EditOutlined />}
            onClick={() => {
              setEditingAddress(record);
              addressForm.setFieldsValue(record);
              setAddressModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Button
            type="link"
            size="small"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteAddress(record.id)}
            disabled={record.isDefault}
          >
            删除
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div style={{ background: '#f5f5f5', minHeight: '100vh', padding: '24px 0' }}>
      <div style={{ maxWidth: 1200, margin: '0 auto', padding: '0 20px' }}>
        <Title level={2}>个人中心</Title>

        <Row gutter={24}>
          {/* 左侧用户信息卡片 */}
          <Col xs={24} md={8}>
            <Card>
              <div style={{ textAlign: 'center', paddingBottom: 16 }}>
                <Avatar size={100} src={user?.avatar} icon={<UserOutlined />} />
                <Title level={4} style={{ marginTop: 16, marginBottom: 0 }}>
                  {user?.username}
                </Title>
                <Text type="secondary">{user?.email}</Text>
              </div>

              <div style={{ marginTop: 24 }}>
                <div
                  onClick={() => navigate('/user/orders')}
                  style={{
                    padding: '12px 0',
                    cursor: 'pointer',
                    borderBottom: '1px solid #f0f0f0',
                  }}
                >
                  <Space>
                    <Text strong>我的订单</Text>
                  </Space>
                </div>
                <div
                  onClick={() => navigate('/user/diy-projects')}
                  style={{
                    padding: '12px 0',
                    cursor: 'pointer',
                    borderBottom: '1px solid #f0f0f0',
                  }}
                >
                  <Space>
                    <Text strong>DIY方案</Text>
                  </Space>
                </div>
                <div
                  onClick={() => setActiveTab('address')}
                  style={{
                    padding: '12px 0',
                    cursor: 'pointer',
                  }}
                >
                  <Space>
                    <Text strong>收货地址</Text>
                  </Space>
                </div>
              </div>
            </Card>
          </Col>

          {/* 右侧内容区域 */}
          <Col xs={24} md={16}>
            <Card>
              <Tabs
                activeKey={activeTab}
                onChange={setActiveTab}
                items={[
                  {
                    key: 'profile',
                    label: '个人信息',
                    children: (
                      <Form
                        form={form}
                        layout="vertical"
                        initialValues={{
                          username: user?.username,
                          email: user?.email,
                          phone: user?.phone,
                        }}
                        onFinish={handleProfileUpdate}
                      >
                        <Form.Item
                          label="用户名"
                          name="username"
                          rules={[{ required: true, message: '请输入用户名' }]}
                        >
                          <Input prefix={<UserOutlined />} size="large" />
                        </Form.Item>

                        <Form.Item
                          label="邮箱"
                          name="email"
                          rules={[
                            { required: true, message: '请输入邮箱' },
                            { type: 'email', message: '请输入有效的邮箱' },
                          ]}
                        >
                          <Input prefix={<MailOutlined />} size="large" disabled />
                        </Form.Item>

                        <Form.Item label="手机号" name="phone">
                          <Input prefix={<PhoneOutlined />} size="large" />
                        </Form.Item>

                        <Form.Item>
                          <Button type="primary" htmlType="submit" size="large">
                            保存修改
                          </Button>
                        </Form.Item>
                      </Form>
                    ),
                  },
                  {
                    key: 'address',
                    label: '收货地址',
                    children: (
                      <div>
                        <div style={{ marginBottom: 16 }}>
                          <Button
                            type="primary"
                            icon={<PlusOutlined />}
                            onClick={() => {
                              setEditingAddress(null);
                              addressForm.resetFields();
                              setAddressModalVisible(true);
                            }}
                          >
                            新增地址
                          </Button>
                        </div>
                        <Table
                          dataSource={addresses}
                          columns={addressColumns}
                          rowKey="id"
                          pagination={false}
                        />
                      </div>
                    ),
                  },
                  {
                    key: 'security',
                    label: '账号安全',
                    children: (
                      <div>
                        <Form layout="vertical">
                          <Form.Item label="当前密码">
                            <Input.Password size="large" />
                          </Form.Item>
                          <Form.Item label="新密码">
                            <Input.Password size="large" />
                          </Form.Item>
                          <Form.Item label="确认新密码">
                            <Input.Password size="large" />
                          </Form.Item>
                          <Form.Item>
                            <Button type="primary" size="large">
                              修改密码
                            </Button>
                          </Form.Item>
                        </Form>
                      </div>
                    ),
                  },
                ]}
              />
            </Card>
          </Col>
        </Row>

        {/* 地址编辑Modal */}
        <Modal
          title={editingAddress ? '编辑地址' : '新增地址'}
          open={addressModalVisible}
          onCancel={() => {
            setAddressModalVisible(false);
            setEditingAddress(null);
            addressForm.resetFields();
          }}
          footer={null}
          width={600}
        >
          <Form form={addressForm} layout="vertical" onFinish={handleAddressSubmit}>
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item
                  label="收货人"
                  name="recipient"
                  rules={[{ required: true, message: '请输入收货人姓名' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  label="联系电话"
                  name="phone"
                  rules={[{ required: true, message: '请输入联系电话' }]}
                >
                  <Input />
                </Form.Item>
              </Col>
            </Row>

            <Row gutter={16}>
              <Col span={8}>
                <Form.Item
                  label="省份"
                  name="province"
                  rules={[{ required: true, message: '请选择省份' }]}
                >
                  <Select placeholder="请选择">
                    <Select.Option value="广东省">广东省</Select.Option>
                    <Select.Option value="北京市">北京市</Select.Option>
                    <Select.Option value="上海市">上海市</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item
                  label="城市"
                  name="city"
                  rules={[{ required: true, message: '请选择城市' }]}
                >
                  <Select placeholder="请选择">
                    <Select.Option value="深圳市">深圳市</Select.Option>
                    <Select.Option value="广州市">广州市</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item
                  label="区县"
                  name="district"
                  rules={[{ required: true, message: '请选择区县' }]}
                >
                  <Select placeholder="请选择">
                    <Select.Option value="南山区">南山区</Select.Option>
                    <Select.Option value="福田区">福田区</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              label="详细地址"
              name="address"
              rules={[{ required: true, message: '请输入详细地址' }]}
            >
              <Input.TextArea rows={3} placeholder="请输入街道、楼栋号、门牌号等详细信息" />
            </Form.Item>

            <Form.Item>
              <Space>
                <Button type="primary" htmlType="submit">
                  保存
                </Button>
                <Button
                  onClick={() => {
                    setAddressModalVisible(false);
                    setEditingAddress(null);
                    addressForm.resetFields();
                  }}
                >
                  取消
                </Button>
              </Space>
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </div>
  );
};

export default UserProfilePage;
