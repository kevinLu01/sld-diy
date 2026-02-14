# 生利达冷冻空调配件商城 - 开发与部署指南

## 📋 目录
- [技术架构](#技术架构)
- [本地开发](#本地开发)
- [项目结构](#项目结构)
- [核心功能实现](#核心功能实现)
- [部署方案](#部署方案)
- [性能优化](#性能优化)
- [安全策略](#安全策略)

---

## 🏗️ 技术架构

### 系统架构图
```
┌─────────────────────────────────────────────────────┐
│                    用户端                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐          │
│  │   Web    │  │  Mobile  │  │  WeChat  │          │
│  │  Browser │  │   App    │  │ MiniApp  │          │
│  └─────┬────┘  └─────┬────┘  └─────┬────┘          │
└────────┼─────────────┼─────────────┼────────────────┘
         │             │             │
         └─────────────┴─────────────┘
                       │
         ┌─────────────▼─────────────┐
         │      Nginx / CDN          │
         │  (负载均衡 + 静态资源)      │
         └─────────────┬─────────────┘
                       │
         ┌─────────────▼─────────────┐
         │     API Gateway           │
         │  (鉴权 + 限流 + 路由)       │
         └─────────────┬─────────────┘
                       │
    ┌──────────────────┼──────────────────┐
    │                  │                  │
┌───▼────┐      ┌─────▼─────┐     ┌─────▼─────┐
│ 产品服务 │      │  订单服务  │     │  用户服务  │
│ Service │      │  Service  │     │  Service  │
└───┬────┘      └─────┬─────┘     └─────┬─────┘
    │                  │                  │
    └──────────────────┼──────────────────┘
                       │
         ┌─────────────▼─────────────┐
         │      Redis (缓存)         │
         │   Elasticsearch (搜索)    │
         │   PostgreSQL (数据库)     │
         │   MinIO (文件存储)        │
         └───────────────────────────┘
```

### 技术栈选型

#### 前端技术栈
```javascript
{
  "框架": "React 18 + Next.js 14",
  "状态管理": "Zustand / Redux Toolkit",
  "UI组件": "Tailwind CSS + shadcn/ui",
  "3D渲染": "Three.js + React Three Fiber",
  "图表": "Recharts / Chart.js",
  "HTTP客户端": "Axios",
  "表单": "React Hook Form + Zod",
  "国际化": "next-i18next"
}
```

#### 后端技术栈
```javascript
{
  "运行时": "Node.js 20 LTS",
  "框架": "Express.js / Fastify",
  "ORM": "Prisma / TypeORM",
  "认证": "JWT + Passport.js",
  "验证": "Joi / Zod",
  "任务队列": "Bull (基于Redis)",
  "日志": "Winston + Morgan",
  "API文档": "Swagger / OpenAPI"
}
```

#### 数据库与存储
```javascript
{
  "关系型数据库": "PostgreSQL 15",
  "缓存": "Redis 7",
  "搜索引擎": "Elasticsearch 8",
  "对象存储": "MinIO / 阿里云OSS",
  "消息队列": "RabbitMQ / Redis"
}
```

---

## 💻 本地开发

### 环境要求
- Node.js >= 18.0.0
- PostgreSQL >= 14.0
- Redis >= 6.0
- npm >= 9.0 或 pnpm >= 8.0

### 快速启动

#### 1. 克隆项目
```bash
git clone https://github.com/sld/sld-mall.git
cd sld-mall
```

#### 2. 安装依赖
```bash
# 前端依赖
cd frontend
npm install

# 后端依赖
cd ../backend
npm install
```

#### 3. 环境配置
```bash
# 后端环境变量
cp .env.example .env

# 编辑 .env 文件
DATABASE_URL="postgresql://user:password@localhost:5432/sld_mall"
REDIS_URL="redis://localhost:6379"
JWT_SECRET="your-secret-key"
PORT=3000
```

#### 4. 初始化数据库
```bash
cd backend

# 运行数据库迁移
npm run migrate

# 导入初始数据
npm run seed
```

#### 5. 启动服务

```bash
# 终端1: 启动后端
cd backend
npm run dev
# 运行在 http://localhost:3000

# 终端2: 启动前端
cd frontend
npm run dev
# 运行在 http://localhost:3001
```

---

## 📁 项目结构

```
sld-mall/
├── frontend/                    # 前端项目
│   ├── public/                  # 静态资源
│   ├── src/
│   │   ├── app/                 # Next.js App Router
│   │   │   ├── (shop)/         # 商城页面组
│   │   │   │   ├── page.tsx    # 首页
│   │   │   │   ├── products/   # 产品列表
│   │   │   │   ├── diy/        # DIY工具
│   │   │   │   └── solutions/  # 解决方案
│   │   │   ├── (account)/      # 账户页面组
│   │   │   │   ├── login/
│   │   │   │   ├── profile/
│   │   │   │   └── orders/
│   │   │   └── api/            # API路由
│   │   ├── components/          # 组件
│   │   │   ├── ui/             # 基础UI组件
│   │   │   ├── product/        # 产品相关组件
│   │   │   ├── diy/            # DIY工具组件
│   │   │   └── layout/         # 布局组件
│   │   ├── lib/                # 工具库
│   │   │   ├── api.ts          # API客户端
│   │   │   ├── auth.ts         # 认证工具
│   │   │   └── utils.ts        # 通用工具
│   │   ├── hooks/              # 自定义Hooks
│   │   ├── store/              # 状态管理
│   │   ├── types/              # TypeScript类型
│   │   └── styles/             # 样式文件
│   ├── package.json
│   └── next.config.js
│
├── backend/                     # 后端项目
│   ├── src/
│   │   ├── routes/             # 路由定义
│   │   │   ├── auth.routes.ts
│   │   │   ├── products.routes.ts
│   │   │   ├── diy.routes.ts
│   │   │   ├── orders.routes.ts
│   │   │   └── solutions.routes.ts
│   │   ├── controllers/        # 控制器
│   │   │   ├── auth.controller.ts
│   │   │   ├── products.controller.ts
│   │   │   └── diy.controller.ts
│   │   ├── services/           # 业务逻辑
│   │   │   ├── product.service.ts
│   │   │   ├── recommendation.service.ts
│   │   │   ├── compatibility.service.ts
│   │   │   └── order.service.ts
│   │   ├── models/             # 数据模型
│   │   │   ├── user.model.ts
│   │   │   ├── product.model.ts
│   │   │   └── order.model.ts
│   │   ├── middlewares/        # 中间件
│   │   │   ├── auth.middleware.ts
│   │   │   ├── error.middleware.ts
│   │   │   └── validation.middleware.ts
│   │   ├── utils/              # 工具函数
│   │   ├── config/             # 配置
│   │   └── app.ts              # 应用入口
│   ├── prisma/                 # Prisma配置
│   │   ├── schema.prisma
│   │   └── migrations/
│   ├── tests/                  # 测试
│   └── package.json
│
├── database/                    # 数据库脚本
│   ├── schema.sql
│   ├── seeds/
│   │   ├── categories.sql
│   │   ├── products.sql
│   │   └── solutions.sql
│   └── migrations/
│
├── docs/                        # 文档
│   ├── API.md                  # API文档
│   ├── DEPLOYMENT.md           # 部署文档
│   └── DEVELOPMENT.md          # 开发文档
│
├── docker/                      # Docker配置
│   ├── Dockerfile.frontend
│   ├── Dockerfile.backend
│   └── docker-compose.yml
│
├── .github/                     # GitHub配置
│   └── workflows/
│       ├── ci.yml
│       └── deploy.yml
│
└── README.md
```

---

## 🔧 核心功能实现

### 1. 智能推荐算法

```typescript
// backend/src/services/recommendation.service.ts

class RecommendationService {
  /**
   * 智能推荐配件
   */
  async recommendProducts(requirements: DIYRequirements): Promise<Recommendation> {
    // 1. 解析需求参数
    const parsedReq = this.parseRequirements(requirements);
    
    // 2. 基于规则匹配候选产品
    const candidates = await this.findCandidateProducts(parsedReq);
    
    // 3. 计算匹配分数
    const scored = this.calculateMatchScores(candidates, parsedReq);
    
    // 4. 验证配件兼容性
    const compatible = await this.validateCompatibility(scored);
    
    // 5. 优化组合方案
    const optimized = this.optimizeCombination(compatible, parsedReq.options);
    
    // 6. 生成推荐结果
    return this.buildRecommendation(optimized, parsedReq);
  }
  
  /**
   * 计算产品匹配分数
   */
  private calculateMatchScores(products: Product[], requirements: any) {
    return products.map(product => ({
      ...product,
      matchScore: this.computeScore(product, requirements)
    })).sort((a, b) => b.matchScore - a.matchScore);
  }
  
  /**
   * 计算单个产品分数
   */
  private computeScore(product: Product, requirements: any): number {
    let score = 0;
    
    // 制冷量匹配 (权重: 40%)
    const capacityMatch = this.matchCoolingCapacity(
      product.specifications.cooling_capacity,
      requirements.coolingCapacity
    );
    score += capacityMatch * 40;
    
    // 温度适应性 (权重: 30%)
    const tempMatch = this.matchTemperatureRange(
      product.specifications.temp_range,
      requirements.temperatureRange
    );
    score += tempMatch * 30;
    
    // 能效等级 (权重: 20%)
    if (requirements.options.energySaving) {
      score += this.getEnergyScore(product.specifications.energy_efficiency) * 20;
    }
    
    // 品牌偏好 (权重: 10%)
    if (requirements.preferredBrands?.includes(product.brand)) {
      score += 10;
    }
    
    return Math.min(score, 100);
  }
}
```

### 2. 兼容性验证系统

```typescript
// backend/src/services/compatibility.service.ts

class CompatibilityService {
  /**
   * 验证产品组合兼容性
   */
  async validateCombination(productIds: number[]): Promise<CompatibilityResult> {
    const products = await this.getProducts(productIds);
    const matrix = await this.buildCompatibilityMatrix(products);
    
    const errors: CompatibilityError[] = [];
    const warnings: CompatibilityWarning[] = [];
    
    // 检查制冷剂兼容性
    this.checkRefrigerantCompatibility(products, errors);
    
    // 检查电气参数匹配
    this.checkElectricalCompatibility(products, errors);
    
    // 检查容量匹配
    this.checkCapacityMatching(products, warnings);
    
    // 检查物理接口
    this.checkPhysicalInterface(products, errors);
    
    return {
      compatible: errors.length === 0,
      errors,
      warnings,
      matrix
    };
  }
  
  /**
   * 检查制冷剂兼容性
   */
  private checkRefrigerantCompatibility(products: Product[], errors: any[]) {
    const refrigerants = products
      .map(p => p.specifications.refrigerant)
      .filter(r => r);
    
    const uniqueRefrigerants = [...new Set(refrigerants)];
    
    if (uniqueRefrigerants.length > 1) {
      errors.push({
        type: 'refrigerant_mismatch',
        message: `检测到多种制冷剂: ${uniqueRefrigerants.join(', ')}`,
        severity: 'error',
        affectedProducts: products.filter(p => 
          p.specifications.refrigerant
        ).map(p => p.id)
      });
    }
  }
}
```

### 3. 3D可视化组件

```typescript
// frontend/src/components/diy/ProductViewer3D.tsx

import { Canvas } from '@react-three/fiber';
import { OrbitControls, useGLTF } from '@react-three/drei';

export function ProductViewer3D({ products }: { products: Product[] }) {
  return (
    <Canvas camera={{ position: [0, 0, 5] }}>
      <ambientLight intensity={0.5} />
      <spotLight position={[10, 10, 10]} angle={0.15} />
      
      {products.map((product, index) => (
        <ProductModel
          key={product.id}
          url={product.model3d}
          position={[index * 2, 0, 0]}
        />
      ))}
      
      <OrbitControls />
    </Canvas>
  );
}

function ProductModel({ url, position }: any) {
  const { scene } = useGLTF(url);
  return <primitive object={scene} position={position} />;
}
```

---

## 🚀 部署方案

### Docker部署

#### docker-compose.yml
```yaml
version: '3.8'

services:
  # PostgreSQL数据库
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: sld_mall
      POSTGRES_USER: sld_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    networks:
      - sld-network

  # Redis缓存
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - sld-network

  # Elasticsearch搜索引擎
  elasticsearch:
    image: elasticsearch:8.11.0
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - es_data:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
    networks:
      - sld-network

  # 后端API服务
  backend:
    build:
      context: ./backend
      dockerfile: ../docker/Dockerfile.backend
    environment:
      NODE_ENV: production
      DATABASE_URL: postgresql://sld_user:${DB_PASSWORD}@postgres:5432/sld_mall
      REDIS_URL: redis://redis:6379
      ELASTICSEARCH_URL: http://elasticsearch:9200
    ports:
      - "3000:3000"
    depends_on:
      - postgres
      - redis
      - elasticsearch
    networks:
      - sld-network

  # 前端应用
  frontend:
    build:
      context: ./frontend
      dockerfile: ../docker/Dockerfile.frontend
    environment:
      NEXT_PUBLIC_API_URL: http://backend:3000/api
    ports:
      - "3001:3000"
    depends_on:
      - backend
    networks:
      - sld-network

  # Nginx反向代理
  nginx:
    image: nginx:alpine
    volumes:
      - ./docker/nginx.conf:/etc/nginx/nginx.conf
      - ./frontend/public:/usr/share/nginx/html/static
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - frontend
      - backend
    networks:
      - sld-network

volumes:
  postgres_data:
  redis_data:
  es_data:

networks:
  sld-network:
    driver: bridge
```

### 云服务部署(阿里云)

#### 1. 服务器配置
```
生产环境推荐配置:
- ECS: 4核8G (2台,做负载均衡)
- RDS PostgreSQL: 2核4G
- Redis: 2G标准版
- OSS: 标准存储
- SLB: 负载均衡器
```

#### 2. CI/CD流程
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Build Docker Images
        run: |
          docker build -t sld-frontend ./frontend
          docker build -t sld-backend ./backend
      
      - name: Push to Registry
        run: |
          docker tag sld-frontend registry.cn-shenzhen.aliyuncs.com/sld/frontend:latest
          docker tag sld-backend registry.cn-shenzhen.aliyuncs.com/sld/backend:latest
          docker push registry.cn-shenzhen.aliyuncs.com/sld/frontend:latest
          docker push registry.cn-shenzhen.aliyuncs.com/sld/backend:latest
      
      - name: Deploy to Server
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_KEY }}
          script: |
            cd /opt/sld-mall
            docker-compose pull
            docker-compose up -d
```

---

## ⚡ 性能优化

### 1. 数据库优化
```sql
-- 添加索引
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- 全文搜索索引
CREATE INDEX idx_products_fulltext ON products 
USING gin(to_tsvector('chinese', name || ' ' || description));

-- 分区表(订单表按月分区)
CREATE TABLE orders_2024_01 PARTITION OF orders
FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');
```

### 2. 缓存策略
```typescript
// 产品列表缓存 - 5分钟
redis.setex(`products:list:${category}:${page}`, 300, JSON.stringify(products));

// 产品详情缓存 - 1小时
redis.setex(`product:${id}`, 3600, JSON.stringify(product));

// 热门搜索缓存 - 10分钟
redis.setex('search:hot', 600, JSON.stringify(hotKeywords));
```

### 3. CDN加速
```
静态资源通过阿里云CDN加速:
- 图片: https://cdn.sld-mall.com/images/
- JS/CSS: https://cdn.sld-mall.com/assets/
- 3D模型: https://cdn.sld-mall.com/models/
```

---

## 🔒 安全策略

### 1. 数据加密
```typescript
// 密码加密
import bcrypt from 'bcrypt';
const hashedPassword = await bcrypt.hash(password, 10);

// JWT Token
import jwt from 'jsonwebtoken';
const token = jwt.sign({ userId, role }, JWT_SECRET, { expiresIn: '7d' });

// 敏感数据加密
import crypto from 'crypto';
const encrypted = crypto.createCipher('aes-256-cbc', SECRET_KEY);
```

### 2. SQL注入防护
```typescript
// 使用参数化查询
const result = await db.query(
  'SELECT * FROM products WHERE id = $1',
  [productId]
);
```

### 3. XSS防护
```typescript
// 输出转义
import DOMPurify from 'dompurify';
const clean = DOMPurify.sanitize(userInput);
```

### 4. CSRF防护
```typescript
// CSRF Token验证
app.use(csrf({ cookie: true }));
```

---

## 📊 监控与日志

### 应用监控
```javascript
// 使用PM2进行进程管理
{
  "apps": [{
    "name": "sld-backend",
    "script": "./dist/app.js",
    "instances": 4,
    "exec_mode": "cluster",
    "max_memory_restart": "500M",
    "env": {
      "NODE_ENV": "production"
    }
  }]
}
```

### 日志系统
```typescript
// Winston日志配置
import winston from 'winston';

const logger = winston.createLogger({
  level: 'info',
  format: winston.format.json(),
  transports: [
    new winston.transports.File({ filename: 'error.log', level: 'error' }),
    new winston.transports.File({ filename: 'combined.log' })
  ]
});
```

---

## 📞 技术支持

- 文档: https://docs.sld-mall.com
- GitHub: https://github.com/sld/sld-mall
- 技术支持: tech@sld.com
