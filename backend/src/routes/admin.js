const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();

async function getStats(req, res) {
  try {
    const [
      totalProducts, totalOrders, totalUsers, totalSolutions,
      recentOrders, todayOrders, weekOrders
    ] = await Promise.all([
      prisma.product.count(),
      prisma.order.count(),
      prisma.user.count(),
      prisma.solution.count(),
      prisma.order.findMany({
        take: 10, orderBy: { createdAt: 'desc' },
        include: { user: { select: { username: true, email: true } }, items: true }
      }),
      prisma.order.count({ where: { createdAt: { gte: new Date(new Date().setHours(0, 0, 0, 0)) } } }),
      prisma.order.findMany({
        where: { createdAt: { gte: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000) } },
        select: { createdAt: true, finalAmount: true }
      })
    ]);

    const products = await prisma.product.findMany({ take: 5, orderBy: { salesCount: 'desc' } });
    const weekData = Array(7).fill(0);
    weekOrders.forEach(o => {
      const day = Math.floor((Date.now() - new Date(o.createdAt).getTime()) / (24 * 60 * 60 * 1000));
      if (day >= 0 && day < 7) weekData[6 - day] += Number(o.finalAmount);
    });

    res.json({
      code: 200, data: { totalProducts, totalOrders, totalUsers, totalSolutions, todayOrders, recentOrders, topProducts: products, weekData }
    });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '服务器错误' }); }
}

