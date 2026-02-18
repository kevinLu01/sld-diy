-- Step2 F1/F2: 后台操作日志
CREATE TABLE IF NOT EXISTS t_admin_audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator_id BIGINT,
    action VARCHAR(32) NOT NULL,
    entity_type VARCHAR(64) NOT NULL,
    entity_id VARCHAR(64),
    request_path VARCHAR(255),
    request_method VARCHAR(16),
    request_payload TEXT,
    result_code INT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_t_admin_audit_log_operator ON t_admin_audit_log(operator_id);
CREATE INDEX idx_t_admin_audit_log_entity ON t_admin_audit_log(entity_type);
CREATE INDEX idx_t_admin_audit_log_time ON t_admin_audit_log(create_time);
