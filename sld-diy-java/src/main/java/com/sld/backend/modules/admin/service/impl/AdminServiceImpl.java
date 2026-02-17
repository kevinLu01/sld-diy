package com.sld.backend.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.enums.OrderStatus;
import com.sld.backend.common.enums.UserStatus;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.modules.admin.dto.request.*;
import com.sld.backend.modules.admin.dto.response.AdminStatsResponse;
import com.sld.backend.modules.admin.service.AdminService;
import com.sld.backend.modules.diy.entity.DiyConfig;
import com.sld.backend.modules.diy.entity.DiyRecommendation;
import com.sld.backend.modules.diy.mapper.DiyConfigMapper;
import com.sld.backend.modules.diy.mapper.DiyRecommendationMapper;
import com.sld.backend.modules.knowledge.entity.Article;
import com.sld.backend.modules.knowledge.mapper.ArticleMapper;
import com.sld.backend.modules.order.entity.Order;
import com.sld.backend.modules.order.mapper.OrderMapper;
import com.sld.backend.modules.product.entity.Brand;
import com.sld.backend.modules.product.entity.Category;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.BrandMapper;
import com.sld.backend.modules.product.mapper.CategoryMapper;
import com.sld.backend.modules.product.mapper.ProductMapper;
import com.sld.backend.modules.solution.entity.Solution;
import com.sld.backend.modules.solution.mapper.SolutionMapper;
import com.sld.backend.modules.user.entity.User;
import com.sld.backend.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理后台服务实现
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;
    private final OrderMapper orderMapper;
    private final SolutionMapper solutionMapper;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;
    private final DiyConfigMapper diyConfigMapper;
    private final DiyRecommendationMapper diyRecommendationMapper;

    @Override
    public AdminStatsResponse getStats() {
        // 统计各种数据
        long totalProducts = productMapper.selectCount(null);
        long totalOrders = orderMapper.selectCount(null);
        long totalUsers = userMapper.selectCount(null);
        long totalSolutions = solutionMapper.selectCount(null);

        // 今日订单数
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long todayOrders = orderMapper.selectCount(
            new LambdaQueryWrapper<Order>().ge(Order::getCreateTime, todayStart)
        );

        AdminStatsResponse response = new AdminStatsResponse();
        response.setTotalProducts(totalProducts);
        response.setTotalOrders(totalOrders);
        response.setTotalUsers(totalUsers);
        response.setTotalSolutions((int) totalSolutions);
        response.setTodayOrders(todayOrders);
        return response;
    }

    // ==================== 产品管理 ====================
    @Override
    public Page<Map<String, Object>> getProducts(Long page, Long limit, String search, String status, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (search != null && !search.isEmpty()) {
            wrapper.like(Product::getName, search).or().like(Product::getSku, search);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Product::getStatus, status);
        }
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Product::getCreatedAt);

        Page<Product> productPage = productMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = productPage.getRecords().stream()
            .map(this::convertProductToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(productPage.getCurrent(), productPage.getSize(), productPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> getProduct(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return convertProductToMap(product);
    }

    @Override
    public Map<String, Object> createProduct(CreateProductRequest request) {
        Product product = new Product();
        BeanUtils.copyProperties(request, product);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.insert(product);
        return convertProductToMap(product);
    }

    @Override
    public Map<String, Object> updateProduct(Long id, UpdateProductRequest request) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        BeanUtils.copyProperties(request, product);
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.updateById(product);
        return convertProductToMap(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productMapper.deleteById(id);
    }

    // ==================== 分类管理 ====================
    @Override
    public Page<Map<String, Object>> getCategories(Long page, Long limit) {
        Page<Category> categoryPage = categoryMapper.selectPage(new Page<>(page, limit), null);
        List<Map<String, Object>> list = categoryPage.getRecords().stream()
            .map(this::convertCategoryToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(categoryPage.getCurrent(), categoryPage.getSize(), categoryPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        categoryMapper.insert(category);
        return convertCategoryToMap(category);
    }

    @Override
    public Map<String, Object> updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "分类不存在");
        }
        BeanUtils.copyProperties(request, category);
        categoryMapper.updateById(category);
        return convertCategoryToMap(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }

    // ==================== 品牌管理 ====================
    @Override
    public Page<Map<String, Object>> getBrands(Long page, Long limit) {
        Page<Brand> brandPage = brandMapper.selectPage(new Page<>(page, limit), null);
        List<Map<String, Object>> list = brandPage.getRecords().stream()
            .map(this::convertBrandToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(brandPage.getCurrent(), brandPage.getSize(), brandPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> createBrand(CreateBrandRequest request) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(request, brand);
        brandMapper.insert(brand);
        return convertBrandToMap(brand);
    }

    @Override
    public Map<String, Object> updateBrand(Long id, UpdateBrandRequest request) {
        Brand brand = brandMapper.selectById(id);
        if (brand == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "品牌不存在");
        }
        BeanUtils.copyProperties(request, brand);
        brandMapper.updateById(brand);
        return convertBrandToMap(brand);
    }

    @Override
    public void deleteBrand(Long id) {
        brandMapper.deleteById(id);
    }

    // ==================== 订单管理 ====================
    @Override
    public Page<Map<String, Object>> getOrders(Long page, Long limit, String status, String search) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            OrderStatus orderStatus = parseOrderStatus(status);
            if (orderStatus != null) {
                wrapper.eq(Order::getStatus, orderStatus);
            }
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = orderPage.getRecords().stream()
            .map(this::convertOrderToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> getOrder(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return convertOrderToMap(order);
    }

    @Override
    public Map<String, Object> updateOrderStatus(Long id, String status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        OrderStatus orderStatus = parseOrderStatus(status);
        if (orderStatus == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的订单状态: " + status);
        }
        order.setStatus(orderStatus);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return convertOrderToMap(order);
    }

    // ==================== 解决方案管理 ====================
    @Override
    public Page<Map<String, Object>> getSolutions(Long page, Long limit) {
        Page<Solution> solutionPage = solutionMapper.selectPage(new Page<>(page, limit), null);
        List<Map<String, Object>> list = solutionPage.getRecords().stream()
            .map(this::convertSolutionToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(solutionPage.getCurrent(), solutionPage.getSize(), solutionPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> getSolution(Long id) {
        Solution solution = solutionMapper.selectById(id);
        if (solution == null) {
            throw new BusinessException(ErrorCode.SOLUTION_NOT_FOUND);
        }
        return convertSolutionToMap(solution);
    }

    @Override
    public Map<String, Object> createSolution(CreateSolutionRequest request) {
        Solution solution = new Solution();
        BeanUtils.copyProperties(request, solution);
        solution.setViewCount(0);
        solution.setUsageCount(0);
        solution.setCreatedAt(LocalDateTime.now());
        solution.setUpdatedAt(LocalDateTime.now());
        solutionMapper.insert(solution);
        return convertSolutionToMap(solution);
    }

    @Override
    public Map<String, Object> updateSolution(Long id, UpdateSolutionRequest request) {
        Solution solution = solutionMapper.selectById(id);
        if (solution == null) {
            throw new BusinessException(ErrorCode.SOLUTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(request, solution);
        solution.setUpdatedAt(LocalDateTime.now());
        solutionMapper.updateById(solution);
        return convertSolutionToMap(solution);
    }

    @Override
    public void deleteSolution(Long id) {
        solutionMapper.deleteById(id);
    }

    // ==================== 文章管理 ====================
    @Override
    public Page<Map<String, Object>> getArticles(Long page, Long limit, String category, String search) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Article::getCategory, category);
        }
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> articlePage = articleMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = articlePage.getRecords().stream()
            .map(this::convertArticleToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> getArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }
        return convertArticleToMap(article);
    }

    @Override
    public Map<String, Object> createArticle(CreateArticleRequest request) {
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        article.setViewCount(0);
        article.setHelpfulCount(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);
        return convertArticleToMap(article);
    }

    @Override
    public Map<String, Object> updateArticle(Long id, UpdateArticleRequest request) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }
        BeanUtils.copyProperties(request, article);
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);
        return convertArticleToMap(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleMapper.deleteById(id);
    }

    // ==================== 用户管理 ====================
    @Override
    public Page<Map<String, Object>> getUsers(Long page, Long limit, String search) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (search != null && !search.isEmpty()) {
            wrapper.like(User::getUsername, search).or().like(User::getEmail, search);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> userPage = userMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = userPage.getRecords().stream()
            .map(this::convertUserToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> updateUserStatus(Long id, String status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        // 更新状态
        UserStatus userStatus = parseUserStatus(status);
        if (userStatus == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "无效的用户状态: " + status);
        }
        user.setStatus(userStatus);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return convertUserToMap(user);
    }
    
    private UserStatus parseUserStatus(String status) {
        for (UserStatus s : UserStatus.values()) {
            if (s.getCode().equals(status)) {
                return s;
            }
        }
        return null;
    }

    // ==================== DIY配置管理 ====================
    @Override
    public Page<Map<String, Object>> getDiyConfigs(Long page, Long limit, String category) {
        LambdaQueryWrapper<DiyConfig> wrapper = new LambdaQueryWrapper<>();
        if (category != null && !category.isEmpty()) {
            wrapper.eq(DiyConfig::getCategory, category);
        }
        Page<DiyConfig> configPage = diyConfigMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = configPage.getRecords().stream()
            .map(this::convertDiyConfigToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(configPage.getCurrent(), configPage.getSize(), configPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> createDiyConfig(CreateDiyConfigRequest request) {
        DiyConfig config = new DiyConfig();
        BeanUtils.copyProperties(request, config);
        config.setKey(request.getKey());
        config.setValue(request.getValue());
        config.setIsActive(true);
        diyConfigMapper.insert(config);
        return convertDiyConfigToMap(config);
    }

    @Override
    public Map<String, Object> updateDiyConfig(Long id, UpdateDiyConfigRequest request) {
        DiyConfig config = diyConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "配置不存在");
        }
        BeanUtils.copyProperties(request, config);
        diyConfigMapper.updateById(config);
        return convertDiyConfigToMap(config);
    }

    @Override
    public void deleteDiyConfig(Long id) {
        diyConfigMapper.deleteById(id);
    }

    // ==================== DIY推荐配置管理 ====================
    @Override
    public Page<Map<String, Object>> getDiyRecommendations(Long page, Long limit, String scenario) {
        LambdaQueryWrapper<DiyRecommendation> wrapper = new LambdaQueryWrapper<>();
        if (scenario != null && !scenario.isEmpty()) {
            wrapper.eq(DiyRecommendation::getScenario, scenario);
        }
        Page<DiyRecommendation> recPage = diyRecommendationMapper.selectPage(new Page<>(page, limit), wrapper);
        List<Map<String, Object>> list = recPage.getRecords().stream()
            .map(this::convertDiyRecommendationToMap)
            .collect(Collectors.toList());
        Page<Map<String, Object>> result = new Page<>(recPage.getCurrent(), recPage.getSize(), recPage.getTotal());
        result.setRecords(list);
        return result;
    }

    @Override
    public Map<String, Object> createDiyRecommendation(CreateDiyRecommendationRequest request) {
        DiyRecommendation rec = new DiyRecommendation();
        BeanUtils.copyProperties(request, rec);
        rec.setIsActive(true);
        diyRecommendationMapper.insert(rec);
        return convertDiyRecommendationToMap(rec);
    }

    @Override
    public Map<String, Object> updateDiyRecommendation(Long id, UpdateDiyRecommendationRequest request) {
        DiyRecommendation rec = diyRecommendationMapper.selectById(id);
        if (rec == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "推荐配置不存在");
        }
        BeanUtils.copyProperties(request, rec);
        diyRecommendationMapper.updateById(rec);
        return convertDiyRecommendationToMap(rec);
    }

    @Override
    public void deleteDiyRecommendation(Long id) {
        diyRecommendationMapper.deleteById(id);
    }

    // ==================== 转换方法 ====================
    private Map<String, Object> convertProductToMap(Product product) {
        Category category = categoryMapper.selectById(product.getCategoryId());
        Brand brand = brandMapper.selectById(product.getBrandId());

        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("name", product.getName());
        map.put("sku", product.getSku());
        map.put("price", product.getPrice());
        map.put("stock", product.getStockQuantity());
        map.put("stockQuantity", product.getStockQuantity());
        map.put("status", product.getStatus());
        map.put("images", product.getImages());
        map.put("brandId", product.getBrandId());
        map.put("categoryId", product.getCategoryId());
        map.put("brandName", brand != null ? brand.getName() : null);
        map.put("categoryName", category != null ? category.getName() : null);
        map.put("brand", brand != null ? convertBrandToMap(brand) : null);
        map.put("category", category != null ? convertCategoryToMap(category) : null);
        map.put("createTime", product.getCreatedAt());
        map.put("createdAt", product.getCreatedAt());
        return map;
    }

    private Map<String, Object> convertCategoryToMap(Category category) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", category.getId());
        map.put("name", category.getName());
        map.put("slug", category.getSlug());
        return map;
    }

    private Map<String, Object> convertBrandToMap(Brand brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", brand.getId());
        map.put("name", brand.getName());
        map.put("logo", brand.getLogo());
        return map;
    }

    private Map<String, Object> convertOrderToMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", order.getId());
        map.put("orderNo", order.getOrderNo());
        map.put("totalAmount", order.getTotalAmount());
        map.put("status", order.getStatus());
        map.put("createTime", order.getCreateTime());
        map.put("createdAt", order.getCreateTime());
        return map;
    }

    private Map<String, Object> convertSolutionToMap(Solution solution) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", solution.getId());
        map.put("title", solution.getTitle());
        map.put("industry", solution.getIndustry());
        map.put("totalPrice", solution.getTotalPrice());
        map.put("usageCount", solution.getUsageCount() != null ? solution.getUsageCount() : 0);
        map.put("viewCount", solution.getViewCount() != null ? solution.getViewCount() : 0);
        map.put("status", solution.getStatus());
        map.put("createTime", solution.getCreatedAt());
        map.put("createdAt", solution.getCreatedAt());
        return map;
    }

    private Map<String, Object> convertArticleToMap(Article article) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", article.getId());
        map.put("title", article.getTitle());
        map.put("category", article.getCategory());
        map.put("status", article.getPublishStatus());
        map.put("viewCount", article.getViewCount() != null ? article.getViewCount() : 0);
        map.put("helpfulCount", article.getHelpfulCount() != null ? article.getHelpfulCount() : 0);
        map.put("author", article.getAuthor());
        map.put("createTime", article.getCreateTime());
        map.put("createdAt", article.getCreateTime());
        return map;
    }

    private Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("status", user.getStatus() != null ? user.getStatus().getCode() : null);
        map.put("createTime", user.getCreateTime());
        map.put("createdAt", user.getCreateTime());
        return map;
    }

    private Map<String, Object> convertDiyConfigToMap(DiyConfig config) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", config.getId());
        map.put("category", config.getCategory());
        map.put("key", config.getKey());
        map.put("label", config.getLabel());
        map.put("value", config.getValue());
        map.put("sortOrder", config.getSortOrder());
        map.put("isActive", config.getIsActive());
        return map;
    }

    private OrderStatus parseOrderStatus(String status) {
        for (OrderStatus s : OrderStatus.values()) {
            if (s.getCode().equals(status)) {
                return s;
            }
        }
        return null;
    }

    private Map<String, Object> convertDiyRecommendationToMap(DiyRecommendation rec) {
        Category category = categoryMapper.selectById(rec.getCategoryId());

        Map<String, Object> map = new HashMap<>();
        map.put("id", rec.getId());
        map.put("scenario", rec.getScenario());
        map.put("productType", rec.getProductType());
        map.put("categoryId", rec.getCategoryId());
        map.put("category", category != null ? convertCategoryToMap(category) : null);
        map.put("priority", rec.getPriority());
        map.put("isRequired", rec.getIsRequired());
        map.put("minQuantity", rec.getMinQuantity());
        map.put("maxQuantity", rec.getMaxQuantity());
        map.put("isActive", rec.getIsActive());
        return map;
    }
}
