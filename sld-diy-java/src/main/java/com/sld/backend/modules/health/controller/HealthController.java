package com.sld.backend.modules.health.controller;

import com.sld.backend.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "健康检查", description = "服务健康状态检查")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    @Operation(summary = "基础健康检查")
    public Result<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now());
        status.put("service", "sld-diy-backend");
        return Result.success(status);
    }

    @GetMapping("/ready")
    @Operation(summary = "就绪检查")
    public Result<Map<String, Object>> ready() {
        Map<String, Object> status = new HashMap<>();
        status.put("ready", true);
        status.put("timestamp", LocalDateTime.now());
        return Result.success(status);
    }

    @GetMapping("/live")
    @Operation(summary = "存活检查")
    public Result<Map<String, Object>> live() {
        Map<String, Object> status = new HashMap<>();
        status.put("alive", true);
        status.put("timestamp", LocalDateTime.now());
        return Result.success(status);
    }

    @GetMapping("/details")
    @Operation(summary = "详细健康检查")
    public Result<Map<String, Object>> healthDetails() {
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", LocalDateTime.now());
        
        // 数据库检查
        Map<String, Object> dbStatus = checkDatabase();
        status.put("database", dbStatus);
        
        // Redis检查
        Map<String, Object> redisStatus = checkRedis();
        status.put("redis", redisStatus);
        
        // 整体状态
        boolean allHealthy = (Boolean) dbStatus.get("healthy") && (Boolean) redisStatus.get("healthy");
        status.put("status", allHealthy ? "UP" : "DOWN");
        status.put("healthy", allHealthy);
        
        return Result.success(status);
    }

    private Map<String, Object> checkDatabase() {
        Map<String, Object> status = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(3)) {
                status.put("healthy", true);
                status.put("status", "UP");
            } else {
                status.put("healthy", false);
                status.put("status", "DOWN");
                status.put("error", "Connection invalid");
            }
        } catch (SQLException e) {
            status.put("healthy", false);
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }

    private Map<String, Object> checkRedis() {
        Map<String, Object> status = new HashMap<>();
        try {
            String result = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(result)) {
                status.put("healthy", true);
                status.put("status", "UP");
            } else {
                status.put("healthy", false);
                status.put("status", "DOWN");
                status.put("error", "Unexpected response: " + result);
            }
        } catch (Exception e) {
            status.put("healthy", false);
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
        }
        return status;
    }
}
