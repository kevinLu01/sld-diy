#!/bin/bash
# 快速重新部署前端脚本

set -e

echo "🚀 重新部署前端代码"
echo "===================="

# 1. 拉取最新代码
echo "📥 拉取最新代码..."
git pull origin main

# 2. 重新构建前端
echo "🏗️  重新构建前端..."
cd frontend
npm install
npm run build
cd ..

# 3. 重启nginx容器
echo "🔄 重启nginx容器..."
docker-compose restart nginx

# 4. 检查状态
echo ""
echo "✅ 部署完成！"
echo ""
echo "📊 服务状态:"
docker-compose ps | grep nginx

echo ""
echo "🌐 前端已更新,访问地址:"
SERVER_IP=$(curl -s ifconfig.me 2>/dev/null || hostname -I | awk '{print $1}')
echo "   http://$SERVER_IP:9000"
echo ""
echo "💡 提示: 清除浏览器缓存后访问 (Ctrl+Shift+R)"
