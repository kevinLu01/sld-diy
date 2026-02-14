const { PrismaClient } = require('@prisma/client');
const jwt = require('jsonwebtoken');

const prisma = new PrismaClient();
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';

const router = require('express').Router();

const authMiddleware = async (req, res, next) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) {
      return res.status(401).json({ code: 401, message: '未登录' });
    }
    const decoded = jwt.verify(token, JWT_SECRET);
    req.userId = decoded.userId;
    next();
  } catch (error) {
    res.status(401).json({ code: 401, message: 'token无效' });
  }
};

router.get('/profile', authMiddleware, async (req, res) => {
  try {
    const user = await prisma.user.findUnique({
      where: { id: req.userId },
      include: { businessInfo: true }
    });
    const { passwordHash, ...userData } = user;
    res.json({ code: 200, data: userData });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.put('/profile', authMiddleware, async (req, res) => {
  try {
    const { phone, avatar } = req.body;
    const user = await prisma.user.update({
      where: { id: req.userId },
      data: { phone, avatar }
    });
    const { passwordHash, ...userData } = user;
    res.json({ code: 200, message: '更新成功', data: userData });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/business-verify', authMiddleware, async (req, res) => {
  try {
    const { companyName, businessLicense, industry, address, contactPerson } = req.body;
    
    const info = await prisma.businessInfo.create({
      data: {
        userId: req.userId,
        companyName,
        businessLicense,
        industry,
        address,
        contactPerson
      }
    });
    
    res.json({ code: 200, message: '提交成功，等待审核', data: info });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/orders', authMiddleware, async (req, res) => {
  try {
    const { status, page = 1, limit = 10 } = req.query;
    const where = { userId: req.userId };
    if (status) where.status = status;

    const orders = await prisma.order.findMany({
      where,
      include: {
        items: { include: { product: true } }
      },
      orderBy: { createdAt: 'desc' },
      skip: (Number(page) - 1) * Number(limit),
      take: Number(limit)
    });

    const total = await prisma.order.count({ where });

    res.json({
      code: 200,
      data: { total, page: Number(page), limit: Number(limit), items: orders }
    });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/favorites', authMiddleware, async (req, res) => {
  try {
    const favorites = await prisma.favorite.findMany({
      where: { userId: req.userId },
      include: { product: true },
      orderBy: { createdAt: 'desc' }
    });
    res.json({ code: 200, data: favorites });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/favorites/:productId', authMiddleware, async (req, res) => {
  try {
    const favorite = await prisma.favorite.create({
      data: {
        userId: req.userId,
        productId: Number(req.params.productId)
      }
    });
    res.json({ code: 200, message: '收藏成功', data: favorite });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.delete('/favorites/:productId', authMiddleware, async (req, res) => {
  try {
    await prisma.favorite.deleteMany({
      where: { userId: req.userId, productId: Number(req.params.productId) }
    });
    res.json({ code: 200, message: '取消收藏成功' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
