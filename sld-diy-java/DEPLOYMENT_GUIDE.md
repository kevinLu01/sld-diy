# SLD DIY Java 后端 - 线上部署与切换流程

## 部署架构

```
                    ┌─────────────┐
                    │   Nginx     │
                    │   :9000     │
                    └──────┬──────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
    ┌──────▼──────┐ ┌──────▼──────┐ ┌──────▼──────┐
    │  Node.js    │ │    Java     │ │    Java     │
    │  Backend    │ │  Backend    │ │  Backend    │
    │   :9001     │ │   :9002     │ │   :9002     │
    │   (旧版)     │ │  (新版 v1)  │ │  (新版 v2)  │
    └─────────────┘ └─────────────┘ └─────────────┘
                           │
           ┌───────────────┴───────────────┐
           │                               │
    ┌──────▼──────┐                ┌──────▼──────┐
    │    MySQL    │                │    Redis    │
    │    :3306    │                │    :6379    │
    └─────────────┘                └─────────────┘
```

---

## 第一阶段：预发布环境部署

### 1.1 准备预发布环境

```bash
# 1. 进入项目目录
cd /opt/sld-diy

# 2. 拉取最新代码
git pull origin main

# 3. 切换到预发布分支
git checkout develop

# 4. 更新子模块 (Java 后端)
cd sld-diy-java
git pull origin develop
```

### 1.2 构建并部署

```bash
# 1. 构建 Docker 镜像
docker build -t sld-diy-java:staging .

# 2. 标记镜像
docker tag sld-diy-java:staging registry.example.com/sld-diy-java:staging

# 3. 推送镜像到仓库 (可选)
docker push registry.example.com/sld-diy-java:staging

# 4. 使用 docker-compose 部署预发布环境
docker-compose -f docker-compose.staging.yml up -d backend-java

# 5. 查看日志
docker-compose -f docker-compose.staging.yml logs -f backend-java
```

### 1.3 预发布环境验证

```bash
# 1. 健康检查
curl http://staging-api.example.com:9002/api/v1/health

# 2. 运行自动化测试
./run-integration-tests.sh staging

# 3. 手动验证关键接口
# - 用户注册/登录
# - 产品列表/详情
# - 下单流程
# - DIY 推荐
```

---

## 第二阶段：生产环境部署（蓝绿部署）

### 2.1 部署绿色环境（新版）

```bash
#!/bin/bash
# deploy-green.sh

set -e

echo "=== 开始部署绿色环境 ==="

# 1. 拉取最新镜像
docker pull registry.example.com/sld-diy-java:${VERSION}

# 2. 停止并移除旧的绿色容器 (如果存在)
docker-compose stop backend-java-green || true
docker-compose rm -f backend-java-green || true

# 3. 启动绿色环境
docker-compose -f docker-compose.prod.yml up -d backend-java-green

# 4. 等待服务就绪
echo "等待服务就绪..."
sleep 30

# 5. 健康检查
for i in {1..10}; do
    if curl -f http://localhost:9003/api/v1/health; then
        echo "绿色环境健康检查通过"
        break
    fi
    echo "等待健康检查... ($i/10)"
    sleep 5
done

# 6. 运行冒烟测试
./smoke-tests.sh localhost:9003

echo "=== 绿色环境部署完成 ==="
```

### 2.2 切换流量（蓝切绿）

```bash
#!/bin/bash
# switch-to-green.sh

echo "=== 开始切换流量到绿色环境 ==="

# 1. 修改 Nginx 配置 upstream
cat > /etc/nginx/conf.d/upstream.conf << EOF
upstream backend {
    server localhost:9003;  # 绿色环境 (Java 新版)
    # server localhost:9001;  # 蓝色环境 (Node.js 旧版) - 已注释
}
EOF

# 2. 检查 Nginx 配置
nginx -t

# 3. 平滑重载 Nginx
nginx -s reload

echo "=== 流量切换完成 ==="
```

### 2.3 监控验证期（建议 30 分钟）

