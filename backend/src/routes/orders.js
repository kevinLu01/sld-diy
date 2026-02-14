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

function generateOrderNo() {
  const date = new Date();
  const dateStr = date.toISOString().slice(0, 10).replace(/-/g, '');
  const random = Math.floor(Math.random() * 10000).toString().padStart(4, '0');
  return `SLD${dateStr}${random}`;
}

router.post('/', authMiddleware, async (req, res) => {
  try {
    const { items, diyProjectId, shippingAddress, notes, needInstallation } = req.body;

    let totalAmount = 0;
    const orderItems = [];

    for (const item of items) {
      const product = await prisma.product.findUnique({
        where: { id: item.productId }
      });

      if (!product) {
        return res.status(400).json({ code: 400, message: `产品 ${item.productId} 不存在` });
      }

      if (product.stockQuantity < item.quantity) {
        return res.status(400).json({ code: 2001, message: `${product.name} 库存不足` });
      }

      const subtotal = Number(product.price) * item.quantity;
      totalAmount += subtotal;

      orderItems.push({
        productId: product.id,
        productName: product.name,
        productSku: product.sku,
        price: product.price,
        quantity: item.quantity,
        subtotal
      });
    }

    const installationFee = needInstallation ? 800 : 0;
    const finalAmount = totalAmount + installationFee;

    const order = await prisma.order.create({
      data: {
        orderNo: generateOrderNo(),
        userId: req.userId,
        diyProjectId,
        totalAmount,
        installationFee,
        finalAmount,
        shippingInfo: shippingAddress,
        notes,
        status: 'pending',
        items: { create: orderItems }
      },
      include: { items: true }
    });

    for (const item of items) {
      await prisma.product.update({
        where: { id: item.productId },
        data: { stockQuantity: { decrement: item.quantity } }
      });
    }

    res.json({
      code: 200,
      data: {
        orderId: order.id,
        orderNo: order.orderNo,
        totalAmount: Number(order.totalAmount),
        shippingFee: Number(order.shippingFee),
        installationFee: Number(order.installationFee),
        finalAmount: Number(order.finalAmount)
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/', authMiddleware, async (req, res) => {
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

router.get('/:orderNo', authMiddleware, async (req, res) => {
  try {
    const order = await prisma.order.findFirst({
      where: { orderNo: req.params.orderNo, userId: req.userId },
      include: {
        items: { include: { product: { include: { brand: true } } } },
        diyProject: true
      }
    });

    if (!order) {
      return res.status(404).json({ code: 404, message: '订单不存在' });
    }

    res.json({ code: 200, data: order });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/:orderNo/cancel', authMiddleware, async (req, res) => {
  try {
    const order = await prisma.order.findFirst({
      where: { orderNo: req.params.orderNo, userId: req.userId }
    });

    if (!order) {
      return res.status(404).json({ code: 404, message: '订单不存在' });
    }

    if (!['pending', 'paid'].includes(order.status)) {
      return res.status(400).json({ code: 4002, message: '订单状态不允许取消' });
    }

    const items = await prisma.orderItem.findMany({ where: { orderId: order.id } });
    for (const item of items) {
      await prisma.product.update({
        where: { id: item.productId },
        data: { stockQuantity: { increment: item.quantity } }
      });
    }

    await prisma.order.update({
      where: { id: order.id },
      data: { status: 'cancelled' }
    });

    res.json({ code: 200, message: '订单已取消' });
  } catch (error) {
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

module.exports = router;
