-- Step2-J: DIY 自定义场景 + 主辅件 + 私发折扣报价

ALTER TABLE t_diy_recommendation
    ADD COLUMN component_role VARCHAR(20) NOT NULL DEFAULT 'main' AFTER product_type;

CREATE INDEX idx_t_diy_rec_role ON t_diy_recommendation(component_role);

ALTER TABLE t_diy_project
    ADD COLUMN custom_scenario_name VARCHAR(100) NULL AFTER scenario,
    ADD COLUMN share_mode VARCHAR(20) NOT NULL DEFAULT 'public' AFTER shared,
    ADD COLUMN quoted_total_price DECIMAL(10,2) NULL AFTER total_price,
    ADD COLUMN discount_rate DECIMAL(5,4) NULL AFTER quoted_total_price,
    ADD COLUMN discount_amount DECIMAL(10,2) NULL AFTER discount_rate,
    ADD COLUMN private_note VARCHAR(255) NULL AFTER discount_amount,
    ADD COLUMN share_expires_at TIMESTAMP NULL AFTER share_token;

CREATE INDEX idx_t_diy_project_share_mode ON t_diy_project(share_mode);
CREATE INDEX idx_t_diy_project_share_expires_at ON t_diy_project(share_expires_at);