```bash
#!/bin/bash
# monitor.sh

echo "=== 开始监控验证 ==="

# 监控指标
for i in {1..30}; do
    echo "=== 第 $i 分钟 ==="
    
    # 1. 错误率检查
    ERROR_RATE=$(curl -s "http://localhost:9002/actuator/metrics/http.server.requests?tag=status:5xx" | jq '.measurements[0].value')
    echo "错误率: $ERROR_RATE"
    
    # 2. 响应时间检查
    RESPONSE_TIME=$(curl -s "http://localhost:9002/actuator/metrics/http.server.requests" | jq '.measurements[0].value')
    echo "平均响应时间: $RESPONSE_TIME ms"
    
    # 3. JVM 内存
    MEMORY_USED=$(curl -s http://localhost:9002/actuator/metrics/jvm.memory.used | jq '.measurements[0].value')
    echo "JVM 内存使用: $MEMORY_USED bytes"
    
    # 4. 检查错误日志
    ERROR_COUNT=$(docker-compose logs --since 1m backend-java-green | grep -i error | wc -l)
    echo "最近1分钟错误数: $ERROR_COUNT"
    
    # 判断是否需要回滚
    if (( $(echo "$ERROR_RATE > 0.01" | bc -l) )) || [ $ERROR_COUNT -gt 10 ]; then
        echo "⚠️ 异常指标 detected，建议回滚!"
        echo "执行 ./rollback.sh 进行回滚"
    fi
    
    sleep 60
done

echo "=== 监控验证完成 ==="
```

---

## 第三阶段：回滚流程

### 3.1 快速回滚

```bash
#!/bin/bash
# rollback.sh

echo "=== 开始回滚 ==="

# 1. 切换回旧版本 (Node.js)
cat > /etc/nginx/conf.d/upstream.conf << EOF
upstream backend {
    server localhost:9001;  # 蓝色环境 (Node.js 旧版)
    # server localhost:9003;  # 绿色环境 (Java 新版) - 已注释
}
EOF

# 2. 重载 Nginx
nginx -t && nginx -s reload

echo "✅ 已切换回 Node.js 旧版"

# 3. 停止 Java 服务 (可选，保留用于排查问题)
docker-compose stop backend-java-green

# 4. 发送告警
curl -X POST "https://alert.example.com/webhook" \
  -H "Content-Type: application/json" \
  -d '{"message": "Java后端回滚完成", "severity": "warning"}'

echo "=== 回滚完成 ==="
```

### 3.2 数据一致性检查

```bash
#!/bin/bash
# data-consistency-check.sh

echo "=== 数据一致性检查 ==="

# 1. 检查订单数据
MYSQL_CONTAINER=$(docker ps -q -f name=mysql)

# 最近1小时订单数量
ORDER_COUNT=$(docker exec $MYSQL_CONTAINER mysql -u root -p${MYSQL_PASSWORD} -e "SELECT COUNT(*) FROM sld_diy.t_order WHERE create_time > DATE_SUB(NOW(), INTERVAL 1 HOUR)" | tail -1)
echo "最近1小时订单数: $ORDER_COUNT"

# 检查异常订单
ABNORMAL_ORDERS=$(docker exec $MYSQL_CONTAINER mysql -u root -p${MYSQL_PASSWORD} -e "SELECT COUNT(*) FROM sld_diy.t_order WHERE status IS NULL OR total_amount < 0" | tail -1)
echo "异常订单数: $ABNORMAL_ORDERS"

# 2. 检查用户数据
USER_COUNT=$(docker exec $MYSQL_CONTAINER mysql -u root -p${MYSQL_PASSWORD} -e "SELECT COUNT(*) FROM sld_diy.t_user WHERE create_time > DATE_SUB(NOW(), INTERVAL 1 HOUR)" | tail -1)
echo "最近1小时新用户: $USER_COUNT"

# 3. 对比缓存和数据库
# ...

echo "=== 数据一致性检查完成 ==="
```

---

## 第四阶段：完全切换

### 4.1 确认稳定后下线旧版

```bash
#!/bin/bash
# finalize-deployment.sh

echo "=== 完成部署 ==="

# 1. 确认稳定运行 24 小时后再执行
read -p "确认 Java 服务已稳定运行 24 小时? (yes/no): " confirm
if [ "$confirm" != "yes" ]; then
    echo "取消操作"
    exit 1
fi

# 2. 停止 Node.js 服务
docker-compose stop backend
docker-compose rm -f backend

# 3. 清理旧镜像
docker image prune -f

# 4. 更新监控告警配置
# 更新 Prometheus targets
# 更新 Grafana dashboard

echo "=== 部署完成 ==="
```

---

## 配置文件模板

### docker-compose.prod.yml

