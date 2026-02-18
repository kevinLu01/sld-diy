package com.sld.backend.modules.admin.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sld.backend.modules.admin.entity.AdminAuditLog;
import com.sld.backend.modules.admin.service.AdminAuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理后台操作日志切面
 */
@Slf4j
@Aspect
@Component
@Order(10)
@RequiredArgsConstructor
public class AdminAuditLogAspect {

    private final AdminAuditLogService adminAuditLogService;
    private final ObjectMapper objectMapper;

    @AfterReturning("execution(* com.sld.backend.modules.admin.controller..*(..))")
    public void logAdminOperation(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String method = request.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
            return;
        }

        try {
            AdminAuditLog logEntity = new AdminAuditLog();
            logEntity.setOperatorId(currentOperatorId());
            logEntity.setAction(toAction(method));
            logEntity.setEntityType(resolveEntityType(request.getRequestURI()));
            logEntity.setEntityId(resolveEntityId(request.getRequestURI()));
            logEntity.setRequestPath(request.getRequestURI());
            logEntity.setRequestMethod(method);
            logEntity.setRequestPayload(buildPayload(joinPoint.getArgs()));
            logEntity.setResultCode(HttpServletResponse.SC_OK);
            logEntity.setCreateTime(LocalDateTime.now());
            adminAuditLogService.save(logEntity);
        } catch (Exception ex) {
            log.warn("save admin audit log failed", ex);
        }
    }

    private Long currentOperatorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        try {
            return Long.valueOf(authentication.getName());
        } catch (Exception ex) {
            return null;
        }
    }

    private String toAction(String method) {
        return switch (method) {
            case "POST" -> "create";
            case "PUT" -> "update";
            case "DELETE" -> "delete";
            default -> method.toLowerCase();
        };
    }

    private String resolveEntityType(String path) {
        String normalized = path == null ? "" : path.replace("/api/v1/admin/", "");
        String[] segments = normalized.split("/");
        return segments.length > 0 ? segments[0] : "unknown";
    }

    private String resolveEntityId(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }
        String[] segments = path.split("/");
        if (segments.length == 0) {
            return null;
        }
        String tail = segments[segments.length - 1];
        if ("status".equalsIgnoreCase(tail) && segments.length > 1) {
            return segments[segments.length - 2];
        }
        return tail.matches("[A-Za-z0-9_-]+") ? tail : null;
    }

    private String buildPayload(Object[] args) {
        Map<String, Object> payload = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            String className = arg.getClass().getName();
            if (className.startsWith("jakarta.servlet") || className.startsWith("org.springframework.web")) {
                continue;
            }
            payload.put("arg" + i, arg);
        }
        try {
            String json = objectMapper.writeValueAsString(payload);
            return json.length() > 2000 ? json.substring(0, 2000) : json;
        } catch (Exception ex) {
            return "{}";
        }
    }
}