// 产品管理
async function getProducts(req, res) {
  try {
    const { page = 1, limit = 20, search, status, category } = req.query;
    const where = {};
    if (search) where.OR = [{ name: { contains: search } }, { sku: { contains: search } }];
    if (status) where.status = status;
    if (category) {
      const cat = await prisma.category.findUnique({ where: { slug: category } });
      if (cat) where.categoryId = cat.id;
    }

    const [items, total] = await Promise.all([
      prisma.product.findMany({
        where, include: { category: true, brand: true },
        orderBy: { createdAt: 'desc' },
        skip: (Number(page) - 1) * Number(limit), take: Number(limit)
      }),
      prisma.product.count({ where })
    ]);

    res.json({ code: 200, data: { total, page: Number(page), limit: Number(limit), items } });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function getProduct(req, res) {
  try {
    const product = await prisma.product.findUnique({
      where: { id: Number(req.params.id) },
      include: { category: true, brand: true, specifications: true }
    });
    if (!product) return res.status(404).json({ code: 404, message: '产品不存在' });
    res.json({ code: 200, data: product });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createProduct(req, res) {
  try {
    const { name, sku, brandId, categoryId, description, price, originalPrice, costPrice, stockQuantity, unit, images, videoUrl, status } = req.body;
    
    const exist = await prisma.product.findUnique({ where: { sku } });
    if (exist) return res.status(400).json({ code: 400, message: 'SKU已存在' });

    const product = await prisma.product.create({
      data: {
        name, sku, brandId: brandId ? Number(brandId) : null, categoryId: Number(categoryId),
        description, price: Number(price), originalPrice: originalPrice ? Number(originalPrice) : null,
        costPrice: costPrice ? Number(costPrice) : null, stockQuantity: Number(stockQuantity) || 0,
        unit: unit || '件', images: images || '', videoUrl, status: status || 'active'
      }
    });
    res.json({ code: 200, message: '创建成功', data: product });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateProduct(req, res) {
  try {
    const { id } = req.params;
    const data = req.body;
    if (data.price) data.price = Number(data.price);
    if (data.originalPrice) data.originalPrice = Number(data.originalPrice);
    if (data.brandId) data.brandId = Number(data.brandId);
    if (data.categoryId) data.categoryId = Number(data.categoryId);

    const product = await prisma.product.update({ where: { id: Number(id) }, data });
    res.json({ code: 200, message: '更新成功', data: product });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteProduct(req, res) {
  try {
    await prisma.product.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '删除失败' }); }
}

// 分类管理
async function getCategories(req, res) {
  try {
    const items = await prisma.category.findMany({
      include: { _count: { select: { products: true } }, children: true },
      orderBy: { sortOrder: 'asc' }
    });
    res.json({ code: 200, data: items });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createCategory(req, res) {
  try {
    const { name, slug, icon, sortOrder, parentId, description } = req.body;
    const category = await prisma.category.create({
      data: { name, slug, icon, sortOrder: Number(sortOrder) || 0, parentId: parentId ? Number(parentId) : null, description }
    });
    res.json({ code: 200, message: '创建成功', data: category });
  } catch (error) { res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateCategory(req, res) {
  try {
    const { id } = req.params;
    const data = req.body;
    if (data.sortOrder) data.sortOrder = Number(data.sortOrder);
    if (data.parentId) data.parentId = Number(data.parentId);
    const category = await prisma.category.update({ where: { id: Number(id) }, data });
    res.json({ code: 200, message: '更新成功', data: category });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteCategory(req, res) {
  try {
    await prisma.category.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

// 品牌管理
async function getBrands(req, res) {
  try {
    const items = await prisma.brand.findMany({
      include: { _count: { select: { products: true } } }
    });
    res.json({ code: 200, data: items });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createBrand(req, res) {
  try {
    const { name, slug, logo, country, description } = req.body;
    const brand = await prisma.brand.create({ data: { name, slug, logo, country, description } });
    res.json({ code: 200, message: '创建成功', data: brand });
  } catch (error) { res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateBrand(req, res) {
  try {
    const brand = await prisma.category.update({ where: { id: Number(req.params.id) }, data: req.body });
    res.json({ code: 200, message: '更新成功', data: brand });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteBrand(req, res) {
  try {
    await prisma.brand.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

// 订单管理
async function getOrders(req, res) {
  try {
    const { page = 1, limit = 20, status, search } = req.query;
    const where = {};
    if (status) where.status = status;

    const [items, total] = await Promise.all([
      prisma.order.findMany({
        where, include: { user: { select: { username: true, email: true, phone: true } }, items: true },
        orderBy: { createdAt: 'desc' },
        skip: (Number(page) - 1) * Number(limit), take: Number(limit)
      }),
      prisma.order.count({ where })
    ]);

    res.json({ code: 200, data: { total, page: Number(page), limit: Number(limit), items } });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function getOrder(req, res) {
  try {
    const order = await prisma.order.findUnique({
      where: { id: Number(req.params.id) },
      include: {
        user: { select: { username: true, email: true, phone: true } },
        items: { include: { product: true } }
      }
    });
    if (!order) return res.status(404).json({ code: 404, message: '订单不存在' });
    res.json({ code: 200, data: order });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function updateOrderStatus(req, res) {
  try {
    const { id } = req.params;
    const { status } = req.body;
    const order = await prisma.order.update({ where: { id: Number(id) }, data: { status } });
    res.json({ code: 200, message: '更新成功', data: order });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

// 解决方案管理
async function getSolutions(req, res) {
  try {
    const items = await prisma.solution.findMany({
      include: { products: { include: { product: true } }, _count: { select: { cases: true } } },
      orderBy: { createdAt: 'desc' }
    });
    res.json({ code: 200, data: items });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function getSolution(req, res) {
  try {
    const solution = await prisma.solution.findUnique({
      where: { id: Number(req.params.id) },
      include: { products: { include: { product: true } }, cases: true }
    });
    if (!solution) return res.status(404).json({ code: 404, message: '方案不存在' });
    res.json({ code: 200, data: solution });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createSolution(req, res) {
  try {
    const { title, industry, scenario, description, coverImage, temperatureRange, capacityRange, features, totalPrice, installationGuide, products } = req.body;
    
    const solution = await prisma.solution.create({
      data: { 
        title, industry, scenario, description, coverImage, 
        images: '', 
        features: features || '', 
        temperatureRange, capacityRange, 
        totalPrice: Number(totalPrice) || 0, 
        installationGuide, 
        status: 'published',
        products: products ? {
          create: products.map((p, idx) => ({
            productId: p.productId,
            quantity: p.quantity || 1,
            isRequired: p.isRequired !== undefined ? p.isRequired : true,
            notes: p.notes || '',
            sortOrder: idx
          }))
        } : undefined
      }
    });
    res.json({ code: 200, message: '创建成功', data: solution });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateSolution(req, res) {
  try {
    const { products, ...data } = req.body;
    if (data.totalPrice) data.totalPrice = Number(data.totalPrice);
    
    // Update solution basic info
    const solution = await prisma.solution.update({ 
      where: { id: Number(req.params.id) }, 
      data 
    });
    
    // Update products if provided
    if (products) {
      // Delete existing products
      await prisma.solutionProduct.deleteMany({
        where: { solutionId: Number(req.params.id) }
      });
      // Create new products
      if (products.length > 0) {
        await prisma.solutionProduct.createMany({
          data: products.map((p, idx) => ({
            solutionId: Number(req.params.id),
            productId: p.productId,
            quantity: p.quantity || 1,
            isRequired: p.isRequired !== undefined ? p.isRequired : true,
            notes: p.notes || '',
            sortOrder: idx
          }))
        });
      }
    }
    
    res.json({ code: 200, message: '更新成功', data: solution });
  } catch (error) { console.error(error); res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteSolution(req, res) {
  try {
    await prisma.solution.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

// 知识库管理
async function getArticles(req, res) {
  try {
    const { page = 1, limit = 20, category, search } = req.query;
    const where = {};
    if (category) where.category = category;
    if (search) where.OR = [{ title: { contains: search } }, { content: { contains: search } }];

    const [items, total] = await Promise.all([
      prisma.knowledgeArticle.findMany({
        where, orderBy: { createdAt: 'desc' },
        skip: (Number(page) - 1) * Number(limit), take: Number(limit)
      }),
      prisma.knowledgeArticle.count({ where })
    ]);

    res.json({ code: 200, data: { total, page: Number(page), limit: Number(limit), items } });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function getArticle(req, res) {
  try {
    const article = await prisma.knowledgeArticle.findUnique({ where: { id: Number(req.params.id) } });
    if (!article) return res.status(404).json({ code: 404, message: '文章不存在' });
    res.json({ code: 200, data: article });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createArticle(req, res) {
  try {
    const { title, category, content, tags, coverImage, author, status } = req.body;
    const article = await prisma.knowledgeArticle.create({
      data: { title, category, content, tags, coverImage, author, status: status || 'published' }
    });
    res.json({ code: 200, message: '创建成功', data: article });
  } catch (error) { res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateArticle(req, res) {
  try {
    const article = await prisma.knowledgeArticle.update({ where: { id: Number(req.params.id) }, data: req.body });
    res.json({ code: 200, message: '更新成功', data: article });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteArticle(req, res) {
  try {
    await prisma.knowledgeArticle.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

// 用户管理
async function getUsers(req, res) {
  try {
    const { page = 1, limit = 20, search } = req.query;
    const where = {};
    if (search) where.OR = [{ username: { contains: search } }, { email: { contains: search } }, { phone: { contains: search } }];

    const [items, total] = await Promise.all([
      prisma.user.findMany({
        where, select: { id: true, username: true, email: true, phone: true, userType: true, status: true, createdAt: true },
        orderBy: { createdAt: 'desc' },
        skip: (Number(page) - 1) * Number(limit), take: Number(limit)
      }),
      prisma.user.count({ where })
    ]);

    res.json({ code: 200, data: { total, page: Number(page), limit: Number(limit), items } });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function updateUserStatus(req, res) {
  try {
    const { status } = req.body;
    const user = await prisma.user.update({ where: { id: Number(req.params.id) }, data: { status } });
    res.json({ code: 200, message: '更新成功', data: user });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

// ===== DIY 配置管理 =====
async function getDiyConfigs(req, res) {
  try {
    const { category } = req.query;
    const where = category ? { category } : {};
    const items = await prisma.diyConfig.findMany({
      where,
      orderBy: [{ category: 'asc' }, { sortOrder: 'asc' }]
    });
    res.json({ code: 200, data: items });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createDiyConfig(req, res) {
  try {
    const { category, key, label, value, icon, description, sortOrder } = req.body;
    const config = await prisma.diyConfig.create({
      data: { category, key, label, value, icon, description, sortOrder: sortOrder || 0 }
    });
    res.json({ code: 200, message: '创建成功', data: config });
  } catch (error) { res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateDiyConfig(req, res) {
  try {
    const { category, key, label, value, icon, description, sortOrder, isActive } = req.body;
    const config = await prisma.diyConfig.update({
      where: { id: Number(req.params.id) },
      data: { category, key, label, value, icon, description, sortOrder, isActive }
    });
    res.json({ code: 200, message: '更新成功', data: config });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteDiyConfig(req, res) {
  try {
    await prisma.diyConfig.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

// ===== DIY 推荐配置管理 =====
async function getDiyRecommendations(req, res) {
  try {
    const { scenario } = req.query;
    const where = scenario ? { scenario } : {};
    const items = await prisma.diyRecommendation.findMany({
      where,
      include: { category: true },
      orderBy: [{ scenario: 'asc' }, { priority: 'asc' }]
    });
    res.json({ code: 200, data: items });
  } catch (error) { res.status(500).json({ code: 500, message: '服务器错误' }); }
}

async function createDiyRecommendation(req, res) {
  try {
    const { scenario, productType, categoryId, priority, isRequired, minQuantity, maxQuantity } = req.body;
    const rec = await prisma.diyRecommendation.create({
      data: { scenario, productType, categoryId: Number(categoryId), priority: priority || 0, isRequired: isRequired !== false, minQuantity: minQuantity || 1, maxQuantity: maxQuantity || 1 }
    });
    res.json({ code: 200, message: '创建成功', data: rec });
  } catch (error) { res.status(500).json({ code: 500, message: '创建失败' }); }
}

async function updateDiyRecommendation(req, res) {
  try {
    const data = req.body;
    if (data.categoryId) data.categoryId = Number(data.categoryId);
    const rec = await prisma.diyRecommendation.update({
      where: { id: Number(req.params.id) },
      data
    });
    res.json({ code: 200, message: '更新成功', data: rec });
  } catch (error) { res.status(500).json({ code: 500, message: '更新失败' }); }
}

async function deleteDiyRecommendation(req, res) {
  try {
    await prisma.diyRecommendation.delete({ where: { id: Number(req.params.id) } });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) { res.status(500).json({ code: 500, message: '删除失败' }); }
}

module.exports = {
  getStats, getProducts, getProduct, createProduct, updateProduct, deleteProduct,
  getCategories, createCategory, updateCategory, deleteCategory,
  getBrands, createBrand, updateBrand, deleteBrand,
  getOrders, getOrder, updateOrderStatus,
  getSolutions, getSolution, createSolution, updateSolution, deleteSolution,
  getArticles, getArticle, createArticle, updateArticle, deleteArticle,
  getUsers, updateUserStatus,
  getDiyConfigs, createDiyConfig, updateDiyConfig, deleteDiyConfig,
  getDiyRecommendations, createDiyRecommendation, updateDiyRecommendation, deleteDiyRecommendation
};