```yaml
version: '3.8'

services:
  # 生产环境 Java 服务 (绿色)
  backend-java-green:
    image: registry.example.com/sld-diy-java:${VERSION:-latest}
    container_name: sld-backend-java-green
    restart: always
    ports:
      - "9003:9002"  # 绿色环境端口
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - MYSQL_HOST=mysql
      - MYSQL_PORT=3306
      - MYSQL_DATABASE=sld_diy
      - MYSQL_USERNAME=${MYSQL_USERNAME}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - REDIS_PASSWORD=${REDIS_PASSWORD}
      - JWT_SECRET=${JWT_SECRET}
      - JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC
    volumes:
      - /data/logs/java:/app/logs
    networks:
      - sld-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9002/api/v1/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 60s
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 3G
        reservations:
          cpus: '1'
          memory: 1G

  # Node.js 旧版 (蓝色) - 保留用于回滚
  backend:
    image: sld-diy-nodejs:stable
    container_name: sld-backend-node
    restart: always
    ports:
      - "9001:9001"
    environment:
      - DATABASE_URL=mysql://${MYSQL_USERNAME}:${MYSQL_PASSWORD}@mysql:3306/sld_diy
      - JWT_SECRET=${JWT_SECRET}
      - REDIS_URL=redis://redis:6379
    networks:
      - sld-network
    profiles: ["legacy"]  # 默认不启动

  nginx:
    image: nginx:alpine
    container_name: sld-nginx
    restart: always
    ports:
      - "9000:80"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/conf.d:/etc/nginx/conf.d:ro
      - ./frontend/dist:/usr/share/nginx/html:ro
    depends_on:
      - backend-java-green
    networks:
      - sld-network

networks:
  sld-network:
    external: true
```

### Nginx 配置

```nginx
# /etc/nginx/conf.d/upstream.conf
upstream backend {
    # 切换时修改这里的配置
    server localhost:9003;  # Java 新版
    # server localhost:9001 backup;  # Node.js 旧版 (backup)
}

server {
    listen 80;
    server_name api.example.com;

    location /api/v1/ {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        
        # 超时配置
        proxy_connect_timeout 5s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
        
        # 健康检查
        proxy_next_upstream error timeout http_502 http_503;
    }
    
    # 健康检查端点
    location /health {
        access_log off;
        return 200 "healthy\n";
        add_header Content-Type text/plain;
    }
}
```

---

## 一键部署脚本

```bash
#!/bin/bash
# deploy-production.sh

VERSION=${1:-latest}
ENVIRONMENT=${2:-staging}

echo "================================"
echo "SLD DIY Java 后端部署脚本"
echo "版本: $VERSION"
echo "环境: $ENVIRONMENT"
echo "================================"

# 预发布环境
if [ "$ENVIRONMENT" == "staging" ]; then
    ./scripts/deploy-staging.sh $VERSION
    exit 0
fi

# 生产环境
if [ "$ENVIRONMENT" == "production" ]; then
    echo "⚠️ 即将部署到生产环境!"
    read -p "输入 'deploy' 确认: " confirm
    if [ "$confirm" != "deploy" ]; then
        echo "取消部署"
        exit 1
    fi
    
    ./scripts/deploy-green.sh $VERSION
    ./scripts/switch-to-green.sh
    ./scripts/monitor.sh &
    
    echo "部署完成，请观察监控数据"
    exit 0
fi

echo "未知环境: $ENVIRONMENT"
exit 1
```

---

## 部署时间线

| 阶段 | 时间 | 操作 | 负责人 |
|------|------|------|--------|
| 预发布 | D-1 | 预发布环境部署验证 | Dev |
| 蓝绿部署 | D-Day 00:00 | 部署绿色环境 | Ops |
| 健康检查 | D-Day 00:10 | 验证绿色环境 | Dev |
| 流量切换 | D-Day 00:30 | Nginx 切流量 | Ops |
| 监控观察 | D-Day 00:35-01:05 | 监控验证 | DevOps |
| 确认稳定 | D-Day 01:05 | 确认是否回滚 | Team |
| 下线旧版 | D+1 | 停止 Node.js 服务 | Ops |

---

## 紧急联系

| 角色 | 姓名 | 电话 |
|------|------|------|
| Tech Lead | XXX | 138-xxxx-xxxx |
| DevOps | XXX | 139-xxxx-xxxx |
| 产品经理 | XXX | 137-xxxx-xxxx |
