-- H2 Test Database Schema (MySQL Mode)
-- Auto-executed by Spring Boot for embedded databases

-- ===== 用户相关 =====
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    user_type VARCHAR(20) DEFAULT 'personal',
    status VARCHAR(20) DEFAULT 'active',
    avatar VARCHAR(500),
    nickname VARCHAR(50),
    wechat_openid VARCHAR(128),
    wechat_unionid VARCHAR(128),
    wechat_nickname VARCHAR(100),
    wechat_avatar VARCHAR(500),
    company_name VARCHAR(200),
    business_license VARCHAR(500),
    credit_code VARCHAR(50),
    verify_status VARCHAR(20),
    last_login_time TIMESTAMP,
    last_login_ip VARCHAR(50),
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_business_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    company_name VARCHAR(200),
    business_license VARCHAR(500),
    credit_code VARCHAR(50),
    industry VARCHAR(100),
    address TEXT,
    contact_person VARCHAR(50),
    contact_phone VARCHAR(20),
    verified BOOLEAN DEFAULT FALSE,
    verified_at TIMESTAMP,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_favorite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 产品相关 =====
CREATE TABLE IF NOT EXISTS t_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100),
    icon VARCHAR(200),
    parent_id BIGINT,
    sort_order INT DEFAULT 0,
    count INT DEFAULT 0,
    description TEXT,
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100),
    logo VARCHAR(500),
    description TEXT,
    website VARCHAR(255),
    product_count INT DEFAULT 0,
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(50),
    name VARCHAR(200) NOT NULL,
    brand_id BIGINT,
    category_id BIGINT,
    price DECIMAL(10,2),
    original_price DECIMAL(10,2),
    stock INT DEFAULT 0,
    images TEXT,
    video VARCHAR(500),
    model3d VARCHAR(500),
    specifications TEXT,
    description TEXT,
    sales_count INT DEFAULT 0,
    rating DECIMAL(3,2),
    review_count INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'on_shelf',
    sort_order INT DEFAULT 0,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_product_spec (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    spec_key VARCHAR(100),
    spec_value VARCHAR(500),
    unit VARCHAR(20),
    sort_order INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS t_product_attr (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT,
    attr_name VARCHAR(100),
    attr_value VARCHAR(500),
    unit VARCHAR(20),
    sort_order INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS t_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    rating INT,
    content TEXT,
    images TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'published',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_compatibility (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_a_id BIGINT,
    product_b_id BIGINT,
    compatibility_type VARCHAR(50),
    notes TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 订单相关 =====
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50),
    user_id BIGINT,
    username VARCHAR(50),
    total_amount DECIMAL(10,2),
    shipping_fee DECIMAL(10,2),
    installation_fee DECIMAL(10,2),
    discount_amount DECIMAL(10,2),
    final_amount DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'pending',
    diy_project_id BIGINT,
    recipient VARCHAR(50),
    phone VARCHAR(20),
    province VARCHAR(50),
    city VARCHAR(50),
    district VARCHAR(50),
    address TEXT,
    postal_code VARCHAR(10),
    notes TEXT,
    need_installation BOOLEAN DEFAULT FALSE,
    pay_time TIMESTAMP,
    ship_time TIMESTAMP,
    deliver_time TIMESTAMP,
    cancel_time TIMESTAMP,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    sku VARCHAR(50),
    product_name VARCHAR(200),
    product_image VARCHAR(500),
    quantity INT,
    price DECIMAL(10,2),
    total DECIMAL(10,2),
    specifications TEXT,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 购物车相关 =====
CREATE TABLE IF NOT EXISTS t_cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    quantity INT DEFAULT 1,
    price DECIMAL(10,2),
    sku VARCHAR(50),
    product_name VARCHAR(200),
    product_image VARCHAR(500),
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== DIY相关 =====
CREATE TABLE IF NOT EXISTS t_diy_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    project_name VARCHAR(200),
    scenario VARCHAR(50),
    custom_scenario_name VARCHAR(100),
    requirements TEXT,
    total_price DECIMAL(10,2),
    quoted_total_price DECIMAL(10,2),
    discount_rate DECIMAL(5,4),
    discount_amount DECIMAL(10,2),
    private_note VARCHAR(255),
    estimated_installation_fee DECIMAL(10,2),
    energy_efficiency VARCHAR(50),
    estimated_power_consumption VARCHAR(100),
    suggestions TEXT,
    shared BOOLEAN DEFAULT FALSE,
    share_mode VARCHAR(20) DEFAULT 'public',
    share_token VARCHAR(100),
    share_expires_at TIMESTAMP,
    view_count INT DEFAULT 0,
    usage_count INT DEFAULT 0,
    status INT DEFAULT 0,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_diy_project_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    diy_project_id BIGINT,
    product_id BIGINT,
    sku VARCHAR(50),
    product_name VARCHAR(200),
    product_image VARCHAR(500),
    quantity INT,
    price DECIMAL(10,2),
    total DECIMAL(10,2),
    match_score INT,
    match_reason TEXT,
    notes TEXT,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_diy_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100),
    config_key VARCHAR(100),
    label VARCHAR(100),
    config_value TEXT,
    icon VARCHAR(200),
    description TEXT,
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_diy_recommendation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scenario VARCHAR(50),
    category_id BIGINT,
    product_type VARCHAR(100),
    component_role VARCHAR(20) DEFAULT 'main',
    priority INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    is_required BOOLEAN DEFAULT FALSE,
    cooling_capacity_min DECIMAL(10,2),
    cooling_capacity_max DECIMAL(10,2),
    temperature_range VARCHAR(100),
    product_ids TEXT,
    total_price DECIMAL(10,2),
    energy_efficiency VARCHAR(50),
    description TEXT,
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 解决方案相关 =====
CREATE TABLE IF NOT EXISTS t_solution (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    industry VARCHAR(50),
    scenario VARCHAR(100),
    cover_image VARCHAR(500),
    images TEXT,
    temperature_range VARCHAR(100),
    capacity_range VARCHAR(100),
    features TEXT,
    description TEXT,
    total_price DECIMAL(10,2),
    installation_guide TEXT,
    technical_docs TEXT,
    cases TEXT,
    usage_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    rating DECIMAL(3,2),
    sort_order INT DEFAULT 0,
    status INT DEFAULT 1,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_solution_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solution_id BIGINT,
    product_id BIGINT,
    quantity INT,
    price DECIMAL(10,2),
    total DECIMAL(10,2),
    notes TEXT,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_solution_case (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    solution_id BIGINT,
    project_name VARCHAR(200),
    client_name VARCHAR(200),
    location VARCHAR(200),
    completion_date DATE,
    description TEXT,
    images TEXT,
    results TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===== 系统/知识/服务相关 =====
CREATE TABLE IF NOT EXISTS t_banner (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    image VARCHAR(500),
    link VARCHAR(500),
    position VARCHAR(50) DEFAULT 'home',
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    summary TEXT,
    content TEXT,
    cover_image VARCHAR(500),
    category VARCHAR(50),
    tags TEXT,
    author VARCHAR(50),
    source VARCHAR(100),
    view_count INT DEFAULT 0,
    like_count INT DEFAULT 0,
    helpful_count INT DEFAULT 0,
    sort_order INT DEFAULT 0,
    publish_status INT DEFAULT 1,
    publish_time TIMESTAMP,
    deleted INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_service_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_no VARCHAR(50),
    user_id BIGINT,
    order_id BIGINT,
    service_type VARCHAR(50),
    priority VARCHAR(20) DEFAULT 'normal',
    description TEXT,
    contact_info TEXT,
    scheduled_time TIMESTAMP,
    assigned_to BIGINT,
    status VARCHAR(20) DEFAULT 'pending',
    resolution TEXT,
    rating INT,
    feedback TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
