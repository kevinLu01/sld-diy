#!/bin/bash
# SLD-DIY 商城快速部署脚本

set -e

echo "🚀 SLD-DIY 商城快速部署"
echo "========================"

# 检查Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker未安装，正在安装..."
    curl -fsSL https://get.docker.com | sh
    systemctl start docker
    systemctl enable docker
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose未安装，正在安装..."
    curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
fi

# 构建前端
echo "🏗️  构建前端..."
cd frontend
npm install
npm run build
cd ..

# 创建环境变量文件
if [ ! -f .env ]; then
    echo "⚙️  创建环境变量文件..."
    cat > .env << 'ENVFILE'
MYSQL_ROOT_PASSWORD=sld_secure_password_$(openssl rand -hex 16)
JWT_SECRET=jwt_secret_$(openssl rand -hex 32)
ENVFILE
fi

# 启动服务
echo "🚀 启动服务..."
docker-compose up -d

# 等待服务就绪
echo "⏳ 等待服务启动..."
sleep 30

# 初始化数据库
echo "💾 初始化数据库..."
docker-compose exec backend npx prisma db push

# 检查服务状态
echo ""
echo "✅ 部署完成！"
echo "========================"
echo "📊 服务状态:"
docker-compose ps
echo ""
echo "🌐 访问地址: http://$(curl -s ifconfig.me)"
echo "📊 查看日志: docker-compose logs -f"
echo "🔄 重启服务: docker-compose restart"
echo "🛑 停止服务: docker-compose down"
