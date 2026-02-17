const { PrismaClient } = require('@prisma/client');
const jwt = require('jsonwebtoken');

const prisma = new PrismaClient();
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';
const router = require('express').Router();

const authMiddleware = async (req, res, next) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) {
      // 匿名用户，设置 userId 为 null
      req.userId = null;
      next();
      return;
    }
    const decoded = jwt.verify(token, JWT_SECRET);
    req.userId = decoded.userId;
    next();
  } catch (error) {
    // Token 无效，也作为匿名用户处理
    req.userId = null;
    next();
  }
};

const requireAuth = async (req, res, next) => {
  if (!req.userId) {
    return res.status(401).json({ code: 401, message: '请先登录' });
  }
  next();
};

router.get('/', authMiddleware, async (req, res) => {
  try {
    // 匿名用户返回空购物车
    if (!req.userId) {
      return res.json({
        code: 200,
        data: {
          items: [],
          totalAmount: 0,
          itemCount: 0
        }
      });
    }

    let cart = await prisma.cart.findUnique({
      where: { userId: req.userId },
      include: {
        items: {
          include: { product: { include: { brand: true } } }
        }
      }
    });

    if (!cart) {
      cart = await prisma.cart.create({
        data: { userId: req.userId },
        include: { items: true }
      });
    }

    const totalAmount = cart.items.reduce((sum, item) => {
      return sum + Number(item.product.price) * item.quantity;
    }, 0);

    res.json({
      code: 200,
      data: {
        items: cart.items.map((item) => ({
          id: item.id,
          product: {
            id: item.product.id,
            name: item.product.name,
            sku: item.product.sku,
            brand: item.product.brand?.name,
            price: Number(item.product.price),
            images: item.product.images,
            stock: item.product.stockQuantity
          },
          quantity: item.quantity,
          subtotal: Number(item.product.price) * item.quantity
        })),
        totalAmount,
        itemCount: cart.items.length
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/add', authMiddleware, requireAuth, async (req, res) => {
  try {
    const { productId, quantity = 1 } = req.body;

    let cart = await prisma.cart.findUnique({
      where: { userId: req.userId }
    });

    if (!cart) {
      cart = await prisma.cart.create({
        data: { userId: req.userId }
      });
    }

    const existingItem = await prisma.cartItem.findUnique({
      where: {
        cartId_productId: { cartId: cart.id, productId }
      }
    });

    if (existingItem) {
      await prisma.cartItem.update({
        where: { id: existingItem.id },
        data: { quantity: existingItem.quantity + quantity }
      });
    } else {
      await prisma.cartItem.create({
        data: { cartId: cart.id, productId: productId, quantity }
      });
    }

    res.json({ code: 200, message: '添加成功' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.put('/items/:itemId', authMiddleware, requireAuth, async (req, res) => {
  try {
    const { quantity } = req.body;

    if (quantity <= 0) {
      await prisma.cartItem.delete({ where: { id: Number(req.params.itemId) } });
    } else {
      await prisma.cartItem.update({
        where: { id: Number(req.params.itemId) },
        data: { quantity }
      });
    }

    res.json({ code: 200, message: '更新成功' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.delete('/items/:itemId', authMiddleware, requireAuth, async (req, res) => {
  try {
    await prisma.cartItem.delete({
      where: { id: Number(req.params.itemId) }
    });
    res.json({ code: 200, message: '删除成功' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.delete('/clear', authMiddleware, requireAuth, async (req, res) => {
  try {
    const cart = await prisma.cart.findUnique({
      where: { userId: req.userId }
    });

    if (cart) {
      await prisma.cartItem.deleteMany({
        where: { cartId: cart.id }
      });
    }

    res.json({ code: 200, message: '清空成功' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
