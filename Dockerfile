# 后端Dockerfile
FROM node:18-alpine

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
