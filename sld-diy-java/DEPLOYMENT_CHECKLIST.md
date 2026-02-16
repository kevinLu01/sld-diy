# SLD DIY Java 后端 - Docker 部署检查报告

## 1. Docker 配置检查

### 1.1 Dockerfile 检查 ✅

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 多阶段构建 | ✅ | 使用 builder + runtime 两阶段构建 |
| 基础镜像选择 | ✅ | 使用 eclipse-temurin:17-jre-alpine，轻量且安全 |
| 依赖缓存 | ✅ | 先复制 pom.xml 下载依赖，再复制源代码 |
| 跳过测试编译 | ✅ | `-DskipTests` 加速构建 |
| 健康检查工具 | ✅ | 安装 curl 用于健康检查 |
| JVM 参数配置 | ✅ | `-Xms512m -Xmx1024m -XX:+UseG1GC` |
| 端口暴露 | ✅ | EXPOSE 9002 |
| 日志目录 | ✅ | 创建 /app/logs 目录 |

### 1.2 Docker Compose 检查

```yaml
# 需要检查以下内容：
services:
  backend-java:
    build:
      context: ./sld-diy-java
      dockerfile: Dockerfile
    ports:
      - "9002:9002"
    environment:
      - MYSQL_HOST=mysql
      - MYSQL_PORT=3306
      - MYSQL_DATABASE=sld_diy
      - REDIS_HOST=redis
      - REDIS_PORT=6379
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9002/api/v1/health"]
      interval: 30s
      timeout: 3s
      retries: 3
      start_period: 60s
```

## 2. 应用配置检查

### 2.1 application.yml 检查清单

```yaml
# 必须配置项
server:
  port: 9002  # 与 Dockerfile 暴露端口一致

spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:mysql}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:sld_diy}
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:}
    
  data:
    redis:
      host: ${REDIS_HOST:redis}
      port: ${REDIS_PORT:6379}
      
  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB

# JWT 配置
jwt:
  secret: ${JWT_SECRET:default-secret-key-change-in-production}
  expiration: 86400000

# 日志配置
logging:
  level:
    root: INFO
    com.sld.backend: INFO
  file:
    name: /app/logs/application.log
```

### 2.2 健康检查端点

```java
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("service", "sld-diy-backend");
        return ResponseEntity.ok(status);
    }
}
```

## 3. 安全扫描检查

### 3.1 Dockerfile 安全

| 检查项 | 状态 | 建议 |
|--------|------|------|
| 使用非 root 用户 | ⚠️ | 建议创建非 root 用户运行应用 |
| 镜像体积 | ✅ | 使用 alpine 基础镜像，体积小 |
| 敏感信息 | ✅ | 无硬编码密码 |
| 依赖漏洞 | ⚠️ | 建议使用 `docker scan` 扫描 |

### 3.2 改进建议

```dockerfile
# 添加非 root 用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app
USER appuser
```

## 4. 性能检查

### 4.1 JVM 参数优化建议

```bash
# 生产环境推荐配置
JAVA_OPTS="-Xms1g -Xmx2g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseStringDeduplication \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/app/logs/heapdump.hprof \
  -Xlog:gc*:file=/app/logs/gc.log:time:filecount=5,filesize=10m"
```

### 4.2 容器资源限制

```yaml
services:
  backend-java:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 3G
        reservations:
          cpus: '1'
          memory: 1G
```

## 5. 监控和日志

### 5.1 日志配置

```xml
<!-- logback-spring.xml -->
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>/app/logs/application.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>/app/logs/application.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <maxFileSize>100MB</maxFileSize>
        </rollingPolicy>
    </appender>
</configuration>
```

### 5.2 Actuator 端点

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when_authorized
```

## 6. 部署前检查清单

### 6.1 代码检查

- [ ] 所有单元测试通过
- [ ] 所有集成测试通过
- [ ] 代码覆盖率 > 80%
- [ ] 无 SonarQube 严重问题
- [ ] 数据库迁移脚本已准备

### 6.2 构建检查

- [ ] Maven 打包成功
- [ ] Docker 镜像构建成功
- [ ] 镜像可以正常启动
- [ ] 健康检查通过
- [ ] 日志输出正常

### 6.3 环境检查

- [ ] MySQL 数据库可访问
- [ ] Redis 可访问
- [ ] 端口 9002 未被占用
- [ ] 域名/SSL 证书已配置
- [ ] 环境变量已设置

## 7. 常见问题排查

### 7.1 启动失败

```bash
# 检查日志
docker logs -f sld-diy-backend

# 检查依赖服务
docker-compose ps

# 检查网络
docker network ls
docker network inspect sld-network
```

### 7.2 数据库连接失败

```bash
# 测试数据库连接
docker exec -it sld-diy-backend sh
nc -zv mysql 3306

# 检查环境变量
docker exec sld-diy-backend env | grep MYSQL
```

### 7.3 内存问题

```bash
# 查看容器资源使用
docker stats sld-diy-backend

# 查看 JVM 内存
jcmd 1 VM.native_memory summary
```
