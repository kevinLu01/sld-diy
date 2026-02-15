# 后端Dockerfile - 使用Debian镜像（包含完整的OpenSSL支持）
FROM node:18-slim

# 安装OpenSSL和其他必要依赖
RUN apt-get update && \
    apt-get install -y openssl libssl-dev ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 复制package文件
COPY backend/package*.json ./

# 安装依赖
RUN npm install --production

# 复制源代码
COPY backend/ .

# 生成Prisma客户端
RUN npx prisma generate

EXPOSE 3001

CMD ["npm", "start"]
