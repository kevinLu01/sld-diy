const { PrismaClient } = require('@prisma/client');

const prisma = new PrismaClient();
const router = require('express').Router();

router.get('/', async (req, res) => {
  try {
    const { q, category, page = 1, limit = 10 } = req.query;

    const where = { status: 'published' };
    if (category) where.category = category;
    if (q) {
      where.OR = [
        { title: { contains: q, mode: 'insensitive' } },
        { content: { contains: q, mode: 'insensitive' } }
      ];
    }

    const articles = await prisma.knowledgeArticle.findMany({
      where,
      orderBy: { createdAt: 'desc' },
      skip: (Number(page) - 1) * Number(limit),
      take: Number(limit)
    });

    const total = await prisma.knowledgeArticle.count({ where });

    res.json({
      code: 200,
      data: {
        total,
        page: Number(page),
        limit: Number(limit),
        items: articles.map((a) => ({
          id: a.id,
          title: a.title,
          category: a.category,
          tags: a.tags,
          coverImage: a.coverImage,
          viewCount: a.viewCount,
          helpfulCount: a.helpfulCount,
          author: a.author,
          createdAt: a.createdAt
        }))
      }
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/:id', async (req, res) => {
  try {
    const article = await prisma.knowledgeArticle.findUnique({
      where: { id: Number(req.params.id) }
    });

    if (!article) {
      return res.status(404).json({ code: 404, message: '文章不存在' });
    }

    await prisma.knowledgeArticle.update({
      where: { id: article.id },
      data: { viewCount: { increment: 1 } }
    });

    res.json({ code: 200, data: article });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/:id/helpful', async (req, res) => {
  try {
    await prisma.knowledgeArticle.update({
      where: { id: Number(req.params.id) },
      data: { helpfulCount: { increment: 1 } }
    });

    res.json({ code: 200, message: '感谢您的反馈' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
