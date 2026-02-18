# Step2 F1/F2 后台CRUD可观测性实现

## 完成时间
- 2026-02-18

## F1 日志模型
- 新增表：`t_admin_audit_log`
- 字段：`operator_id/action/entity_type/entity_id/request_path/request_method/request_payload/result_code/create_time`
- 迁移脚本：`sld-diy-java/db/migrations/V20260218_03_admin_audit_log.sql`

## F2 写入与查询
1. 自动写入
- 新增切面 `AdminAuditLogAspect`：
  - 拦截 `com.sld.backend.modules.admin.controller..*`
  - 对 `POST/PUT/DELETE` 自动记录审计日志
  - 记录操作人、动作、资源、请求快照（截断至2000字符）

2. 查询接口
- 新增接口：`GET /api/v1/admin/audit-logs`
- 支持筛选：`operatorId/action/entityType` + 分页

## 关键文件
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/entity/AdminAuditLog.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/mapper/AdminAuditLogMapper.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/service/AdminAuditLogService.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/service/impl/AdminAuditLogServiceImpl.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/aspect/AdminAuditLogAspect.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/admin/controller/AdminAuditLogController.java`
- `sld-diy-java/src/main/resources/schema.sql`
- `sld-diy-java/pom.xml`（新增 `spring-boot-starter-aop`）

## 验证
- 后端编译通过：`mvn -DskipTests compile`

## 结论
- F1/F2 已具备基础可观测能力，后台主要变更操作可追踪。
