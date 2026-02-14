const { PrismaClient } = require('@prisma/client');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');

const prisma = new PrismaClient();
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';
const router = require('express').Router();

const authMiddleware = async (req, res, next) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (token) {
      const decoded = jwt.verify(token, JWT_SECRET);
      req.userId = decoded.userId;
    }
    next();
  } catch {
    next();
  }
};

router.get('/config', async (req, res) => {
  try {
    const configs = await prisma.diyConfig.findMany({
      where: { isActive: true },
      orderBy: [{ category: 'asc' }, { sortOrder: 'asc' }]
    });

    const recommendations = await prisma.diyRecommendation.findMany({
      where: { isActive: true },
      include: { category: true },
      orderBy: [{ scenario: 'asc' }, { priority: 'asc' }]
    });

    res.json({
      code: 200,
      data: {
        configs,
        recommendations
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/solutions', async (req, res) => {
  try {
    const { industry, scenario } = req.query;
    const where = { status: 'published' };
    if (industry) where.industry = industry;
    if (scenario) where.scenario = scenario;

    const solutions = await prisma.solution.findMany({
      where,
      include: {
        products: {
          include: { product: { include: { brand: true } } }
        }
      },
      orderBy: { usageCount: 'desc' },
      take: 20
    });

    res.json({
      code: 200,
      data: solutions.map(s => ({
        id: s.id,
        title: s.title,
        industry: s.industry,
        scenario: s.scenario,
        description: s.description,
        coverImage: s.coverImage,
        temperatureRange: s.temperatureRange,
        capacityRange: s.capacityRange,
        features: s.features,
        totalPrice: Number(s.totalPrice || 0),
        usageCount: s.usageCount,
        productCount: s.products.length
      }))
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/recommend', authMiddleware, async (req, res) => {
  try {
    const { scenario, temperatureRange, coolingCapacity, capacityUnit, volume, volumeUnit, ambientTemp, options } = req.body;

    // 从DiyRecommendation获取该场景需要的产品类型
    const recommendations = await prisma.diyRecommendation.findMany({
      where: { scenario, isActive: true },
      include: { category: true },
      orderBy: { priority: 'asc' }
    });

    // 为每个推荐类型查询产品
    const productsMap = {};
    for (const rec of recommendations) {
      const categorySlug = rec.category.slug;
      const products = await prisma.product.findMany({
        where: {
          status: 'active',
          categoryId: rec.categoryId
        },
        include: { brand: true, specifications: true },
        take: 5
      });
      productsMap[categorySlug] = products.map((p) => ({
        id: p.id,
        name: p.name,
        sku: p.sku,
        price: Number(p.price),
        images: p.images,
        brand: p.brand?.name,
        specifications: p.specifications.reduce((acc, s) => {
          acc[s.specKey] = s.specValue + (s.unit || '');
          return acc;
        }, {})
      }));
    }

    const recommendationId = uuidv4();

    res.json({
      code: 200,
      data: {
        recommendationId,
        scenario,
        requirements: { temperatureRange, coolingCapacity, unit: capacityUnit },
        products: productsMap,
        recommendations: recommendations.map(r => ({
          productType: r.category.slug,
          isRequired: r.isRequired,
          priority: r.priority
        }))
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

// 验证兼容性
router.post('/validate-compatibility', async (req, res) => {
  try {
    const { productIds } = req.body;

    const compatibilities = await prisma.compatibility.findMany({
      where: {
        OR: [
          { productAId: { in: productIds }, productBId: { in: productIds } },
          { productBId: { in: productIds }, productAId: { in: productIds } }
        ]
      }
    });

    const warnings = [];
    const errors = [];
    const matrix = [];

    for (let i = 0; i < productIds.length; i++) {
      for (let j = i + 1; j < productIds.length; j++) {
        const compat = compatibilities.find(
          (c) => 
            (c.productAId === productIds[i] && c.productBId === productIds[j]) ||
            (c.productAId === productIds[j] && c.productBId === productIds[i])
        );

        if (compat?.compatibilityType === 'incompatible') {
          errors.push({
            type: 'incompatible',
            message: `产品 ${productIds[i]} 与 ${productIds[j]} 不兼容`
          });
        }

        matrix.push({
          productA: productIds[i],
          productB: productIds[j],
          status: compat?.compatibilityType || 'unknown',
          note: compat?.notes
        });
      }
    }

    res.json({
      code: 200,
      data: {
        compatible: errors.length === 0,
        warnings,
        errors,
        compatibilityMatrix: matrix
      }
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/projects', authMiddleware, async (req, res) => {
  try {
    const { projectName, solutionId, scenario, temperatureRange, coolingCapacity, capacityUnit, volume, volumeUnit, ambientTemp, options, selectedProducts } = req.body;

    let totalPrice = 0;
    let itemsToSave = selectedProducts || [];

    // If creating from a solution, load its products as default
    if (solutionId && (!selectedProducts || selectedProducts.length === 0)) {
      const solution = await prisma.solution.findUnique({
        where: { id: Number(solutionId) },
        include: { products: true }
      });
      if (solution) {
        itemsToSave = solution.products.map(sp => ({
          productId: sp.productId,
          quantity: sp.quantity,
          notes: sp.notes
        }));
        // Use solution parameters as defaults
        if (!scenario) scenario = solution.scenario;
        if (!temperatureRange) temperatureRange = solution.temperatureRange;
      }
    }

    if (itemsToSave.length > 0) {
      const products = await prisma.product.findMany({
        where: { id: { in: itemsToSave.map((p) => p.productId) } }
      });
      totalPrice = itemsToSave.reduce((sum, sp) => {
        const product = products.find((p) => p.id === sp.productId);
        return sum + (product ? Number(product.price) * sp.quantity : 0);
      }, 0);
    }

    const project = await prisma.diyProject.create({
      data: {
        userId: req.userId,
        solutionId: solutionId ? Number(solutionId) : null,
        projectName,
        scenario,
        temperatureRange,
        coolingCapacity,
        capacityUnit,
        volume,
        volumeUnit,
        ambientTemp,
        options,
        totalPrice,
        status: 'saved'
      }
    });

    if (itemsToSave.length > 0) {
      await prisma.diyProjectItem.createMany({
        data: itemsToSave.map((sp, idx) => ({
          projectId: project.id,
          productId: sp.productId,
          quantity: sp.quantity,
          notes: sp.notes,
          sortOrder: idx
        }))
      });
    }

    // Increment solution usage count
    if (solutionId) {
      await prisma.solution.update({
        where: { id: Number(solutionId) },
        data: { usageCount: { increment: 1 } }
      });
    }

    res.json({
      code: 200,
      message: '保存成功',
      data: { projectId: project.id }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/projects', async (req, res) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) {
      return res.json({ code: 200, data: { total: 0, items: [] } });
    }

    const decoded = jwt.verify(token, JWT_SECRET);
    const { page = 1, limit = 10 } = req.query;

    const projects = await prisma.diyProject.findMany({
      where: { userId: decoded.userId },
      include: {
        items: { include: { product: true } }
      },
      orderBy: { updatedAt: 'desc' },
      skip: (Number(page) - 1) * Number(limit),
      take: Number(limit)
    });

    const total = await prisma.diyProject.count({
      where: { userId: decoded.userId }
    });

    res.json({
      code: 200,
      data: { total, page: Number(page), limit: Number(limit), items: projects }
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/projects/:id', async (req, res) => {
  try {
    const project = await prisma.diyProject.findUnique({
      where: { id: Number(req.params.id) },
      include: {
        items: {
          include: { product: { include: { brand: true } } },
          orderBy: { sortOrder: 'asc' }
        }
      }
    });

    if (!project) {
      return res.status(404).json({ code: 404, message: '方案不存在' });
    }

    res.json({ code: 200, data: project });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/projects/:id/share', async (req, res) => {
  try {
    const shareToken = uuidv4().substring(0, 8);
    
    const project = await prisma.diyProject.update({
      where: { id: Number(req.params.id) },
      data: { shared: true, shareToken }
    });

    res.json({
      code: 200,
      data: {
        shareUrl: `https://sld-mall.com/diy/share/${shareToken}`,
        shareToken,
        qrCode: `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=https://sld-mall.com/diy/share/${shareToken}`
      }
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/share/:token', async (req, res) => {
  try {
    const project = await prisma.diyProject.findFirst({
      where: { shareToken: req.params.token, shared: true },
      include: {
        items: { include: { product: { include: { brand: true } } } }
      }
    });

    if (!project) {
      return res.status(404).json({ code: 404, message: '分享链接已失效' });
    }

    res.json({ code: 200, data: project });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
