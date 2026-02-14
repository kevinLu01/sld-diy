# API接口设计文档

## 基础URL
```
生产环境: https://api.sld-mall.com/v1
开发环境: http://localhost:3000/api/v1
```

## 认证方式
使用JWT (JSON Web Token)进行身份认证
请求头: `Authorization: Bearer <token>`

---

## 1. 用户相关API

### 1.1 用户注册
```
POST /auth/register
```

**请求参数:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "phone": "string",
  "userType": "personal|business"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 1,
    "username": "test_user",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 1.2 用户登录
```
POST /auth/login
```

### 1.3 获取用户信息
```
GET /users/profile
```

### 1.4 企业认证
```
POST /users/business-verify
```

---

## 2. 产品相关API

### 2.1 获取产品分类
```
GET /categories
```

**响应:**
```json
{
  "code": 200,
  "data": [
    {
      "id": 1,
      "name": "压缩机",
      "slug": "compressors",
      "icon": "fas fa-cog",
      "count": 2000,
      "children": [
        {
          "id": 11,
          "name": "涡旋压缩机",
          "slug": "scroll-compressors",
          "count": 800
        }
      ]
    }
  ]
}
```

### 2.2 获取产品列表
```
GET /products?category=compressors&page=1&limit=20&sort=sales
```

**查询参数:**
- `category`: 分类slug
- `page`: 页码 (默认1)
- `limit`: 每页数量 (默认20)
- `sort`: 排序方式 (sales/price/new)
- `brand`: 品牌筛选
- `priceMin`: 最低价格
- `priceMax`: 最高价格
- `search`: 搜索关键词

**响应:**
```json
{
  "code": 200,
  "data": {
    "total": 156,
    "page": 1,
    "limit": 20,
    "items": [
      {
        "id": 1,
        "sku": "PN-2P20S",
        "name": "松下 2P20S 涡旋压缩机",
        "brand": "松下",
        "price": 3680.00,
        "originalPrice": 4200.00,
        "stock": 50,
        "images": ["url1", "url2"],
        "specifications": {
          "power": "2HP",
          "cooling_capacity": "5.5kW",
          "refrigerant": "R404A"
        },
        "salesCount": 1250,
        "rating": 4.8
      }
    ]
  }
}
```

### 2.3 获取产品详情
```
GET /products/:id
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "sku": "PN-2P20S",
    "name": "松下 2P20S 涡旋压缩机",
    "brand": "松下",
    "category": {
      "id": 11,
      "name": "涡旋压缩机"
    },
    "price": 3680.00,
    "stock": 50,
    "description": "详细描述...",
    "images": ["url1", "url2", "url3"],
    "video": "video_url",
    "model3d": "model_url",
    "specifications": {
      "power": "2HP",
      "cooling_capacity": "5.5kW",
      "voltage": "380V/3Ph/50Hz",
      "refrigerant": "R404A",
      "weight": "45kg"
    },
    "attributes": [
      {
        "name": "功率",
        "value": "2",
        "unit": "HP"
      }
    ],
    "compatibleProducts": [
      {
        "id": 10,
        "name": "风冷翅片式冷凝器 FNH-60",
        "price": 1580.00,
        "compatibilityType": "recommended"
      }
    ],
    "reviews": {
      "average": 4.8,
      "count": 156,
      "distribution": {
        "5": 120,
        "4": 25,
        "3": 8,
        "2": 2,
        "1": 1
      }
    }
  }
}
```

### 2.4 获取产品兼容性
```
GET /products/:id/compatibility
```

---

## 3. DIY配套系统API

### 3.1 智能推荐配件
```
POST /diy/recommend
```

