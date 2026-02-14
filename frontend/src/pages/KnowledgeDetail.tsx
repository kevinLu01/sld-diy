import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Card,
  Typography,
  Space,
  Tag,
  Breadcrumb,
  Button,
  Divider,
  Row,
  Col,
  Avatar,
  List,
  Empty,
  Spin,
} from 'antd';
import {
  HomeOutlined,
  BulbOutlined,
  LikeOutlined,
  EyeOutlined,
  ShareAltOutlined,
  DownloadOutlined,
  ClockCircleOutlined,
} from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import dayjs from 'dayjs';

const { Title, Paragraph, Text } = Typography;

// 临时mock数据
const getArticleDetail = async (id: number) => {
  return {
    data: {
      id,
      title: '制冷系统基础知识：压缩机的选型与配置',
      category: 'technical',
      content: `
# 一、压缩机在制冷系统中的作用

压缩机是制冷系统的核心部件，被称为制冷系统的"心脏"。它的主要作用是：

1. **提高制冷剂压力**：将低温低压的制冷剂气体压缩成高温高压气体
2. **推动制冷剂循环**：为整个制冷系统提供动力
3. **实现能量转换**：将电能转换为机械能，再转换为制冷效果

## 二、压缩机的分类

### 1. 活塞式压缩机
- 优点：结构简单、维修方便、价格实惠
- 缺点：噪音较大、振动明显
- 适用：中小型制冷系统

### 2. 涡旋式压缩机
- 优点：运转平稳、噪音低、效率高
- 缺点：价格较高、对清洁度要求严格
- 适用：商超冷柜、精密空调

### 3. 螺杆式压缩机
- 优点：制冷量大、运行可靠
- 缺点：初期投资大
- 适用：大型冷库、工业制冷

## 三、压缩机选型要点

### 1. 确定制冷量
首先需要计算所需的制冷量，主要考虑：
- 空间体积
- 降温要求
- 环境温度
- 热负荷

### 2. 选择压缩机类型
根据应用场景选择合适的压缩机类型：

| 应用场景 | 推荐类型 | 理由 |
|---------|---------|------|
| 商超冷柜 | 涡旋式 | 低噪音、高效率 |
| 冷库 | 螺杆式/活塞式 | 制冷量大、可靠性高 |
| 精密空调 | 涡旋式 | 温控精度高 |

### 3. 品牌选择建议
**高端品牌**：
- 松下（Panasonic）
- 艾默生（Emerson）
- 丹佛斯（Danfoss）

**性价比品牌**：
- 美优乐
- 比泽尔
- 谷轮

### 4. 功率匹配
压缩机功率需与系统其他部件匹配：
- 冷凝器面积
- 蒸发器容量
- 管路规格
- 控制系统

## 四、安装注意事项

1. **基础要求**
   - 安装基础必须平整、坚固
   - 预留足够的维修空间
   - 确保良好的通风

2. **减震措施**
   - 使用减震垫
   - 管路采用柔性连接
   - 避免刚性连接

3. **电气连接**
   - 使用专用电源
   - 安装过载保护
   - 接地必须可靠

## 五、维护保养

### 日常检查
- 检查运行声音
- 观察压力表
- 测量电流
- 检查润滑油

### 定期维护
- 更换润滑油（每年1-2次）
- 清洁冷凝器
- 检查电气系统
- 紧固螺栓

## 六、常见故障及处理

### 1. 压缩机不启动
**可能原因**：
- 电源故障
- 保护器动作
- 接线松动

**处理方法**：
- 检查电源
- 复位保护器
- 紧固接线端子

### 2. 制冷效果差
**可能原因**：
- 制冷剂不足
- 冷凝器散热不良
- 过滤器堵塞

**处理方法**：
- 检查并补充制冷剂
- 清洁冷凝器
- 更换过滤器

## 七、节能建议

1. 选择高效压缩机
2. 合理设置运行参数
3. 定期维护保养
4. 优化系统设计
5. 采用变频技术

---

**总结**

正确选择和使用压缩机是确保制冷系统高效运行的关键。在选型时要综合考虑制冷量、应用场景、预算等因素，并注重后期的维护保养。

如有疑问，欢迎联系我们的技术团队获取专业支持。
      `,
      tags: ['压缩机', '选型', '基础知识', '维护'],
      viewCount: 3200,
      helpfulCount: 156,
      createdAt: '2024-02-10',
      updatedAt: '2024-02-12',
      author: {
        name: '技术团队',
        avatar: '',
        title: '高级工程师',
      },
      attachments: [
        { name: '压缩机选型计算表.xlsx', url: '#' },
        { name: '安装指导手册.pdf', url: '#' },
      ],
      relatedArticles: [
        { id: 2, title: '冷凝器的选择与安装', viewCount: 1200 },
        { id: 3, title: '制冷剂充注标准流程', viewCount: 980 },
      ],
    },
  };
};

const KnowledgeDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const { data: articleData, isLoading } = useQuery({
    queryKey: ['article', id],
    queryFn: () => getArticleDetail(Number(id)),
    enabled: !!id,
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
        {/* 面包屑 */}
        <Breadcrumb
          style={{ marginBottom: 24 }}
          items={[
            { title: <HomeOutlined />, href: '/' },
            { title: <BulbOutlined />, href: '/knowledge' },
            { title: article.title },
          ]}
        />

        {/* 文章头部 */}
        <Card style={{ marginBottom: 24 }}>
          <Title level={2}>{article.title}</Title>
          
          <Space wrap style={{ marginBottom: 16 }}>
            {article.tags.map((tag: string) => (
              <Tag key={tag} color="blue">
                {tag}
              </Tag>
            ))}
          </Space>

          <Row gutter={16} style={{ marginBottom: 16 }}>
            <Col>
              <Space>
                <Avatar src={article.author.avatar} icon={<BulbOutlined />} />
                <div>
                  <div style={{ fontWeight: 500 }}>{article.author.name}</div>
                  <Text type="secondary" style={{ fontSize: 12 }}>
                    {article.author.title}
                  </Text>
                </div>
              </Space>
            </Col>
          </Row>

          <Space size={24} style={{ color: '#666' }}>
            <Space>
              <ClockCircleOutlined />
              <Text type="secondary">
                发布于 {dayjs(article.createdAt).format('YYYY-MM-DD')}
              </Text>
            </Space>
            <Space>
              <EyeOutlined />
              <Text type="secondary">{article.viewCount} 阅读</Text>
            </Space>
            <Space>
              <LikeOutlined />
              <Text type="secondary">{article.helpfulCount} 点赞</Text>
            </Space>
          </Space>

          <Divider />

          <Space>
            <Button icon={<LikeOutlined />}>觉得有用</Button>
            <Button icon={<ShareAltOutlined />}>分享</Button>
          </Space>
        </Card>

        <Row gutter={24}>
          {/* 文章正文 */}
          <Col xs={24} lg={18}>
            <Card>
              <div
                style={{
                  fontSize: 16,
                  lineHeight: 1.8,
                  color: '#333',
                }}
                dangerouslySetInnerHTML={{
                  __html: article.content
                    .replace(/\n/g, '<br/>')
                    .replace(/#{1,6}\s(.+)/g, '<h3 style="margin-top: 24px; margin-bottom: 16px; color: #1890ff;">$1</h3>')
                    .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
                    .replace(/\|(.+?)\|/g, '<div style="overflow-x: auto;">$1</div>'),
                }}
              />

              {/* 附件下载 */}
              {article.attachments && article.attachments.length > 0 && (
                <>
                  <Divider />
                  <div>
                    <Title level={4}>📎 相关附件</Title>
                    <Space direction="vertical" style={{ width: '100%' }}>
                      {article.attachments.map((file: any, idx: number) => (
                        <Card key={idx} size="small">
                          <Row justify="space-between" align="middle">
                            <Col>{file.name}</Col>
                            <Col>
                              <Button type="link" icon={<DownloadOutlined />}>
                                下载
                              </Button>
                            </Col>
                          </Row>
                        </Card>
                      ))}
                    </Space>
                  </div>
                </>
              )}
            </Card>
          </Col>

          {/* 侧边栏 */}
          <Col xs={24} lg={6}>
            {/* 相关文章 */}
            <Card title="相关文章" style={{ marginBottom: 16 }}>
              <List
                dataSource={article.relatedArticles}
                renderItem={(item: any) => (
                  <List.Item
                    style={{ cursor: 'pointer', padding: '12px 0' }}
                    onClick={() => navigate(`/knowledge/${item.id}`)}
                  >
                    <List.Item.Meta
                      title={
                        <Text ellipsis style={{ fontSize: 14 }}>
                          {item.title}
                        </Text>
                      }
                      description={
                        <Text type="secondary" style={{ fontSize: 12 }}>
                          <EyeOutlined /> {item.viewCount}
                        </Text>
                      }
                    />
                  </List.Item>
                )}
              />
            </Card>

            {/* 需要帮助 */}
            <Card>
              <div style={{ textAlign: 'center' }}>
                <BulbOutlined style={{ fontSize: 48, color: '#1890ff', marginBottom: 16 }} />
                <Title level={4}>需要帮助？</Title>
                <Paragraph type="secondary">
                  我们的技术团队随时为您提供专业支持
                </Paragraph>
                <Button type="primary" block>
                  联系技术支持
                </Button>
              </div>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default KnowledgeDetailPage;
