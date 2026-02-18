-- Step2 E3: 工单状态变更历史
CREATE TABLE IF NOT EXISTS t_service_request_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_no VARCHAR(50) NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    operator_id BIGINT,
    operator_role VARCHAR(20),
    note TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_t_srv_hist_request_no ON t_service_request_history(request_no);
CREATE INDEX idx_t_srv_hist_time ON t_service_request_history(create_time);
