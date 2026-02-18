# Step2 H3 手机端专项回归报告

## 执行时间
- 2026-02-18

## 测试范围
- 前台移动端视口：390x844
- 核心页面：`/login`、`/diy`、`/cart`、`/user/orders`
- 链路：登录 -> DIY 场景选择 -> 购物车/订单页可访问

## 执行方式
- 使用 `agent-browser` 进行自动化点击与截图。
- 使用 `server-access` 登录 `kevin-hk`，从 `sld-diy-nginx-1` 提取访问日志核对真实请求。

## 结果
- 登录链路可用（`admin/admin123` 登录成功，跳转首页）。
- DIY 第一步“应用场景”选中反馈可见（页面文本可见“已选中：空调系统”）。
- 购物车/订单页在移动视口可加载，无按钮遮挡导致的致命阻塞。
- 服务端请求日志已确认（含 `/api/v1/auth/login`、`/api/v1/diy/recommend`、`/api/v1/cart`、`/api/v1/orders`）。

## 发现与修复
- 问题：DIY 第二步在移动端操作下，`coolingCapacity` 有概率空值提交，后端返回“参数校验失败”。
- 修复：前端表单改造为独立“制冷量单位”字段，并设置默认值：
  - `coolingCapacity=1`
  - `capacityUnit=kW`
- 影响文件：`frontend/src/pages/DIYTool.tsx`

## 截图
- `agent-progress/artifacts/h3-mobile-diy.png`
- `agent-progress/artifacts/h3-mobile-orders.png`

## 日志核对要点（kevin-hk）
- `POST /api/v1/auth/login` 200
- `POST /api/v1/diy/recommend` 200
- `GET /api/v1/cart` 200
- `GET /api/v1/orders?page=1&limit=20` 200

## 结论
- H3 移动端专项回归完成。
- DIY 第二步参数空值问题已在代码层修复，等待部署后复测确认线上行为。
