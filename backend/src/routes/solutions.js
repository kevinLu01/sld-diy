const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();
const router = require('express').Router();

router.get('/', async (req, res) => {
  try {
    const { industry, scenario, temperatureRange, sort, page = 1, limit = 10 } = req.query;

    const where = { status: 'published' };
    if (industry) where.industry = industry;
    if (scenario) where.scenario = scenario;
    if (temperatureRange) where.temperatureRange = temperatureRange;

    let orderBy = { createdAt: 'desc' };
    if (sort === 'popular') orderBy = { usageCount: 'desc' };

    const solutions = await prisma.solution.findMany({
      where,
      orderBy,
      skip: (Number(page) - 1) * Number(limit),
      take: Number(limit)
    });

    const total = await prisma.solution.count({ where });

    res.json({
      code: 200,
      data: {
        total,
        items: solutions.map((s) => ({
          id: s.id,
          title: s.title,
          industry: s.industry,
          scenario: s.scenario,
          coverImage: s.coverImage,
          temperatureRange: s.temperatureRange,
          capacityRange: s.capacityRange,
          features: s.features,
          totalPrice: Number(s.totalPrice || 0),
          usageCount: s.usageCount,
          viewCount: s.viewCount,
          rating: Number(s.rating || 0)
        }))
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/:id', async (req, res) => {
  try {
    const solution = await prisma.solution.findUnique({
      where: { id: Number(req.params.id) },
      include: {
        products: {
          include: { product: { include: { brand: true } } }
        },
        cases: true
      }
    });

    if (!solution) {
      return res.status(404).json({ code: 404, message: '解决方案不存在' });
    }

    await prisma.solution.update({
      where: { id: solution.id },
      data: { viewCount: { increment: 1 } }
    });

    res.json({
      code: 200,
      data: {
        id: solution.id,
        title: solution.title,
        industry: solution.industry,
        scenario: solution.scenario,
        description: solution.description,
        coverImage: solution.coverImage,
        images: solution.images,
        temperatureRange: solution.temperatureRange,
        capacityRange: solution.capacityRange,
        features: solution.features,
        products: solution.products.map((sp) => ({
          productId: sp.productId,
          product: {
            id: sp.product.id,
            name: sp.product.name,
            brand: sp.product.brand?.name,
            price: Number(sp.product.price),
            images: sp.product.images
          },
          quantity: sp.quantity,
          notes: sp.notes,
          isRequired: sp.isRequired
        })),
        totalPrice: Number(solution.totalPrice || 0),
        installationGuide: solution.installationGuide,
        cases: solution.cases
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
