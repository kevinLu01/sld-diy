# Step2 A5 数据一致性核对报告

## 执行时间
- 2026-02-18

## 执行环境
- 服务器：`kevin-hk` (`ffjj.sldbd.com`)
- 数据库：`sld_diy`

## 执行过程
1. 对比 CamelCase 与 `t_*` 表行数，发现 `t_*` 初始全 0。
2. 定位迁移脚本问题：旧表字段为驼峰命名，原脚本引用蛇形字段导致失败。
3. 修复 `V20260218_01_unify_to_t_tables.sql` 并补齐缺失表迁移。
4. 在服务器执行迁移后再次核对行数。

## 行数对账结果
| Legacy | Count | Target | Count | 结果 |
|---|---:|---|---:|---|
| Category | 6 | t_category | 6 | 一致 |
| Brand | 5 | t_brand | 5 | 一致 |
| Product | 5 | t_product | 5 | 一致 |
| ProductSpec | 7 | t_product_spec | 7 | 一致 |
| ProductAttr | 0 | t_product_attr | 0 | 一致 |
| Compatibility | 5 | t_compatibility | 5 | 一致 |
| Cart | 0 | t_cart | 0 | 一致 |
| Solution | 1 | t_solution | 1 | 一致 |
| SolutionCase | 0 | t_solution_case | 0 | 一致 |
| DiyConfig | 16 | t_diy_config | 16 | 一致 |
| DiyRecommendation | 15 | t_diy_recommendation | 15 | 一致 |
| DiyProjectItem | 0 | t_diy_project_item | 0 | 一致 |

## 结论
- A5 行数核对通过，当前抽样模块无阻塞差异。
- 迁移脚本已可执行并已在目标环境完成一次回填。
