-- Step2-K: 场景化BOM模板（场景->组件类型->规格选项）

CREATE TABLE IF NOT EXISTS t_diy_scene_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scene_code VARCHAR(64) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    application_notes VARCHAR(255),
    temp_min DECIMAL(8,2),
    temp_max DECIMAL(8,2),
    capacity_min DECIMAL(10,2),
    capacity_max DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_t_diy_scene_code(scene_code)
);

CREATE TABLE IF NOT EXISTS t_diy_scene_component (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scene_id BIGINT NOT NULL,
    component_code VARCHAR(64) NOT NULL,
    component_name VARCHAR(100) NOT NULL,
    component_role VARCHAR(20) DEFAULT 'main',
    is_required BOOLEAN DEFAULT TRUE,
    min_qty INT DEFAULT 1,
    max_qty INT DEFAULT 1,
    selection_mode VARCHAR(20) DEFAULT 'single',
    spec_requirement VARCHAR(255),
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    KEY idx_t_diy_scene_component_scene(scene_id)
);

CREATE TABLE IF NOT EXISTS t_diy_component_option (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scene_component_id BIGINT NOT NULL,
    product_id BIGINT,
    option_name VARCHAR(120) NOT NULL,
    brand_name VARCHAR(80),
    model_spec VARCHAR(120),
    spec_json TEXT,
    base_price DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    KEY idx_t_diy_component_option_component(scene_component_id)
);

-- 默认场景: 冷库制冷
INSERT INTO t_diy_scene_template (scene_code, name, description, application_notes, temp_min, temp_max, capacity_min, capacity_max, sort_order)
VALUES ('cold_storage', '冷库制冷', '适用于冷藏/冷冻/速冻冷库', '按库容、目标温度、进货频率选择机组', -30, 5, 5, 500, 10)
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

SET @scene_id = (SELECT id FROM t_diy_scene_template WHERE scene_code='cold_storage' LIMIT 1);

INSERT INTO t_diy_scene_component (scene_id, component_code, component_name, component_role, is_required, min_qty, max_qty, selection_mode, spec_requirement, sort_order)
VALUES
(@scene_id, 'compressor_unit', '压缩机/冷凝机组', 'main', TRUE, 1, 2, 'single', '按库容和目标温度匹配制冷量', 10),
(@scene_id, 'evaporator', '蒸发器', 'main', TRUE, 1, 4, 'single', '按冷库尺寸和风量匹配', 20),
(@scene_id, 'expansion_valve', '膨胀阀', 'auxiliary', TRUE, 1, 4, 'single', '按冷媒和流量匹配', 30),
(@scene_id, 'copper_pipe', '铜管与保温', 'auxiliary', TRUE, 1, 1, 'single', '按管径和管长匹配', 40),
(@scene_id, 'controller', '温控与电控', 'auxiliary', TRUE, 1, 2, 'single', '支持化霜/报警/远程监控', 50),
(@scene_id, 'refrigerant', '制冷剂', 'auxiliary', FALSE, 1, 1, 'single', '根据环保与工况选择', 60)
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '5HP 冷凝机组', 'Copeland', '低温型', '{"capacity":"5HP","temperature":"-18~-25"}', 6800, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='compressor_unit'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '8HP 冷凝机组', 'Bitzer', '低温型', '{"capacity":"8HP","temperature":"-18~-30"}', 9800, 20
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='compressor_unit'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '吊顶冷风机 DD-60', '国内品牌', '中温冷藏', '{"airVolume":"6000m3/h"}', 3200, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='evaporator'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '吊顶冷风机 DJ-80', '国内品牌', '低温冷冻', '{"airVolume":"8000m3/h"}', 4300, 20
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='evaporator'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '热力膨胀阀 R404A', 'Danfoss', 'TEX2', '{"refrigerant":"R404A"}', 420, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='expansion_valve'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, '铜管套装 Φ16/Φ10 + 25m', 'Hailiang', '中低温标准', '{"pipeLength":"25m"}', 1500, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='copper_pipe'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, 'PLC温控箱(双机位)', '施耐德', '支持远程告警', '{"remote":"true"}', 2600, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='controller'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;

INSERT INTO t_diy_component_option (scene_component_id, option_name, brand_name, model_spec, spec_json, base_price, sort_order)
SELECT c.id, 'R404A 冷媒 10kg', 'Honeywell', '冷冻工况', '{"weight":"10kg"}', 880, 10
FROM t_diy_scene_component c
WHERE c.scene_id=@scene_id AND c.component_code='refrigerant'
ON DUPLICATE KEY UPDATE update_time = CURRENT_TIMESTAMP;
