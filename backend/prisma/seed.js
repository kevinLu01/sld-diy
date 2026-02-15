const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

async function main() {
  console.log('🌱 开始初始化测试数据...');

  // 1. 创建分类
  console.log('📦 创建产品分类...');
  const categories = [
    { id: 1, name: '压缩机', description: '各类制冷压缩机', icon: 'compressor' },
    { id: 2, name: '冷凝器', description: '风冷/水冷冷凝器', icon: 'condenser' },
    { id: 3, name: '蒸发器', description: '各类蒸发器', icon: 'evaporator' },
    { id: 4, name: '控制器', description: '温控器和控制系统', icon: 'controller' },
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
      name: '谷轮涡旋压缩机 ZB系列',
      description: '高效节能，静音运行，适用于中小型冷库',
      price: 3500.00,
      categoryId: 1,
      stock: 50,
      image: 'https://via.placeholder.com/400x300/2196F3/FFFFFF?text=ZB+Compressor',
      specifications: JSON.stringify({ model: 'ZB26KQE', power: '2.5HP', voltage: '380V' }),
    },
    {
      id: 2,
      name: '比泽尔半封闭压缩机',
      description: '德国品质，性能稳定',
      price: 8500.00,
      categoryId: 1,
      stock: 30,
      image: 'https://via.placeholder.com/400x300/FF9800/FFFFFF?text=Bitzer',
      specifications: JSON.stringify({ model: '4HE-25Y', power: '25HP' }),
    },
    {
      id: 3,
      name: '风冷冷凝器 FNH系列',
      description: '高效散热，节能环保',
      price: 2200.00,
      categoryId: 2,
      stock: 80,
      image: 'https://via.placeholder.com/400x300/9C27B0/FFFFFF?text=FNH',
      specifications: JSON.stringify({ model: 'FNH-40', area: '40㎡' }),
    },
    {
      id: 4,
      name: '冷风机蒸发器 DD系列',
      description: '快速制冷，温度均匀',
      price: 1800.00,
      categoryId: 3,
      stock: 60,
      image: 'https://via.placeholder.com/400x300/F44336/FFFFFF?text=DD',
      specifications: JSON.stringify({ model: 'DD-30', temperature: '-18℃~0℃' }),
    },
    {
      id: 5,
      name: '数显温控器 EK-3030',
      description: '精准控温，操作简单',
      price: 280.00,
      categoryId: 4,
      stock: 200,
      image: 'https://via.placeholder.com/400x300/FF5722/FFFFFF?text=EK-3030',
      specifications: JSON.stringify({ model: 'EK-3030', range: '-50℃~150℃' }),
    },
  ];

  for (const prod of products) {
    await prisma.product.upsert({
      where: { id: prod.id },
      update: {},
      create: prod,
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
      email: 'test@sldbd.com',
      password: hashedPassword,
      name: '测试用户',
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
