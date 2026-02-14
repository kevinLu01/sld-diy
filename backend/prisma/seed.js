const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

async function main() {
  console.log('开始种子数据...');

  const categories = await Promise.all([
    prisma.category.upsert({
      where: { slug: 'compressors' },
      update: {},
      create: { name: '压缩机', slug: 'compressors', icon: 'fas fa-cog', sortOrder: 1 }
    }),
    prisma.category.upsert({
      where: { slug: 'condensers' },
      update: {},
      create: { name: '冷凝器', slug: 'condensers', icon: 'fas fa-wind', sortOrder: 2 }
    }),
    prisma.category.upsert({
      where: { slug: 'evaporators' },
      update: {},
      create: { name: '蒸发器', slug: 'evaporators', icon: 'fas fa-temperature-low', sortOrder: 3 }
    }),
    prisma.category.upsert({
      where: { slug: 'controllers' },
      update: {},
      create: { name: '控制器', slug: 'controllers', icon: 'fas fa-sliders-h', sortOrder: 4 }
    }),
    prisma.category.upsert({
      where: { slug: 'refrigerants' },
      update: {},
      create: { name: '冷媒配件', slug: 'refrigerants', icon: 'fas fa-tint', sortOrder: 5 }
    }),
    prisma.category.upsert({
      where: { slug: 'tools' },
      update: {},
      create: { name: '工具耗材', slug: 'tools', icon: 'fas fa-wrench', sortOrder: 6 }
    })
  ]);

  const brands = await Promise.all([
    prisma.brand.upsert({
      where: { slug: 'panasonic' },
      update: {},
      create: { name: '松下', slug: 'panasonic', country: '日本' }
    }),
    prisma.brand.upsert({
      where: { slug: 'copeland' },
      update: {},
      create: { name: '谷轮', slug: 'copeland', country: '美国' }
    }),
    prisma.brand.upsert({
      where: { slug: 'danfoss' },
      update: {},
      create: { name: '丹佛斯', slug: 'danfoss', country: '丹麦' }
    }),
    prisma.brand.upsert({
      where: { slug: 'carel' },
      update: {},
      create: { name: '卡乐', slug: 'carel', country: '意大利' }
    }),
    prisma.brand.upsert({
      where: { slug: 'ebm-papst' },
      update: {},
      create: { name: '依必安派特', slug: 'ebm-papst', country: '德国' }
    })
  ]);

  const compressorCat = categories.find(c => c.slug === 'compressors');
  const condenserCat = categories.find(c => c.slug === 'condensers');
  const evaporatorCat = categories.find(c => c.slug === 'evaporators');
  const controllerCat = categories.find(c => c.slug === 'controllers');

  const panasonic = brands.find(b => b.slug === 'panasonic');
  const copeland = brands.find(b => b.slug === 'copeland');
  const danfoss = brands.find(b => b.slug === 'danfoss');
  const carel = brands.find(b => b.slug === 'carel');

  const compressor1 = await prisma.product.upsert({
    where: { sku: 'PN-2P20S' },
    update: {},
    create: {
      sku: 'PN-2P20S',
      name: '松下 2P20S 涡旋压缩机',
      brandId: panasonic.id,
      categoryId: compressorCat.id,
      description: '高效节能涡旋压缩机，适用于商用制冷系统',
      price: 3680,
      originalPrice: 4200,
      stockQuantity: 50,
      images: 'https://placehold.co/400x400/0066cc/ffffff?text=2P20S',
      status: 'active',
      salesCount: 1250
    }
  });

  for (const spec of [
    { specKey: '功率', specValue: '2', unit: 'HP' },
    { specKey: '制冷量', specValue: '5.5', unit: 'kW' },
    { specKey: '电压', specValue: '380V/3Ph/50Hz', unit: '' },
    { specKey: '制冷剂', specValue: 'R404A', unit: '' },
    { specKey: '重量', specValue: '45', unit: 'kg' }
  ]) {
    await prisma.productSpec.create({
      data: { productId: compressor1.id, ...spec }
    });
  }

  const compressor2 = await prisma.product.upsert({
    where: { sku: 'CP-ZB45KQE' },
    update: {},
    create: {
      sku: 'CP-ZB45KQE',
      name: '谷轮 ZB45KQE-TFD 压缩机',
      brandId: copeland.id,
      categoryId: compressorCat.id,
      description: '高性能涡旋压缩机，可靠性高',
      price: 3280,
      stockQuantity: 35,
      images: 'https://placehold.co/400x400/0066cc/ffffff?text=ZB45KQE',
      status: 'active',
      salesCount: 890
    }
  });

  const condenser1 = await prisma.product.upsert({
    where: { sku: 'FNH-60' },
    update: {},
    create: {
      sku: 'FNH-60',
      name: '风冷翅片式冷凝器 FNH-60',
      brandId: danfoss.id,
      categoryId: condenserCat.id,
      description: '高效换热，低噪音设计',
      price: 1580,
      stockQuantity: 80,
      images: 'https://placehold.co/400x400/009900/ffffff?text=FNH-60',
      status: 'active',
      salesCount: 560
    }
  });

  for (const spec of [
    { specKey: '换热面积', specValue: '6', unit: 'm²' },
    { specKey: '风量', specValue: '3000', unit: 'm³/h' }
  ]) {
    await prisma.productSpec.create({
      data: { productId: condenser1.id, ...spec }
    });
  }

  const evaporator1 = await prisma.product.upsert({
    where: { sku: 'DD-12' },
    update: {},
    create: {
      sku: 'DD-12',
      name: '冷风机蒸发器 DD-12',
      brandId: danfoss.id,
      categoryId: evaporatorCat.id,
      description: '高效冷风机，适用于冷库和商超',
      price: 980,
      stockQuantity: 120,
      images: 'https://placehold.co/400x400/009900/ffffff?text=DD-12',
      status: 'active',
      salesCount: 780
    }
  });

  const controller1 = await prisma.product.upsert({
    where: { sku: 'EKC-326A' },
    update: {},
    create: {
      sku: 'EKC-326A',
      name: '卡乐智能温控器 EKC 326A',
      brandId: carel.id,
      categoryId: controllerCat.id,
      description: '数字显示，多点控制，故障报警',
      price: 680,
      stockQuantity: 200,
      images: 'https://placehold.co/400x400/ff6600/ffffff?text=EKC-326A',
      status: 'active',
      salesCount: 1450
    }
  });

  for (const compat of [
    { productAId: compressor1.id, productBId: condenser1.id, compatibilityType: 'recommended', notes: '完美匹配' },
    { productAId: compressor1.id, productBId: evaporator1.id, compatibilityType: 'compatible', notes: '良好匹配' },
    { productAId: compressor1.id, productBId: controller1.id, compatibilityType: 'compatible' },
    { productAId: condenser1.id, productBId: evaporator1.id, compatibilityType: 'compatible' },
    { productAId: compressor2.id, productBId: condenser1.id, compatibilityType: 'compatible' }
  ]) {
    await prisma.compatibility.upsert({
      where: { productAId_productBId: { productAId: compat.productAId, productBId: compat.productBId } },
      update: {},
      create: compat
    });
  }

  const solution = await prisma.solution.create({
    data: {
      title: '商超冷柜制冷系统方案',
      industry: 'retail',
      scenario: 'supermarket_freezer',
      description: '适用于大型超市、便利店的冷柜制冷系统解决方案',
      coverImage: 'https://placehold.co/800x400/667eea/ffffff?text=商超冷柜方案',
      images: '[]',
      temperatureRange: '-5~0°C',
      capacityRange: '20-100m³',
      features: '节能30%,低噪音运行,智能温控',
      totalPrice: 9920,
      usageCount: 1200,
      viewCount: 5600
    }
  });

  for (const sp of [
    { solutionId: solution.id, productId: compressor1.id, quantity: 1, isRequired: true, notes: '核心压缩机' },
    { solutionId: solution.id, productId: condenser1.id, quantity: 1, isRequired: true, notes: '风冷冷凝器' },
    { solutionId: solution.id, productId: evaporator1.id, quantity: 2, isRequired: true, notes: '冷风机' },
    { solutionId: solution.id, productId: controller1.id, quantity: 1, isRequired: true, notes: '智能温控' }
  ]) {
    await prisma.solutionProduct.create({ data: sp });
  }

  for (const article of [
    { title: '如何选择合适的压缩机', category: '选型指南', content: '选择压缩机时需要考虑以下因素：制冷量、温度范围、制冷剂类型、能效等级等...', tags: '压缩机,选型,制冷系统', author: '技术部' },
    { title: '冷库日常维护指南', category: '维护保养', content: '定期检查压缩机运行状态、清洁冷凝器、检查制冷剂压力...', tags: '冷库,维护,保养', author: '技术部' }
  ]) {
    await prisma.knowledgeArticle.create({ data: article });
  }

  const passwordHash = await bcrypt.hash('123456', 10);
  await prisma.user.upsert({
    where: { email: 'test@test.com' },
    update: {},
    create: {
      username: 'testuser',
      email: 'test@test.com',
      passwordHash,
      phone: '13800138000',
      userType: 'personal'
    }
  });

  console.log('种子数据创建完成！');

  // ===== DIY 配置 =====
  console.log('添加DIY配置...');

  // 应用场景配置
  const scenarios = [
    { key: 'cold_storage', label: '冷库制冷', value: '-25~-5', icon: 'fas fa-warehouse', description: '适用于大中型冷库', sortOrder: 1 },
    { key: 'supermarket', label: '商超冷柜', value: '-5~5', icon: 'fas fa-store', description: '适用于超市、便利店', sortOrder: 2 },
    { key: 'hvac', label: '中央空调', value: '16~30', icon: 'fas fa-wind', description: '商业建筑空调系统', sortOrder: 3 },
    { key: 'industrial', label: '工业制冷', value: 'custom', icon: 'fas fa-industry', description: '特殊工业用途', sortOrder: 4 }
  ];

  for (const s of scenarios) {
    await prisma.diyConfig.upsert({
      where: { category_key: { category: 'scenario', key: s.key } },
      update: {},
      create: { category: 'scenario', key: s.key, label: s.label, value: s.value, icon: s.icon, description: s.description, sortOrder: s.sortOrder }
    });
  }

  // 温度范围配置
  const tempRanges = [
    { key: '-25~-18', label: '低温冷冻', value: '-25~-18', sortOrder: 1 },
    { key: '-5~0', label: '冷藏保鲜', value: '-5~0', sortOrder: 2 },
    { key: '0~5', label: '冷藏', value: '0~5', sortOrder: 3 },
    { key: '5~15', label: '恒温', value: '5~15', sortOrder: 4 }
  ];

  for (const t of tempRanges) {
    await prisma.diyConfig.upsert({
      where: { category_key: { category: 'temperature', key: t.key } },
      update: {},
      create: { category: 'temperature', key: t.key, label: t.label, value: t.value, sortOrder: t.sortOrder }
    });
  }

  // 容量单位配置
  const capacityUnits = [
    { key: 'kw', label: 'kW (千瓦)', sortOrder: 1 },
    { key: 'rt', label: 'RT (冷吨)', sortOrder: 2 },
    { key: 'm3', label: 'm³ (立方米)', sortOrder: 3 },
    { key: 'hp', label: 'HP (匹)', sortOrder: 4 }
  ];

  for (const c of capacityUnits) {
    await prisma.diyConfig.upsert({
      where: { category_key: { category: 'capacityUnit', key: c.key } },
      update: {},
      create: { category: 'capacityUnit', key: c.key, label: c.label, sortOrder: c.sortOrder }
    });
  }

  // 产品类型配置（用于DIY推荐）
  const productTypes = [
    { key: 'compressor', label: '压缩机', description: '制冷系统核心部件', sortOrder: 1 },
    { key: 'condenser', label: '冷凝器', description: '热量散发部件', sortOrder: 2 },
    { key: 'evaporator', label: '蒸发器', description: '制冷效果部件', sortOrder: 3 },
    { key: 'controller', label: '控制器', description: '温控和系统管理', sortOrder: 4 }
  ];

  for (const p of productTypes) {
    await prisma.diyConfig.upsert({
      where: { category_key: { category: 'productType', key: p.key } },
      update: {},
      create: { category: 'productType', key: p.key, label: p.label, description: p.description, sortOrder: p.sortOrder }
    });
  }

  // DIY 推荐配置（每个场景需要哪些产品类型）
  const recommendations = [
    { scenario: 'cold_storage', productType: 'compressor', categoryId: compressorCat.id, priority: 1, isRequired: true },
    { scenario: 'cold_storage', productType: 'condenser', categoryId: condenserCat.id, priority: 2, isRequired: true },
    { scenario: 'cold_storage', productType: 'evaporator', categoryId: evaporatorCat.id, priority: 3, isRequired: true },
    { scenario: 'cold_storage', productType: 'controller', categoryId: controllerCat.id, priority: 4, isRequired: false },
    { scenario: 'supermarket', productType: 'compressor', categoryId: compressorCat.id, priority: 1, isRequired: true },
    { scenario: 'supermarket', productType: 'condenser', categoryId: condenserCat.id, priority: 2, isRequired: true },
    { scenario: 'supermarket', productType: 'evaporator', categoryId: evaporatorCat.id, priority: 3, isRequired: true },
    { scenario: 'supermarket', productType: 'controller', categoryId: controllerCat.id, priority: 4, isRequired: true },
    { scenario: 'hvac', productType: 'compressor', categoryId: compressorCat.id, priority: 1, isRequired: true },
    { scenario: 'hvac', productType: 'condenser', categoryId: condenserCat.id, priority: 2, isRequired: true },
    { scenario: 'hvac', productType: 'controller', categoryId: controllerCat.id, priority: 3, isRequired: true },
    { scenario: 'industrial', productType: 'compressor', categoryId: compressorCat.id, priority: 1, isRequired: true },
    { scenario: 'industrial', productType: 'condenser', categoryId: condenserCat.id, priority: 2, isRequired: true },
    { scenario: 'industrial', productType: 'evaporator', categoryId: evaporatorCat.id, priority: 3, isRequired: false },
    { scenario: 'industrial', productType: 'controller', categoryId: controllerCat.id, priority: 4, isRequired: true }
  ];

  for (const r of recommendations) {
    await prisma.diyRecommendation.upsert({
      where: { scenario_productType: { scenario: r.scenario, productType: r.productType } },
      update: {},
      create: r
    });
  }

  console.log('DIY配置添加完成！');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
