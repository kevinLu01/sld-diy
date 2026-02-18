# Step2 A4 代码切换与旧表下线报告

## 完成时间
- 2026-02-18

## 结论
- 后端代码已完成 CamelCase 表到 `t_*` 的主读写切换。
- 代码库中已无 `@TableName("CamelCase")` 与旧表 SQL 引用。

## 关键变更
- 实体表名切换到 `t_*`：
  - `Category -> t_category`
  - `Brand -> t_brand`
  - `Product -> t_product`
  - `ProductSpec -> t_product_spec`
  - `ProductAttr -> t_product_attr`
  - `Compatibility -> t_compatibility`
  - `Cart -> t_cart`
  - `DiyConfig -> t_diy_config`
  - `DiyRecommendation -> t_diy_recommendation`
  - `DiyProjectItem -> t_diy_project_item`
  - `Solution -> t_solution`
  - `SolutionCase -> t_solution_case`
- 补齐了 snake_case 列映射（`@TableField`），避免 `map-underscore-to-camel-case=false` 下字段失配。
- DIY mapper 手写 SQL 已切换至 `t_diy_config/t_diy_recommendation/t_category`。

## 验证
- 静态扫描：
  - `@TableName("CamelCase")` 命中为 0。
  - 旧表 `FROM/JOIN` SQL 命中为 0。
- 编译验证：`mvn -DskipTests compile` 通过。

## 风险与说明
- `DiyRecommendation.minQuantity/maxQuantity` 兼容映射至 `cooling_capacity_min/max`（历史字段语义迁移）。
- `Brand.country` 映射到 `t_brand.website` 兼容保存（历史模型差异）。
