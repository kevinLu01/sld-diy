#!/bin/bash
# Docker 构建脚本

VERSION=${1:-latest}
REGISTRY=${2:-}

echo "================================"
echo "构建 Docker 镜像"
echo "版本: $VERSION"
echo "================================"

# 构建镜像
docker build -t sld-diy-java:$VERSION .

# 标记镜像
if [ -n "$REGISTRY" ]; then
    echo "标记镜像: $REGISTRY/sld-diy-java:$VERSION"
    docker tag sld-diy-java:$VERSION $REGISTRY/sld-diy-java:$VERSION
    
    echo "推送镜像到仓库..."
    docker push $REGISTRY/sld-diy-java:$VERSION
fi

echo "================================"
echo "构建完成"
echo "镜像: sld-diy-java:$VERSION"
echo "================================"