**请求参数:**
```json
{
  "scenario": "cold_storage",
  "temperatureRange": "-5~0",
  "coolingCapacity": 50,
  "capacityUnit": "kW",
  "volume": 100,
  "volumeUnit": "m3",
  "ambientTemp": "30~40",
  "options": {
    "energySaving": true,
    "lowNoise": true,
    "smartControl": false,
    "remoteMonitoring": false
  }
}
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "recommendationId": "rec_123456",
    "scenario": "cold_storage",
    "requirements": {
      "temperatureRange": "-5~0",
      "coolingCapacity": 50,
      "unit": "kW"
    },
    "products": {
      "compressor": [
        {
          "id": 1,
          "name": "松下 2P20S 涡旋压缩机",
          "price": 3680.00,
          "matchScore": 95,
          "matchReason": "最佳匹配",
          "quantity": 1,
          "specifications": {...}
        }
      ],
      "condenser": [...],
      "evaporator": [...],
      "controller": [...],
      "accessories": [...]
    },
    "totalPrice": 15680.00,
    "estimatedInstallationFee": 800.00,
    "energyEfficiency": "A++",
    "estimatedPowerConsumption": "35kW/day",
    "suggestions": [
      "建议配套电子膨胀阀以提高能效比",
      "推荐使用智能控制系统实现远程监控"
    ]
  }
}
```

### 3.2 验证配件兼容性
```
POST /diy/validate-compatibility
```

**请求参数:**
```json
{
  "productIds": [1, 10, 25, 38]
}
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "compatible": true,
    "warnings": [
      {
        "type": "performance",
        "message": "冷凝器换热面积略小,建议在高温环境下增加风扇数量"
      }
    ],
    "errors": [],
    "compatibilityMatrix": [
      {
        "productA": 1,
        "productB": 10,
        "status": "compatible",
        "note": "完美匹配"
      }
    ]
  }
}
```

### 3.3 保存DIY方案
```
POST /diy/projects
```

**请求参数:**
```json
{
  "projectName": "我的冷库方案",
  "scenario": "cold_storage",
  "requirements": {...},
  "selectedProducts": [
    {
      "productId": 1,
      "quantity": 1,
      "notes": ""
    }
  ],
  "shared": false
}
```

### 3.4 获取用户DIY方案列表
```
GET /diy/projects?page=1&limit=10
```

### 3.5 获取DIY方案详情
```
GET /diy/projects/:id
```

### 3.6 分享DIY方案
```
POST /diy/projects/:id/share
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "shareUrl": "https://sld-mall.com/diy/share/abc123",
    "shareToken": "abc123",
    "qrCode": "data:image/png;base64,..."
  }
}
```

---

## 4. 解决方案API

### 4.1 获取行业解决方案列表
```
GET /solutions?industry=retail&page=1&limit=10
```

**查询参数:**
- `industry`: 行业分类 (retail/warehouse/industrial/medical)
- `scenario`: 应用场景
- `temperatureRange`: 温度范围
- `sort`: 排序 (popular/new)

**响应:**
```json
{
  "code": 200,
  "data": {
    "total": 45,
    "items": [
      {
        "id": 1,
        "title": "商超冷柜制冷系统方案",
        "industry": "retail",
        "scenario": "supermarket_freezer",
        "coverImage": "image_url",
        "temperatureRange": "-5~0°C",
        "capacityRange": "20-100m³",
        "features": [
          "节能30%",
          "低噪音运行",
          "智能温控"
        ],
        "totalPrice": 15680.00,
        "usageCount": 1200,
        "viewCount": 5600,
        "rating": 4.9
      }
    ]
  }
}
```

