-- Step2 A3/A4: 迁移脚本（从 CamelCase 表回填到 t_*）
-- 说明：按“增量插入（NOT EXISTS）”策略，支持重复执行（幂等）。

START TRANSACTION;

-- Category -> t_category
INSERT INTO t_category (id, name, slug, icon, parent_id, sort_order, description, status, deleted, create_time, update_time)
SELECT c.id, c.name, c.slug, c.icon, c.parentId, c.sortOrder, c.description,
       CASE WHEN c.isActive = 1 THEN 1 ELSE 0 END,
       0,
       c.createdAt,
       NOW()
FROM Category c
WHERE NOT EXISTS (SELECT 1 FROM t_category tc WHERE tc.id = c.id);

-- Brand -> t_brand
INSERT INTO t_brand (id, name, slug, logo, description, website, status, deleted, create_time, update_time)
SELECT b.id, b.name, b.slug, b.logo, b.description, b.country,
       CASE WHEN b.isActive = 1 THEN 1 ELSE 0 END,
       0,
       b.createdAt,
       NOW()
FROM Brand b
WHERE NOT EXISTS (SELECT 1 FROM t_brand tb WHERE tb.id = b.id);

-- Product -> t_product
INSERT INTO t_product (id, sku, name, brand_id, category_id, description, price, original_price, stock, images, video, model3d, status, sales_count, deleted, create_time, update_time)
SELECT p.id, p.sku, p.name, p.brandId, p.categoryId, p.description, p.price, p.originalPrice,
       p.stockQuantity, p.images, p.videoUrl, p.model3dUrl,
       CASE WHEN LOWER(p.status) = 'active' THEN 'on_shelf' ELSE 'off_shelf' END,
       p.salesCount,
       0,
       p.createdAt,
       p.updatedAt
FROM Product p
WHERE NOT EXISTS (SELECT 1 FROM t_product tp WHERE tp.id = p.id);

-- ProductSpec -> t_product_spec
INSERT INTO t_product_spec (id, product_id, spec_key, spec_value, unit, sort_order)
SELECT ps.id, ps.productId, ps.specKey, ps.specValue, ps.unit, ps.sortOrder
FROM ProductSpec ps
WHERE NOT EXISTS (SELECT 1 FROM t_product_spec tps WHERE tps.id = ps.id);

-- ProductAttr -> t_product_attr
INSERT INTO t_product_attr (id, product_id, attr_name, attr_value, unit, sort_order)
SELECT pa.id, pa.productId, pa.attrName, pa.attrValue, pa.unit, pa.sortOrder
FROM ProductAttr pa
WHERE NOT EXISTS (SELECT 1 FROM t_product_attr tpa WHERE tpa.id = pa.id);

-- Compatibility -> t_compatibility
INSERT INTO t_compatibility (id, product_a_id, product_b_id, compatibility_type, notes, create_time)
SELECT cp.id, cp.productAId, cp.productBId, cp.compatibilityType, cp.notes, cp.createdAt
FROM Compatibility cp
WHERE NOT EXISTS (SELECT 1 FROM t_compatibility tcp WHERE tcp.id = cp.id);

-- Cart -> t_cart
INSERT INTO t_cart (id, user_id, create_time, update_time)
SELECT c.id, c.userId, c.createdAt, c.updatedAt
FROM Cart c
WHERE NOT EXISTS (SELECT 1 FROM t_cart tc WHERE tc.id = c.id);

-- Solution -> t_solution
INSERT INTO t_solution (id, title, industry, scenario, cover_image, images, temperature_range, capacity_range, features, description, total_price, installation_guide, usage_count, view_count, rating, status, deleted, create_time, update_time)
SELECT s.id, s.title, s.industry, s.scenario, s.coverImage, s.images, s.temperatureRange, s.capacityRange, s.features, s.description, s.totalPrice, s.installationGuide,
       s.usageCount, s.viewCount, s.rating,
       CASE WHEN LOWER(s.status) IN ('active', 'published') THEN 1 ELSE 0 END,
       0,
       s.createdAt,
       s.updatedAt
FROM Solution s
WHERE NOT EXISTS (SELECT 1 FROM t_solution ts WHERE ts.id = s.id);

-- SolutionCase -> t_solution_case
INSERT INTO t_solution_case (id, solution_id, project_name, client_name, location, completion_date, description, images, results, create_time)
SELECT sc.id, sc.solutionId, sc.projectName, sc.clientName, sc.location, DATE(sc.completionDate), sc.description, sc.images, sc.results, sc.createdAt
FROM SolutionCase sc
WHERE NOT EXISTS (SELECT 1 FROM t_solution_case tsc WHERE tsc.id = sc.id);

-- DiyConfig -> t_diy_config
INSERT INTO t_diy_config (id, category, config_key, label, config_value, icon, description, sort_order, is_active, deleted, create_time, update_time)
SELECT dc.id, dc.category, dc.`key`, dc.label, dc.value, dc.icon, dc.description, dc.sortOrder, dc.isActive, 0, dc.createdAt, dc.updatedAt
FROM DiyConfig dc
WHERE NOT EXISTS (SELECT 1 FROM t_diy_config tdc WHERE tdc.id = dc.id);

-- DiyRecommendation -> t_diy_recommendation
INSERT INTO t_diy_recommendation (id, scenario, category_id, product_type, priority, is_active, is_required, cooling_capacity_min, cooling_capacity_max, description, status, deleted, create_time, update_time)
SELECT dr.id, dr.scenario, dr.categoryId, dr.productType, dr.priority, dr.isActive, dr.isRequired,
       dr.minQuantity, dr.maxQuantity, NULL,
       CASE WHEN dr.isActive = 1 THEN 1 ELSE 0 END,
       0,
       dr.createdAt, dr.updatedAt
FROM DiyRecommendation dr
WHERE NOT EXISTS (SELECT 1 FROM t_diy_recommendation tdr WHERE tdr.id = dr.id);

-- DiyProjectItem -> t_diy_project_item
INSERT INTO t_diy_project_item (id, diy_project_id, product_id, quantity, notes)
SELECT dpi.id, dpi.projectId, dpi.productId, dpi.quantity, dpi.notes
FROM DiyProjectItem dpi
WHERE NOT EXISTS (SELECT 1 FROM t_diy_project_item tdpi WHERE tdpi.id = dpi.id);

COMMIT;
