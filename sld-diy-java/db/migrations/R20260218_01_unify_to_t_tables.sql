-- Step2 A3: 回滚草案（回滚迁移时使用）
-- 说明：仅删除本次迁移插入的目标表记录。实际生产建议先做快照后执行。

START TRANSACTION;

-- 这里按ID范围或批次标签回滚更安全；当前草案仅示意
-- 建议在正式执行前增加 migration_batch_id 字段或审计表来精准回滚

-- 示例（请按实际批次条件替换）
-- DELETE FROM t_diy_recommendation WHERE create_time >= '2026-02-18 00:00:00';
-- DELETE FROM t_diy_config WHERE create_time >= '2026-02-18 00:00:00';
-- DELETE FROM t_solution WHERE create_time >= '2026-02-18 00:00:00';
-- DELETE FROM t_product WHERE create_time >= '2026-02-18 00:00:00';
-- DELETE FROM t_brand WHERE create_time >= '2026-02-18 00:00:00';
-- DELETE FROM t_category WHERE create_time >= '2026-02-18 00:00:00';

COMMIT;
