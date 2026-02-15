# 🚀 部署优化指南

## 📋 本次优化内容总结

### ✅ 已修复的问题

#### 1. **Dockerfile 优化**
- ✅ 使用多阶段构建，减小镜像体积
- ✅ 修复 `npm install --production` 导致 Prisma 无法生成的问题
- ✅ 添加 Docker 缓存层优化，加快构建速度
- ✅ 添加健康检查
- ✅ 使用非 root 用户运行（安全性提升）

#### 2. **docker-compose.yml 优化**
- ✅ 移除 MySQL 不支持的 `schema=public` 参数
- ✅ 添加 volume 挂载，代码更新后只需 `restart` 而不是 `rebuild`
- ✅ 添加健康检查配置

#### 3. **GitHub Actions 优化**
- ✅ 前端在服务器上构建（避免 SCP 认证问题）
- ✅ 部署时只需 `restart` 而不是 `rebuild`（节省时间）
- ✅ 添加数据库迁移步骤
- ✅ 修复健康检查 URL
- ✅ 自动拉取最新代码并重启服务

---

## 🎯 现在的部署流程

### **提交代码后自动部署**

```bash
# 1. 本地提交代码
git add .
git commit -m "feat: 新功能"
git push origin main

# 2. GitHub Actions 自动执行：
#    - 拉取最新代码到服务器
#    - 在服务器上构建前端
#    - 重启 backend 容器（自动加载新代码）
#    - 运行数据库迁移
#    - 健康检查

# 3. 完成！无需手动操作
```

### **关键改进**

| 操作 | 之前 | 现在 |
|------|------|------|
| 代码更新 | 需要 `rebuild` | 只需 `restart` ✅ |
| 前端构建 | 本地+服务器 | 只在服务器 ✅ |
| 部署时间 | ~5-10 分钟 | ~3-4 分钟 ✅ |
| 数据库迁移 | 手动执行 | 自动执行 ✅ |

---

## 🔧 首次部署（服务器上）

### **1. 拉取最新代码**

```bash
cd /var/www/sld-diy
git pull origin main
```

### **2. 重新构建镜像（只需一次）**

```bash
# 停止并删除旧容器
docker-compose down

# 重新构建（使用新的 Dockerfile）
docker-compose build --no-cache backend

# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f backend
```

### **3. 初始化数据库**

```bash
# 运行数据库迁移
docker-compose exec -T backend npx prisma migrate deploy

# 初始化测试数据
docker-compose exec -T backend npm run db:seed
```

---

## 📦 后续更新（自动化）

### **方式 1：推送代码自动部署（推荐）**

```bash
git add .
git commit -m "更新内容"
git push origin main
```

GitHub Actions 会自动部署！

### **方式 2：手动部署**

```bash
cd /var/www/sld-diy
git pull origin main
docker-compose restart backend
```

**注意**：现在只需 `restart`，不需要 `rebuild`！

---

## 🔍 常用命令

### **查看服务状态**

```bash
docker-compose ps
```

### **查看日志**

```bash
# 查看所有服务日志
docker-compose logs -f

# 只查看 backend 日志
docker-compose logs -f backend

# 查看最后 50 行
docker-compose logs --tail=50 backend
```

### **重启服务**

```bash
# 重启 backend
docker-compose restart backend

# 重启所有服务
docker-compose restart
```

### **执行数据库操作**

```bash
# 运行迁移
docker-compose exec -T backend npx prisma migrate deploy

# 初始化测试数据
docker-compose exec -T backend npm run db:seed
```

---

## 🏥 健康检查

### **检查服务是否正常**

```bash
# 检查前端
curl http://localhost:9000/

# 检查后端
curl http://localhost:3001/api/v1/health
```

### **Docker 健康检查**

```bash
# 查看健康状态
docker-compose ps

# 应该看到 (healthy) 标记
```

---

## 🐛 故障排查

### **问题 1：代码更新后没有生效**

```bash
# 重启容器
docker-compose restart backend

# 如果还不行，检查 volume 挂载
docker-compose exec backend ls -la /app/src
```

### **问题 2：数据库连接失败**

```bash
# 检查 MySQL 是否启动
docker-compose ps mysql

# 查看 MySQL 日志
docker-compose logs mysql

# 重启 MySQL
docker-compose restart mysql
```

### **问题 3：Prisma 错误**

```bash
# 重新生成 Prisma Client
docker-compose exec backend npx prisma generate

# 重启 backend
docker-compose restart backend
```

