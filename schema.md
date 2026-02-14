# 数据库设计文档

## 核心数据表

### 1. 用户相关

#### users (用户表)
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    user_type VARCHAR(20) NOT NULL, -- 'personal', 'business'
    status VARCHAR(20) DEFAULT 'active', -- 'active', 'suspended', 'deleted'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### business_info (企业信息表)
```sql
CREATE TABLE business_info (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    company_name VARCHAR(200) NOT NULL,
    business_license VARCHAR(50),
    industry VARCHAR(100),
    address TEXT,
    contact_person VARCHAR(50),
    verified BOOLEAN DEFAULT false,
    verified_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. 产品相关

#### categories (分类表)
```sql
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id INTEGER REFERENCES categories(id),
    slug VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(100),
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### products (产品表)
```sql
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    category_id INTEGER REFERENCES categories(id),
    description TEXT,
    specifications JSONB, -- 规格参数 JSON
    price DECIMAL(10, 2) NOT NULL,
    cost_price DECIMAL(10, 2),
    stock_quantity INTEGER DEFAULT 0,
    unit VARCHAR(20) DEFAULT '件',
    images JSONB, -- 图片数组
    video_url VARCHAR(255),
    model_3d_url VARCHAR(255), -- 3D模型链接
    status VARCHAR(20) DEFAULT 'active',
    view_count INTEGER DEFAULT 0,
    sales_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### product_attributes (产品属性表)
```sql
CREATE TABLE product_attributes (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES products(id),
    attribute_name VARCHAR(100) NOT NULL,
    attribute_value TEXT NOT NULL,
    unit VARCHAR(20),
    sort_order INTEGER DEFAULT 0
);
```

#### compatibility_rules (兼容性规则表)
```sql
CREATE TABLE compatibility_rules (
    id SERIAL PRIMARY KEY,
    product_a_id INTEGER REFERENCES products(id),
    product_b_id INTEGER REFERENCES products(id),
    compatibility_type VARCHAR(20), -- 'compatible', 'recommended', 'incompatible'
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. 解决方案相关

#### solutions (解决方案表)
```sql
CREATE TABLE solutions (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    industry VARCHAR(100), -- 行业分类
    scenario VARCHAR(200), -- 应用场景
    description TEXT,
    cover_image VARCHAR(255),
    temperature_range VARCHAR(50), -- 温度范围
    capacity_range VARCHAR(50), -- 容量范围
    features JSONB, -- 方案特点
    products JSONB, -- 产品清单 [{product_id, quantity, notes}]
    total_price DECIMAL(10, 2),
    installation_guide TEXT,
    view_count INTEGER DEFAULT 0,
    usage_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'published',
    created_by INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### solution_cases (案例表)
```sql
CREATE TABLE solution_cases (
    id SERIAL PRIMARY KEY,
    solution_id INTEGER REFERENCES solutions(id),
    project_name VARCHAR(200) NOT NULL,
    client_name VARCHAR(100),
    location VARCHAR(200),
    completion_date DATE,
    description TEXT,
    images JSONB,
    results TEXT, -- 实施效果
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4. DIY配套相关

#### diy_projects (DIY项目表)
```sql
CREATE TABLE diy_projects (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    project_name VARCHAR(200) NOT NULL,
    scenario VARCHAR(100), -- 应用场景
    requirements JSONB, -- 需求参数
    selected_products JSONB, -- 选择的产品
    total_price DECIMAL(10, 2),
    status VARCHAR(20) DEFAULT 'draft', -- 'draft', 'saved', 'ordered'
    shared BOOLEAN DEFAULT false,
    share_token VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5. 订单相关

#### orders (订单表)
```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id INTEGER REFERENCES users(id),
    diy_project_id INTEGER REFERENCES diy_projects(id),
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2) DEFAULT 0,
    shipping_fee DECIMAL(10, 2) DEFAULT 0,
    final_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending', -- 'pending', 'paid', 'processing', 'shipped', 'completed', 'cancelled'
    payment_method VARCHAR(50),
    payment_time TIMESTAMP,
    shipping_address JSONB,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### order_items (订单明细表)
```sql
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(id),
    product_id INTEGER REFERENCES products(id),
    product_name VARCHAR(200),
    product_sku VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 6. 服务相关

#### service_requests (服务请求表)
```sql
CREATE TABLE service_requests (
    id SERIAL PRIMARY KEY,
    request_no VARCHAR(50) UNIQUE NOT NULL,
    user_id INTEGER REFERENCES users(id),
    order_id INTEGER REFERENCES orders(id),
    service_type VARCHAR(50), -- 'installation', 'maintenance', 'consultation', 'repair'
    priority VARCHAR(20) DEFAULT 'normal', -- 'low', 'normal', 'high', 'urgent'
    description TEXT,
    contact_info JSONB,
    scheduled_time TIMESTAMP,
    assigned_to INTEGER,
    status VARCHAR(20) DEFAULT 'pending', -- 'pending', 'assigned', 'in_progress', 'completed', 'cancelled'
    resolution TEXT,
    rating INTEGER, -- 1-5星评价
    feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### knowledge_base (知识库表)
```sql
CREATE TABLE knowledge_base (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(100),
    content TEXT,
    tags JSONB,
    attachments JSONB,
    view_count INTEGER DEFAULT 0,
    helpful_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'published',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 索引设计

```sql
-- 产品表索引
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_brand ON products(brand);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_sku ON products(sku);

-- 订单表索引
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created ON orders(created_at DESC);

-- 兼容性规则索引
CREATE INDEX idx_compat_product_a ON compatibility_rules(product_a_id);
CREATE INDEX idx_compat_product_b ON compatibility_rules(product_b_id);

-- 全文搜索索引
CREATE INDEX idx_products_search ON products USING gin(to_tsvector('chinese', name || ' ' || description));
CREATE INDEX idx_solutions_search ON solutions USING gin(to_tsvector('chinese', title || ' ' || description));
```

## 数据初始化

详见各个 `seed_*.sql` 文件
