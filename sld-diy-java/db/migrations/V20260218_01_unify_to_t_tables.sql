-- Step2 A3: 迁移草案（预发演练后再生产执行）
-- 说明：按“增量插入（NOT EXISTS）”策略，从 CamelCase 表向 t_* 同步。

START TRANSACTION;

-- Category -> t_category
INSERT INTO t_category (id, name, slug, icon, parent_id, sort_order, description, status, deleted, create_time, update_time)
SELECT c.id, c.name, c.slug, c.icon, c.parent_id, c.sort_order, c.description,
       CASE WHEN c.is_active = 1 THEN 1 ELSE 0 END,
       0,
       c.createdAt,
       NOW()
FROM Category c
WHERE NOT EXISTS (SELECT 1 FROM t_category tc WHERE tc.id = c.id);

-- Brand -> t_brand
INSERT INTO t_brand (id, name, slug, logo, description, status, deleted, create_time, update_time)
SELECT b.id, b.name, b.slug, b.logo, b.description,
       CASE WHEN b.is_active = 1 THEN 1 ELSE 0 END,
       0,
       b.createdAt,
       NOW()
FROM Brand b
WHERE NOT EXISTS (SELECT 1 FROM t_brand tb WHERE tb.id = b.id);

-- Product -> t_product
INSERT INTO t_product (id, sku, name, brand_id, category_id, description, price, original_price, stock, images, status, deleted, create_time, update_time)
SELECT p.id, p.sku, p.name, p.brand_id, p.category_id, p.description, p.price, p.original_price,
       p.stock_quantity, p.images, p.status, 0, p.createdAt, p.updatedAt
FROM Product p
WHERE NOT EXISTS (SELECT 1 FROM t_product tp WHERE tp.id = p.id);

-- Solution -> t_solution
INSERT INTO t_solution (id, title, industry, scenario, description, total_price, status, deleted, create_time, update_time)
SELECT s.id, s.title, s.industry, s.scenario, s.description, s.total_price,
       CASE WHEN s.status = 'active' THEN 1 ELSE 0 END,
       0,
       s.createdAt,
       s.updatedAt
FROM Solution s
WHERE NOT EXISTS (SELECT 1 FROM t_solution ts WHERE ts.id = s.id);

-- DiyConfig -> t_diy_config
INSERT INTO t_diy_config (id, category, config_key, label, config_value, description, sort_order, is_active, deleted, create_time, update_time)
SELECT dc.id, dc.category, dc.`key`, dc.label, dc.value, dc.description, dc.sortOrder, dc.isActive, 0, dc.createdAt, dc.updatedAt
FROM DiyConfig dc
WHERE NOT EXISTS (SELECT 1 FROM t_diy_config tdc WHERE tdc.id = dc.id);

-- DiyRecommendation -> t_diy_recommendation
INSERT INTO t_diy_recommendation (id, scenario, category_id, product_type, priority, is_active, is_required, cooling_capacity_min, cooling_capacity_max, temperature_range, product_ids, total_price, energy_efficiency, description, status, deleted, create_time, update_time)
SELECT dr.id, dr.scenario, dr.categoryId, dr.productType, dr.priority, dr.isActive, dr.isRequired,
       dr.coolingCapacityMin, dr.coolingCapacityMax, dr.temperatureRange, dr.productIds,
       dr.totalPrice, dr.energyEfficiency, dr.description,
       CASE WHEN dr.isActive = 1 THEN 1 ELSE 0 END,
       0,
       dr.createdAt, dr.updatedAt
FROM DiyRecommendation dr
WHERE NOT EXISTS (SELECT 1 FROM t_diy_recommendation tdr WHERE tdr.id = dr.id);

COMMIT;
