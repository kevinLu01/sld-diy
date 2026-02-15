const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

async function main() {
  console.log('🌱 开始初始化测试数据...');

  // 1. 创建分类
  console.log('📦 创建产品分类...');
  const categories = [
    { id: 1, name: '压缩机', slug: 'compressor', description: '各类制冷压缩机', icon: 'compressor' },
    { id: 2, name: '冷凝器', slug: 'condenser', description: '风冷/水冷冷凝器', icon: 'condenser' },
    { id: 3, name: '蒸发器', slug: 'evaporator', description: '各类蒸发器', icon: 'evaporator' },
    { id: 4, name: '控制器', slug: 'controller', description: '温控器和控制系统', icon: 'controller' },
  ];

  for (const cat of categories) {
    await prisma.category.upsert({
      where: { id: cat.id },
      update: {},
      create: cat,
    });
  }

  console.log(`✅ 创建了 ${categories.length} 个分类`);

  // 2. 创建产品
  console.log('📦 创建产品...');
  const products = [
    {
      id: 1,
      sku: 'COMP-ZB26KQE',
      name: '谷轮涡旋压缩机 ZB系列',
      description: '高效节能，静音运行，适用于中小型冷库',
      price: 3500.00,
      categoryId: 1,
      stockQuantity: 50,
      images: JSON.stringify(['https://via.placeholder.com/400x300/2196F3/FFFFFF?text=ZB+Compressor']),
      specifications: [
        { specKey: '型号', specValue: 'ZB26KQE', sortOrder: 1 },
        { specKey: '功率', specValue: '2.5', unit: 'HP', sortOrder: 2 },
        { specKey: '电压', specValue: '380', unit: 'V', sortOrder: 3 },
        { specKey: '制冷剂', specValue: 'R404A', sortOrder: 4 },
      ],
    },
    {
      id: 2,
      sku: 'COMP-4HE25Y',
      name: '比泽尔半封闭压缩机',
      description: '德国品质，性能稳定',
      price: 8500.00,
      categoryId: 1,
      stockQuantity: 30,
      images: JSON.stringify(['https://via.placeholder.com/400x300/FF9800/FFFFFF?text=Bitzer']),
      specifications: [
        { specKey: '型号', specValue: '4HE-25Y', sortOrder: 1 },
        { specKey: '功率', specValue: '25', unit: 'HP', sortOrder: 2 },
        { specKey: '电压', specValue: '380', unit: 'V', sortOrder: 3 },
      ],
    },
    {
      id: 3,
      sku: 'COND-FNH40',
      name: '风冷冷凝器 FNH系列',
      description: '高效散热，节能环保',
      price: 2200.00,
      categoryId: 2,
      stockQuantity: 80,
      images: JSON.stringify(['https://via.placeholder.com/400x300/9C27B0/FFFFFF?text=FNH']),
      specifications: [
        { specKey: '型号', specValue: 'FNH-40', sortOrder: 1 },
        { specKey: '散热面积', specValue: '40', unit: '㎡', sortOrder: 2 },
        { specKey: '风量', specValue: '8000', unit: 'm³/h', sortOrder: 3 },
      ],
    },
    {
      id: 4,
      sku: 'EVAP-DD30',
      name: '冷风机蒸发器 DD系列',
      description: '快速制冷，温度均匀',
      price: 1800.00,
      categoryId: 3,
      stockQuantity: 60,
      images: JSON.stringify(['https://via.placeholder.com/400x300/F44336/FFFFFF?text=DD']),
      specifications: [
        { specKey: '型号', specValue: 'DD-30', sortOrder: 1 },
        { specKey: '温度范围', specValue: '-18℃~0℃', sortOrder: 2 },
        { specKey: '融霜方式', specValue: '电热融霜', sortOrder: 3 },
      ],
    },
    {
      id: 5,
      sku: 'CTRL-EK3030',
      name: '数显温控器 EK-3030',
      description: '精准控温，操作简单',
      price: 280.00,
      categoryId: 4,
      stockQuantity: 200,
      images: JSON.stringify(['https://via.placeholder.com/400x300/FF5722/FFFFFF?text=EK-3030']),
      specifications: [
        { specKey: '型号', specValue: 'EK-3030', sortOrder: 1 },
        { specKey: '温度范围', specValue: '-50℃~150℃', sortOrder: 2 },
        { specKey: '精度', specValue: '±0.5', unit: '℃', sortOrder: 3 },
      ],
    },
  ];

  for (const prod of products) {
    const { specifications, ...productData } = prod;

    await prisma.product.upsert({
      where: { id: prod.id },
      update: {},
      create: {
        ...productData,
        specifications: {
          create: specifications,
        },
      },
    });
  }

  console.log(`✅ 创建了 ${products.length} 个产品`);

  // 3. 创建测试用户
  console.log('👤 创建测试用户...');
  const hashedPassword = await bcrypt.hash('123456', 10);
  
  await prisma.user.upsert({
    where: { email: 'test@sldbd.com' },
    update: {},
    create: {
      username: 'testuser',
      email: 'test@sldbd.com',
      passwordHash: hashedPassword,
      phone: '13800138000',
    },
  });

  console.log('✅ 测试用户: test@sldbd.com (密码: 123456)');
  console.log('\n🎉 数据初始化完成！');
}

main()
  .catch((e) => {
    console.error('❌ 错误:', e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
