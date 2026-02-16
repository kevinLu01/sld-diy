# SLD DIY Backend - Java (Spring Boot)

生利达冷冻空调配件DIY商城后端 - Spring Boot + MyBatis Plus 版本

## 技术栈

- Spring Boot 3.2.3
- Spring Security 6.x
- MyBatis Plus 3.5.5
- MySQL 8.0
- Redis 7.x
- JWT (jjwt 0.12.3)
- Knife4j (Swagger) 4.4.0
- Hutool 5.8.24
- Lombok

## 快速开始

### 本地开发

```bash
# 1. 确保已安装 JDK 17+ 和 Maven 3.8+

# 2. 克隆项目
cd sld-diy-java

# 3. 安装依赖
mvn clean install

# 4. 配置数据库和 Redis
# 编辑 src/main/resources/application.yml

# 5. 启动应用
mvn spring-boot:run
```

### Docker 部署

```bash
# 构建镜像
docker build -t sld-diy-backend-java .

# 运行容器
docker run -p 9002:9002 \
  -e MYSQL_HOST=localhost \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DATABASE=sld_diy \
  -e MYSQL_USERNAME=root \
  -e MYSQL_PASSWORD=your_password \
  -e JWT_SECRET=your_secret_key \
  sld-diy-backend-java
```

### Docker Compose 部署

```bash
# 在项目根目录运行
cd sld-diy
docker-compose up -d backend-java
```

## 访问地址

- 应用地址: http://localhost:9002
- API 文档: http://localhost:9002/doc.html
- 健康检查: http://localhost:9002/api/v1/health

## 项目结构

```
sld-diy-java/
├── src/main/java/com/sld/backend/
│   ├── SldBackendApplication.java    # 启动类
│   ├── common/                       # 公共模块
│   │   ├── constant/                 # 常量
│   │   ├── enums/                    # 枚举
│   │   ├── exception/                # 异常处理
│   │   ├── result/                   # 统一响应
│   │   └── util/                    # 工具类
│   ├── config/                      # 配置类
│   ├── security/                    # 安全模块
│   └── modules/                     # 业务模块
│       ├── auth/                    # 认证
│       ├── user/                    # 用户
│       ├── product/                 # 产品
│       ├── category/                # 分类
│       ├── cart/                    # 购物车
│       ├── order/                   # 订单
│       ├── diy/                     # DIY
│       ├── solution/                # 解决方案
│       ├── knowledge/               # 知识库
│       └── admin/                   # 管理后台
└── src/main/resources/
    └── application.yml              # 配置文件
```

## API 文档

启动应用后访问: http://localhost:9002/doc.html

## 端口说明

| 服务 | 端口 |
|------|------|
| Node.js 后端 | 9001 |
| Java 后端 | 9002 |
| MySQL | 3306 |
| Redis | 6379 |

## 开发说明

### 新增模块

1. 创建 Entity 实体类
2. 创建 Mapper 接口
3. 创建 Service 接口和实现
4. 创建 Controller 控制器
5. 创建 DTO/VO

### API 兼容性

Java 后端与 Node.js 后端保持 API 接口完全兼容，可无缝切换。

## License

ISC
