const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();
const router = require('express').Router();

router.get('/', async (req, res) => {
  try {
    const categories = await prisma.category.findMany({
      where: { isActive: true },
      include: {
        children: true,
        _count: { select: { products: true } }
      },
      orderBy: { sortOrder: 'asc' }
    });

    const result = categories.map((cat) => ({
      id: cat.id,
      name: cat.name,
      slug: cat.slug,
      icon: cat.icon,
      count: cat._count.products,
      children: cat.children.map((child) => ({
        id: child.id,
        name: child.name,
        slug: child.slug,
        count: child._count?.products || 0
      }))
    }));

    res.json({ code: 200, data: result });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/:slug', async (req, res) => {
  try {
    const category = await prisma.category.findUnique({
      where: { slug: req.params.slug },
      include: {
        children: true,
        parent: true
      }
    });

    if (!category) {
      return res.status(404).json({ code: 404, message: '分类不存在' });
    }

    res.json({ code: 200, data: category });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
