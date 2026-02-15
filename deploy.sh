#!/bin/bash
# SLD-DIY 商城完整部署脚本（包含项目克隆）

set -e

echo "🚀 SLD-DIY 商城完整部署"
echo "========================"

# 1. 添加GitHub Actions SSH公钥（用于自动部署）
echo "📝 步骤1: 配置SSH公钥..."
mkdir -p ~/.ssh
chmod 700 ~/.ssh

# 检查公钥是否已存在
if ! grep -q "github-actions-deploy" ~/.ssh/authorized_keys 2>/dev/null; then
    cat >> ~/.ssh/authorized_keys << 'PUBKEY'
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAACAQDKm+76996gcYvXtVTWQtGPZqAebZf+fFdw0jhLUfWw4mBAdMXGVR9T97UBgzvz+5o6IxV+7QLVZOTpSJ+Xbuve+b5efwymkt1exUJuMLLqyAn4kic7wZYmwC203I3n7R8vVjevYc5aqJhG1DmCkYYR4ZpaVIxu/R9W5zag6kzqM4CuRBUVYJA30ynPOYX6qPTJ6ygbZtBdNv+LtRKAsJV8aujYL+4TUwsq6NpAzIEV6krXGhOaShcp5Qs/y3RNM1bAAxy4QzsKrJ41wU0yNu7jqHE7UEo3EfywKBLpcwtIdRC2XgS9fvh+fsX8OQ4BG4/CDEEe3a/gdrKZBVx+CgMjYUrJW3lQFSDBwjkww1qrUGFQz0311hVwNPc4ZN9XVlqzKc7e1m0JYYP1w5xM+gSqA7J4GL9i4g6j50oEoupifod0PpN7ke7jDZDNP/2ag0WG95CLAzCdJ9yonzLXvNvOf5tR/686gmM68SP6F52urb697yyTJDMLTXEzENzWorjqGbQtC38u+G+CVw3yxZMGkkzZKcZoR/Z9EYrxjqFBKvNC7YJpawu7PV8YXBK7F/hrj2p5bHgYMUUQXA7LuctkffwG9kt365U9KS4Sm601c0jIGNktJg2+2EZug0XQNzuSZQym2T9WULz9UEDMrc7aBwyl14wO0a2P9D/WI0t9CQ== github-actions-deploy
PUBKEY
    chmod 600 ~/.ssh/authorized_keys
    echo "✅ SSH公钥已添加"
else
    echo "✅ SSH公钥已存在"
fi

# 2. 更新系统
echo ""
echo "📦 步骤2: 更新系统..."
sudo apt update -qq

# 3. 安装Docker
echo ""
echo "🐳 步骤3: 安装Docker..."
if ! command -v docker &> /dev/null; then
    curl -fsSL https://get.docker.com | sudo sh
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker $USER
    echo "✅ Docker安装成功"
else
    echo "✅ Docker已安装: $(docker --version)"
fi

# 4. 安装Docker Compose
echo ""
echo "🐳 步骤4: 安装Docker Compose..."
if ! command -v docker-compose &> /dev/null; then
    sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    echo "✅ Docker Compose安装成功"
else
    echo "✅ Docker Compose已安装: $(docker-compose --version)"
fi

# 5. 安装Node.js (构建前端需要)
echo ""
echo "📦 步骤5: 安装Node.js..."
if ! command -v node &> /dev/null; then
    curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
    sudo apt-get install -y nodejs
    echo "✅ Node.js安装成功: $(node -v)"
else
    echo "✅ Node.js已安装: $(node -v)"
fi

# 6. 克隆或更新项目
echo ""
echo "📥 步骤6: 克隆项目..."
PROJECT_DIR="/var/www/sld-diy"

if [ -d "$PROJECT_DIR" ]; then
    echo "⚠️  项目目录已存在，更新代码..."
    cd "$PROJECT_DIR"
    sudo git pull origin main
else
    echo "📥 克隆项目..."
    sudo mkdir -p /var/www
    cd /var/www
    sudo git clone https://github.com/kevinLu01/sld-diy.git
    cd sld-diy
    echo "✅ 项目克隆成功"
fi

# 7. 设置目录权限
echo ""
echo "🔐 步骤7: 设置权限..."
sudo chown -R $USER:$USER "$PROJECT_DIR"
cd "$PROJECT_DIR"

# 8. 构建前端
echo ""
echo "🏗️  步骤8: 构建前端..."
cd frontend
npm install
npm run build
cd ..
echo "✅ 前端构建成功"

# 9. 创建环境变量文件
echo ""
echo "⚙️  步骤9: 配置环境变量..."
if [ ! -f .env ]; then
    cat > .env << 'ENVFILE'
MYSQL_ROOT_PASSWORD=sld_secure_password_$(openssl rand -hex 16)
JWT_SECRET=jwt_secret_$(openssl rand -hex 32)
ENVFILE
    echo "✅ 环境变量文件创建成功"
else
    echo "✅ 环境变量文件已存在"
fi

# 10. 启动Docker服务
echo ""
echo "🚀 步骤10: 启动Docker服务..."
docker-compose down 2>/dev/null || true
docker-compose up -d

# 11. 等待服务启动
echo ""
echo "⏳ 步骤11: 等待服务启动..."
sleep 30

# 12. 初始化数据库
echo ""
echo "💾 步骤12: 初始化数据库..."
docker-compose exec -T backend npx prisma db push || echo "⚠️  数据库初始化跳过（可能已存在）"

# 13. 检查服务状态
echo ""
echo "================================"
echo "✅ 部署完成！"
echo "================================"
echo ""
echo "📊 服务状态:"
docker-compose ps
echo ""
SERVER_IP=$(curl -s ifconfig.me 2>/dev/null || echo "124.156.182.246")
echo "🌐 访问地址: http://$SERVER_IP"
echo ""
echo "📝 常用命令:"
echo "  查看日志: cd /var/www/sld-diy && docker-compose logs -f"
echo "  重启服务: cd /var/www/sld-diy && docker-compose restart"
echo "  停止服务: cd /var/www/sld-diy && docker-compose down"
echo ""
echo "🤖 GitHub Actions自动部署已配置！"
echo "   以后只需 git push origin main 即可自动部署"
echo ""
