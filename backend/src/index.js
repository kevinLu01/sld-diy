const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');

dotenv.config();

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get('/api/v1/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

app.use('/api/v1/auth', require('./routes/auth'));
app.use('/api/v1/users', require('./routes/users'));
app.use('/api/v1/categories', require('./routes/categories'));
app.use('/api/v1/products', require('./routes/products'));
app.use('/api/v1/diy', require('./routes/diy'));
app.use('/api/v1/solutions', require('./routes/solutions'));
app.use('/api/v1/orders', require('./routes/orders'));
app.use('/api/v1/cart', require('./routes/cart'));
app.use('/api/v1/knowledge', require('./routes/knowledge'));

const adminRoutes = require('./routes/admin');

app.get('/api/v1/admin/stats', adminRoutes.getStats);

app.get('/api/v1/admin/products', adminRoutes.getProducts);
app.get('/api/v1/admin/products/:id', adminRoutes.getProduct);
app.post('/api/v1/admin/products', adminRoutes.createProduct);
app.put('/api/v1/admin/products/:id', adminRoutes.updateProduct);
app.delete('/api/v1/admin/products/:id', adminRoutes.deleteProduct);

app.get('/api/v1/admin/categories', adminRoutes.getCategories);
app.post('/api/v1/admin/categories', adminRoutes.createCategory);
app.put('/api/v1/admin/categories/:id', adminRoutes.updateCategory);
app.delete('/api/v1/admin/categories/:id', adminRoutes.deleteCategory);

app.get('/api/v1/admin/brands', adminRoutes.getBrands);
app.post('/api/v1/admin/brands', adminRoutes.createBrand);
app.put('/api/v1/admin/brands/:id', adminRoutes.updateBrand);
app.delete('/api/v1/admin/brands/:id', adminRoutes.deleteBrand);

app.get('/api/v1/admin/orders', adminRoutes.getOrders);
app.get('/api/v1/admin/orders/:id', adminRoutes.getOrder);
app.put('/api/v1/admin/orders/:id', adminRoutes.updateOrderStatus);

app.get('/api/v1/admin/solutions', adminRoutes.getSolutions);
app.get('/api/v1/admin/solutions/:id', adminRoutes.getSolution);
app.post('/api/v1/admin/solutions', adminRoutes.createSolution);
app.put('/api/v1/admin/solutions/:id', adminRoutes.updateSolution);
app.delete('/api/v1/admin/solutions/:id', adminRoutes.deleteSolution);

app.get('/api/v1/admin/articles', adminRoutes.getArticles);
app.get('/api/v1/admin/articles/:id', adminRoutes.getArticle);
app.post('/api/v1/admin/articles', adminRoutes.createArticle);
app.put('/api/v1/admin/articles/:id', adminRoutes.updateArticle);
app.delete('/api/v1/admin/articles/:id', adminRoutes.deleteArticle);

app.get('/api/v1/admin/users', adminRoutes.getUsers);
app.put('/api/v1/admin/users/:id', adminRoutes.updateUserStatus);

// DIY 配置管理
app.get('/api/v1/admin/diy-configs', adminRoutes.getDiyConfigs);
app.post('/api/v1/admin/diy-configs', adminRoutes.createDiyConfig);
app.put('/api/v1/admin/diy-configs/:id', adminRoutes.updateDiyConfig);
app.delete('/api/v1/admin/diy-configs/:id', adminRoutes.deleteDiyConfig);

// DIY 推荐配置管理
app.get('/api/v1/admin/diy-recommendations', adminRoutes.getDiyRecommendations);
app.post('/api/v1/admin/diy-recommendations', adminRoutes.createDiyRecommendation);
app.put('/api/v1/admin/diy-recommendations/:id', adminRoutes.updateDiyRecommendation);
app.delete('/api/v1/admin/diy-recommendations/:id', adminRoutes.deleteDiyRecommendation);

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({
    code: 500,
    message: '服务器内部错误',
    error: process.env.NODE_ENV === 'development' ? err.message : undefined
  });
});

app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});

module.exports = app;