### 4.2 获取解决方案详情
```
GET /solutions/:id
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "id": 1,
    "title": "商超冷柜制冷系统方案",
    "industry": "retail",
    "scenario": "supermarket_freezer",
    "description": "详细描述...",
    "coverImage": "image_url",
    "images": ["url1", "url2"],
    "temperatureRange": "-5~0°C",
    "capacityRange": "20-100m³",
    "features": [...],
    "products": [
      {
        "productId": 1,
        "product": {...},
        "quantity": 1,
        "notes": "主压缩机"
      }
    ],
    "totalPrice": 15680.00,
    "installationGuide": "安装指导文档...",
    "technicalDocs": [
      {
        "name": "系统原理图",
        "url": "pdf_url"
      }
    ],
    "cases": [
      {
        "id": 1,
        "projectName": "华润万家深圳门店",
        "clientName": "华润万家",
        "location": "深圳市",
        "completionDate": "2024-03",
        "images": [...],
        "results": "节能35%,温控精度±0.5°C"
      }
    ]
  }
}
```

### 4.3 基于解决方案创建订单
```
POST /solutions/:id/create-order
```

---

## 5. 订单相关API

### 5.1 创建订单
```
POST /orders
```

**请求参数:**
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 1,
      "price": 3680.00
    }
  ],
  "diyProjectId": 123,
  "shippingAddress": {
    "recipient": "张三",
    "phone": "13800138000",
    "province": "广东省",
    "city": "东莞市",
    "district": "虎门镇",
    "address": "连升路XXX号",
    "postalCode": "523900"
  },
  "notes": "请尽快发货",
  "needInstallation": true
}
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "orderId": 1001,
    "orderNo": "SLD20240201001",
    "totalAmount": 15680.00,
    "shippingFee": 0,
    "installationFee": 800.00,
    "finalAmount": 16480.00,
    "paymentUrl": "https://pay.sld-mall.com/..."
  }
}
```

### 5.2 获取订单列表
```
GET /orders?status=pending&page=1&limit=10
```

### 5.3 获取订单详情
```
GET /orders/:orderNo
```

### 5.4 取消订单
```
POST /orders/:orderNo/cancel
```

---

## 6. 服务相关API

### 6.1 创建服务请求
```
POST /services/requests
```

**请求参数:**
```json
{
  "serviceType": "installation|maintenance|consultation|repair",
  "orderId": 1001,
  "description": "需要安装冷库制冷系统",
  "contactInfo": {
    "name": "张三",
    "phone": "13800138000",
    "address": "广东省东莞市..."
  },
  "scheduledTime": "2024-02-15 09:00:00",
  "priority": "normal"
}
```

### 6.2 获取服务请求列表
```
GET /services/requests?status=pending&page=1
```

### 6.3 获取服务请求详情
```
GET /services/requests/:requestNo
```

### 6.4 评价服务
```
POST /services/requests/:requestNo/rate
```

**请求参数:**
```json
{
  "rating": 5,
  "feedback": "服务非常专业,安装师傅很细心"
}
```

---

## 7. 知识库API

### 7.1 搜索知识库
```
GET /knowledge?q=压缩机&category=installation&page=1
```

### 7.2 获取知识文章
```
GET /knowledge/:id
```

### 7.3 标记文章有用
```
POST /knowledge/:id/helpful
```

---

## 8. 搜索API

### 8.1 全局搜索
```
GET /search?q=松下压缩机&type=products,solutions&page=1
```

**响应:**
```json
{
  "code": 200,
  "data": {
    "products": {
      "total": 25,
      "items": [...]
    },
    "solutions": {
      "total": 5,
      "items": [...]
    },
    "knowledge": {
      "total": 12,
      "items": [...]
    }
  }
}
```

### 8.2 智能搜索建议
```
GET /search/suggestions?q=松下
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 422 | 验证失败 |
| 500 | 服务器错误 |
| 1001 | 用户名已存在 |
| 1002 | 邮箱已注册 |
| 2001 | 库存不足 |
| 2002 | 产品已下架 |
| 3001 | 配件不兼容 |
| 4001 | 订单不存在 |
| 4002 | 订单状态不允许此操作 |

## 通用响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {...}
}
```

错误响应:
```json
{
  "code": 400,
  "message": "请求参数错误",
  "errors": {
    "email": ["邮箱格式不正确"],
    "password": ["密码长度至少6位"]
  }
}
```
