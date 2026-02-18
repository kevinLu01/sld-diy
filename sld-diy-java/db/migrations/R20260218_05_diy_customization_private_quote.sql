-- Step2-J rollback
DROP INDEX idx_t_diy_project_share_expires_at ON t_diy_project;
DROP INDEX idx_t_diy_project_share_mode ON t_diy_project;

ALTER TABLE t_diy_project
    DROP COLUMN share_expires_at,
    DROP COLUMN private_note,
    DROP COLUMN discount_amount,
    DROP COLUMN discount_rate,
    DROP COLUMN quoted_total_price,
    DROP COLUMN share_mode,
    DROP COLUMN custom_scenario_name;

DROP INDEX idx_t_diy_rec_role ON t_diy_recommendation;
ALTER TABLE t_diy_recommendation DROP COLUMN component_role;
