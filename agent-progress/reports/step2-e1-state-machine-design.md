# Step2 E1 工单状态流转设计

## 状态集合
- `pending` 待处理
- `processing` 处理中
- `completed` 已完成
- `closed` 已关闭

## 流转规则
- `pending -> processing | closed`
- `processing -> completed | closed`
- `completed -> closed`
- 其他流转均拒绝。

## 权限矩阵
- 用户端：创建工单、查看本人工单、已完成工单评价。
- 管理端：查询全部工单、推进状态、填写处理结果、指派处理人。

## 业务约束
- 进入 `completed` 前必须提供 `resolution`。
- 工单仅在 `completed` 状态允许用户评价。
- 进入 `processing` 可指定 `assignedTo`，默认当前操作人。

## 接口落地
- `GET /api/v1/admin/service-requests`
- `PUT /api/v1/admin/service-requests/{requestNo}/status`
