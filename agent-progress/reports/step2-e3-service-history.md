# Step2 E3 工单通知与审计（状态历史）

## 完成时间
- 2026-02-18

## 实现内容
- 新增工单状态历史表：`t_service_request_history`
- 记录字段：`request_no/from_status/to_status/operator_id/operator_role/note/create_time`
- 写入时机：
  - 用户创建工单（初始化 pending）
  - 管理员变更工单状态（pending/processing/completed/closed）
- 用户可见：工单详情返回 `histories` 列表，支持追踪责任人和时间。

## 影响文件
- `sld-diy-java/db/migrations/V20260218_04_service_request_history.sql`
- `sld-diy-java/src/main/java/com/sld/backend/modules/service/entity/ServiceRequestHistory.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/service/mapper/ServiceRequestHistoryMapper.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/service/dto/response/ServiceRequestVO.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/service/service/impl/ServiceRequestServiceImpl.java`
- `sld-diy-java/src/main/resources/schema.sql`
