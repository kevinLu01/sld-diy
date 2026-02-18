# Step2 设计包（D1/E1/F1/G1）

## D1 方案发布门禁规则

### 规则
1. 方案状态从 `draft -> published` 前，必须满足：
- 至少绑定 1 个主件（压缩机/机组类）
- 至少绑定 1 个辅件（如阀件/控制器）
- 兼容性校验通过（无硬性冲突）

2. 未满足规则时禁止发布，返回业务码：
- `6002`: 方案缺少主件
- `6003`: 方案缺少辅件
- `6004`: 方案存在兼容性冲突

### 接口影响
- `POST /api/v1/admin/solutions/{id}/publish`
- `PUT /api/v1/admin/solutions/{id}`（当 status=published 时触发同校验）

## E1 服务工单状态机设计

### 状态图
- `pending` -> `processing` -> `completed` -> `closed`
- 允许 `processing -> pending`（退回）
- 禁止跨级跳转（例如 pending 直接 completed）

### 权限
- 用户：只读自己的工单状态
- 后台管理员：可执行状态流转

### 业务约束
- 仅 `completed` 状态允许用户评价
- 二次评价默认禁止（如需允许需增加 revision 规则）

## F1 后台操作审计模型

### 审计表建议 `t_admin_audit_log`
- `id`
- `operator_id`
- `operator_name`
- `module`（product/order/solution/user...）
- `action`（create/update/delete/publish/status_change）
- `entity_id`
- `before_json`
- `after_json`
- `ip`
- `ua`
- `create_time`

### 记录策略
- 所有后台写操作必须记录
- 只记录字段 diff，避免全量冗余

## G1 DIY 推荐可解释模型

### 返回结构扩展
- `score`: 总分
- `scoreBreakdown`: 评分分项
- `rulesHit`: 命中规则列表
- `rulesMissed`: 未命中规则及原因
- `alternatives`: 替代候选
- `riskNotes`: 风险提示

### 示例
```json
{
  "score": 86,
  "scoreBreakdown": {
    "capacityMatch": 35,
    "tempRangeMatch": 25,
    "energyPreference": 16,
    "cost": 10
  },
  "rulesHit": ["温区匹配", "制冷量覆盖"],
  "rulesMissed": ["噪音优先未满足"],
  "alternatives": [{"productId": 1024, "reason": "更低噪音"}],
  "riskNotes": ["当前环境温度上限接近方案边界"]
}
```

## 验收清单
- D1/E1/F1/G1 均有可执行接口清单与错误码/字段定义。
- 评审通过后可直接拆开发任务（D2/E2/F2/G2）。
