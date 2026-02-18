# Step2 A1/A2 - 双表读写盘点与目标模型对照

## A1. 当前读写盘点（Java实体层）

### 已使用 `t_*` 的实体
- `t_user`, `t_business_info`, `t_favorite`
- `t_order`, `t_order_item`
- `t_cart_item`
- `t_diy_project`
- `t_service_request`
- `t_solution_product`
- `t_review`
- `t_article`
- `t_banner`

### 仍使用 CamelCase 的实体
- `Category`, `Brand`, `Product`, `ProductSpec`, `ProductAttr`, `Compatibility`
- `Cart`
- `Solution`, `SolutionCase`
- `DiyConfig`, `DiyRecommendation`, `DiyProjectItem`

## 风险结论
- 当前系统是“混合表体系”：同一业务链路跨两套表。
- 该结构会导致：
  1) 测试库/预发库容易出现表缺失（已在 C2 本地测试复现）。
  2) 规则演进时新增字段容易漏改一侧。
  3) 数据核对复杂度高，线上排障成本高。

## A2. 目标模型（统一到 `t_*`）

### 目标映射
- `Category` -> `t_category`
- `Brand` -> `t_brand`
- `Product` -> `t_product`
- `ProductSpec` -> `t_product_spec`
- `ProductAttr` -> `t_product_attr`
- `Compatibility` -> `t_compatibility`
- `Cart` -> `t_cart`
- `Solution` -> `t_solution`
- `SolutionCase` -> `t_solution_case`
- `DiyConfig` -> `t_diy_config`
- `DiyRecommendation` -> `t_diy_recommendation`
- `DiyProjectItem` -> `t_diy_project_item`

### 执行原则
1. 统一主写模型：所有 Mapper/Entity 只指向 `t_*`。
2. CamelCase 表仅在迁移窗口期只读兜底，迁移完成后下线。
3. 所有 SQL 显式列映射（避免同名字段隐式映射）。

## 验收标准
- 主分支不再出现新的 `@TableName("CamelCase")`。
- 关键链路回归与线上抽样对账通过。
- 预发完成一次全量迁移 + 回滚演练。
