const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const prisma = new PrismaClient();
const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';

const router = require('express').Router();

router.post('/register', async (req, res) => {
  try {
    const { username, email, password, phone, userType } = req.body;

    const existingUser = await prisma.user.findFirst({
      where: { OR: [{ email }, { username }] }
    });

    if (existingUser) {
      return res.status(400).json({
        code: existingUser.email === email ? 1002 : 1001,
        message: existingUser.email === email ? '邮箱已注册' : '用户名已存在'
      });
    }

    const passwordHash = await bcrypt.hash(password, 10);

    const user = await prisma.user.create({
      data: {
        username,
        email,
        passwordHash,
        phone,
        userType: userType || 'personal'
      }
    });

    const token = jwt.sign({ userId: user.id }, JWT_SECRET, { expiresIn: '7d' });

    res.json({
      code: 200,
      message: '注册成功',
      data: {
        userId: user.id,
        username: user.username,
        token
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;

    const user = await prisma.user.findFirst({
      where: { OR: [{ email: username }, { username }] }
    });

    if (!user) {
      return res.status(401).json({ code: 401, message: '用户不存在' });
    }

    const isValid = await bcrypt.compare(password, user.passwordHash);
    if (!isValid) {
      return res.status(401).json({ code: 401, message: '密码错误' });
    }

    const token = jwt.sign({ userId: user.id }, JWT_SECRET, { expiresIn: '7d' });

    res.json({
      code: 200,
      message: '登录成功',
      data: {
        userId: user.id,
        username: user.username,
        email: user.email,
        userType: user.userType,
        token
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ code: 500, message: '服务器错误' });
  }
});

router.get('/me', async (req, res) => {
  try {
    const token = req.headers.authorization?.replace('Bearer ', '');
    if (!token) {
      return res.status(401).json({ code: 401, message: '未登录' });
    }

    const decoded = jwt.verify(token, JWT_SECRET);
    const user = await prisma.user.findUnique({
      where: { id: decoded.userId },
      include: { businessInfo: true }
    });

    if (!user) {
      return res.status(404).json({ code: 404, message: '用户不存在' });
    }

    const { passwordHash, ...userData } = user;
    res.json({ code: 200, data: userData });
  } catch (error) {
    res.status(401).json({ code: 401, message: 'token无效' });
  }
});

module.exports = router;
