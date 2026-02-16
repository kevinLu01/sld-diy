#!/bin/bash
# 冒烟测试脚本

BASE_URL=${1:-http://localhost:9002}

echo "================================"
echo "冒烟测试"
echo "目标: $BASE_URL"
echo "================================"

# 测试健康检查
echo "1. 测试健康检查..."
curl -sf $BASE_URL/api/v1/health || { echo "❌ 健康检查失败"; exit 1; }
echo "✅ 健康检查通过"

# 测试详细健康检查
echo "2. 测试详细健康检查..."
curl -sf $BASE_URL/api/v1/health/details || { echo "❌ 详细健康检查失败"; exit 1; }
echo "✅ 详细健康检查通过"

# 测试产品列表
echo "3. 测试产品列表..."
curl -sf "$BASE_URL/api/v1/products?page=1&limit=10" || { echo "❌ 产品列表失败"; exit 1; }
echo "✅ 产品列表通过"

# 测试分类列表
echo "4. 测试分类列表..."
curl -sf "$BASE_URL/api/v1/categories" || { echo "❌ 分类列表失败"; exit 1; }
echo "✅ 分类列表通过"

# 测试解决方案列表
echo "5. 测试解决方案列表..."
curl -sf "$BASE_URL/api/v1/solutions?page=1&limit=10" || { echo "❌ 解决方案列表失败"; exit 1; }
echo "✅ 解决方案列表通过"

# 测试知识库列表
echo "6. 测试知识库列表..."
curl -sf "$BASE_URL/api/v1/knowledge?page=1&limit=10" || { echo "❌ 知识库列表失败"; exit 1; }
echo "✅ 知识库列表通过"

echo "================================"
echo "✅ 所有冒烟测试通过"
echo "================================"
