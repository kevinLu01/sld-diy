const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();
const router = require('express').Router();

router.get('/', async (req, res) => {
  try {
    const { category, page = 1, limit = 20, sort, brand, priceMin, priceMax, search } = req.query;

    const where = { status: 'active' };
    
    if (category) {
      const cat = await prisma.category.findUnique({ where: { slug: category } });
      if (cat) where.categoryId = cat.id;
    }
    
    if (brand) where.brand = { name: brand };
    if (priceMin || priceMax) {
      where.price = {};
      if (priceMin) where.price.gte = Number(priceMin);
      if (priceMax) where.price.lte = Number(priceMax);
    }
    if (search) {
      where.OR = [
        { name: { contains: search, mode: 'insensitive' } },
        { sku: { contains: search, mode: 'insensitive' } }
      ];
    }

    let orderBy = { createdAt: 'desc' };
    if (sort === 'sales') orderBy = { salesCount: 'desc' };
    else if (sort === 'price') orderBy = { price: 'asc' };
    else if (sort === '-price') orderBy = { price: 'desc' };

    const products = await prisma.product.findMany({
      where,
      include: {
        brand: true,
        category: true,
        specifications: true
      },
      orderBy,
      skip: (Number(page) - 1) * Number(limit),
      take: Number(limit)
    });

    const total = await prisma.product.count({ where });

    res.json({
      code: 200,
      data: {
        total,
        page: Number(page),
        limit: Number(limit),
        items: products.map((p) => ({
          id: p.id,
          sku: p.sku,
          name: p.name,
          brand: p.brand?.name,
          price: Number(p.price),
          originalPrice: p.originalPrice ? Number(p.originalPrice) : null,
          stock: p.stockQuantity,
          images: p.images,
          specifications: p.specifications.reduce((acc, s) => {
            acc[s.specKey] = s.specValue + (s.unit || '');
            return acc;
          }, {}),
          salesCount: p.salesCount,
          rating: Number(p.rating || 0)
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
    const product = await prisma.product.findUnique({
      where: { id: Number(req.params.id) },
      include: {
        brand: true,
        category: true,
        specifications: true,
        attributes: true,
        reviews: {
          take: 5,
          orderBy: { createdAt: 'desc' },
          include: { user: { select: { username: true, avatar: true } } }
        }
      }
    });

    if (!product) {
      return res.status(404).json({ code: 404, message: '产品不存在' });
    }

    await prisma.product.update({
      where: { id: product.id },
      data: { viewCount: { increment: 1 } }
    });

    const compatibles = await prisma.compatibility.findMany({
      where: { productAId: product.id },
      include: { productB: { include: { brand: true } } }
    });

    const reviewStats = await prisma.review.aggregate({
      where: { productId: product.id },
      _avg: { rating: true },
      _count: true
    });

    const ratingDist = await prisma.review.groupBy({
      by: ['rating'],
      where: { productId: product.id },
      _count: true
    });

    res.json({
      code: 200,
      data: {
        id: product.id,
        sku: product.sku,
        name: product.name,
        brand: product.brand,
        category: product.category,
        price: Number(product.price),
        originalPrice: product.originalPrice ? Number(product.originalPrice) : null,
        stock: product.stockQuantity,
        description: product.description,
        images: product.images,
        videoUrl: product.videoUrl,
        model3dUrl: product.model3dUrl,
        specifications: product.specifications,
        attributes: product.attributes,
        compatibleProducts: compatibles.map((c) => ({
          id: c.productB.id,
          name: c.productB.name,
          brand: c.productB.brand?.name,
          price: Number(c.productB.price),
          compatibilityType: c.compatibilityType
        })),
        reviews: {
          average: Number(reviewStats._avg.rating || 0).toFixed(1),
          count: reviewStats._count,
          distribution: ratingDist.reduce((acc, r) => {
            acc[r.rating] = r._count;
            return acc;
          }, {})
        },
        recentReviews: product.reviews
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/:id/compatibility', async (req, res) => {
  try {
    const compatibles = await prisma.compatibility.findMany({
      where: { productAId: Number(req.params.id) },
      include: {
        productB: {
          include: { brand: true, category: true }
        }
      }
    });

    res.json({
      code: 200,
      data: compatibles.map((c) => ({
        id: c.productB.id,
        name: c.productB.name,
        brand: c.productB.brand?.name,
        category: c.productB.category?.name,
        price: Number(c.productB.price),
        compatibilityType: c.compatibilityType,
        notes: c.notes
      }))
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
