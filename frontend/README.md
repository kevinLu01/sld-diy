# 生利达冷冻空调配件DIY商城 - 前端项目

基于 React 18 + TypeScript + Vite + Ant Design 构建的现代化电商前端应用。

## 🚀 技术栈

- **框架**: React 18
- **语言**: TypeScript
- **构建工具**: Vite
- **UI组件库**: Ant Design 5.x
- **路由**: React Router v6
- **状态管理**: Zustand
- **数据请求**: Axios + React Query
- **样式**: CSS + Ant Design

## 📦 项目结构

```
frontend/
├── src/
│   ├── components/      # 公共组件
│   │   ├── Header.tsx   # 顶部导航
│   │   ├── Footer.tsx   # 底部信息
│   │   └── Layout.tsx   # 主布局
│   ├── pages/           # 页面组件
│   │   ├── Home.tsx     # 首页
│   │   ├── Products.tsx # 产品列表
│   │   └── Login.tsx    # 登录注册
│   ├── services/        # API服务
│   │   ├── api.ts       # Axios配置
│   │   ├── auth.ts      # 认证服务
│   │   ├── product.ts   # 产品服务
│   │   ├── diy.ts       # DIY服务
│   │   └── order.ts     # 订单服务
│   ├── store/           # 状态管理
│   │   ├── user.ts      # 用户状态
│   │   └── cart.ts      # 购物车状态
│   ├── types/           # TypeScript类型定义
│   │   └── index.ts
│   ├── utils/           # 工具函数
│   ├── App.tsx          # 根组件
│   ├── main.tsx         # 入口文件
│   └── index.css        # 全局样式
├── package.json
├── vite.config.ts
├── tsconfig.json
└── README.md
```

## 🛠️ 开发指南

### 安装依赖

```bash
cd frontend
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:5173

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 🔧 环境配置

复制 `.env.example` 为 `.env` 并修改配置：

```bash
cp .env.example .env
```

## 📋 核心功能

### 已实现 ✅

- [x] 用户认证系统（登录/注册）
- [x] 产品浏览与搜索
- [x] 购物车管理
- [x] 响应式布局
- [x] TypeScript类型安全
- [x] API请求封装
- [x] 状态管理

### 开发中 🚧

- [ ] DIY配套工具页面
- [ ] 产品详情页
- [ ] 订单管理
- [ ] 用户中心
- [ ] 解决方案中心
- [ ] 知识库
- [ ] 管理后台

## 🎨 代码规范

项目使用 ESLint 和 Prettier 进行代码规范：

```bash
# 代码检查
npm run lint

# 代码格式化
npm run format
```

## 📝 API文档

后端API地址: `http://localhost:3001/api/v1`

详细API文档请参考项目根目录的 `API.md`

## 🔐 认证机制

使用 JWT Token 进行身份认证：
- Token存储在 localStorage
- 请求拦截器自动添加 Authorization header
- 401响应自动跳转登录页

## 📦 状态管理

使用 Zustand 进行轻量级状态管理：
- `useUserStore`: 用户信息和认证状态
- `useCartStore`: 购物车状态

## 🌐 路由结构

```
/                  - 首页
/products          - 产品列表
/products/:id      - 产品详情
/diy               - DIY配套工具
/solutions         - 解决方案
/knowledge         - 知识库
/cart              - 购物车
/user/profile      - 个人中心
/user/orders       - 我的订单
/login             - 登录注册
```

## 🚀 部署

### 使用 Nginx

1. 构建项目：
```bash
npm run build
```

2. 将 `dist` 目录部署到 Nginx：
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    root /path/to/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:3001;
    }
}
```

## 📄 License

Copyright © 2024 生利达冷冻设备有限公司

## 👥 贡献

欢迎提交 Issue 和 Pull Request！
