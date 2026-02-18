# 全系统三步执行清单（2026-02-18）

## Step 1: 全量测试与修复（模块/接口/表/页面流程）

### 1.1 模块与页面清单
- [ ] 前台页面: Home
- [ ] 前台页面: Login/Register
- [ ] 前台页面: Products/ProductDetail
- [ ] 前台页面: Cart
- [ ] 前台页面: Orders
- [ ] 前台页面: DIYTool/DIYProjects
- [ ] 前台页面: Solutions/SolutionDetail
- [ ] 前台页面: Knowledge/KnowledgeDetail
- [ ] 前台页面: UserProfile
- [ ] 后台页面: admin/login + dashboard + products/categories/brands/orders/solutions/articles/users/diy

### 1.2 接口清单（逐模块）
- [ ] health: /api/v1/health, /ready, /live, /details
- [ ] auth: /register, /login, /me
- [ ] categories: list/tree/detail
- [ ] products: list/detail/compatibility/reviews
- [ ] solutions: list/detail/cases/create-order
- [ ] knowledge: list/detail/helpful
- [ ] search: search/suggestions
- [ ] users: profile/update/business/favorites
- [ ] cart: get/add/update/delete/clear（兼容路径）
- [ ] orders: create/list/detail/cancel
- [ ] diy: config/solutions/recommend/validate/projects/share
- [ ] services: create/list/detail/rate
- [ ] admin: stats + products/categories/brands/orders/solutions/articles/users/diy-configs/diy-recommendations CRUD

### 1.3 数据库清单（逐表）
- [ ] t_user
- [ ] t_business_info
- [ ] t_favorite
- [ ] t_category
- [ ] t_brand
- [ ] t_product
- [ ] t_product_spec
- [ ] t_product_attr
- [ ] t_review
- [ ] t_compatibility
- [ ] t_order
- [ ] t_order_item
- [ ] t_cart
- [ ] t_cart_item
- [ ] t_diy_project
- [ ] t_diy_project_item
- [ ] t_diy_config
- [ ] t_diy_recommendation
- [ ] t_solution
- [ ] t_solution_product
- [ ] t_solution_case
- [ ] t_banner
- [ ] t_article
- [ ] t_service_request

### 1.4 业务流程清单
- [ ] 注册->登录->获取个人信息
- [ ] 商品浏览->详情->加购->下单->取消
- [ ] DIY: 选场景->参数->推荐->兼容->保存->分享->分享页查看
- [ ] 解决方案->创建订单
- [ ] 用户收藏增删查
- [ ] 服务工单创建->列表->详情->评价
- [ ] 后台登录->核心 CRUD

### 1.5 修复与回归
- [ ] 汇总缺陷（页面/UI/API/DB）
- [ ] 完成代码修复
- [ ] 回归测试通过
- [ ] 产出 Step1 报告

## Step1 当前结论（2026-02-18 10:53 CST）
- [x] 已完成：前后台核心页面点击 + 接口全链路（注册/登录、DIY、购物车、订单、服务、后台CRUD）
- [x] 已完成：修复并上线（Article字段映射、JWT用户ID回填、DIY projects鉴权、后台分类slug）
- [x] 已完成：回归报告 `agent-progress/reports/step1-full-validation.md`（PASS 37 / WARN 1 / FAIL 0）
- [x] 已完成：DB对表核验（线上 `sld_diy`）
- [ ] 待处理：统一双表体系（`t_*` 与 CamelCase 并存，存在长期维护风险）

## Step 2: 优化建议（功能设计 + 代码专业度）
- [x] 功能设计问题列表
- [x] 交互体验问题列表
- [x] 架构/代码质量问题列表
- [x] 安全/可维护性问题列表
- [x] 优先级改进路线图（P0/P1/P2）

## Step 3: 行业方案与系统匹配分析
- [x] 梳理空调冷冻配件主流场景与方案
- [x] 映射当前系统的数据结构与交互
- [x] 识别不匹配点（字段/流程/推荐逻辑）
- [x] 提出可落地改造建议（产品与技术）
- [x] 产出最终分析报告

## Step2/3 产出文件
- `agent-progress/reports/step2-optimization-recommendations.md`
- `agent-progress/reports/step2-task-breakdown.md`
- `agent-progress/reports/step2-sprint1-board.md`
- `agent-progress/reports/step3-industry-solution-fit.md`
