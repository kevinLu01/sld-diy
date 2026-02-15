# 后端Dockerfile - 多阶段构建优化
# 阶段1: 依赖安装
FROM node:18-slim AS dependencies

# 安装OpenSSL和其他必要依赖
RUN apt-get update && \
    apt-get install -y openssl libssl-dev ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 复制package文件（利用Docker缓存）
COPY backend/package*.json ./

# 安装所有依赖（包括devDependencies，因为Prisma需要）
RUN npm ci

# 生成Prisma客户端
COPY backend/prisma ./prisma
RUN npx prisma generate

# 阶段2: 生产镜像
FROM node:18-slim AS production

# 安装运行时依赖
RUN apt-get update && \
    apt-get install -y openssl ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 从依赖阶段复制node_modules
COPY --from=dependencies /app/node_modules ./node_modules
COPY --from=dependencies /app/package*.json ./

# 复制源代码
COPY backend/ .

# 创建非root用户
RUN useradd -m -u 1001 nodeuser && \
    chown -R nodeuser:nodeuser /app

USER nodeuser

EXPOSE 3001

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD node -e "require('http').get('http://localhost:3001/api/health', (r) => {process.exit(r.statusCode === 200 ? 0 : 1)})"

CMD ["npm", "start"]
