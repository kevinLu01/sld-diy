# Step2 B3 控制器重构回归报告

## 执行时间
- 2026-02-18

## 范围
- `@CurrentUserId` 改造相关控制器：
  - Auth/User/ServiceRequest/Solution/DIY/Order/Cart
- 401/403 统一语义。

## 回归结论
- 已验证不再依赖散落 `X-User-Id` 解析，统一从 JWT 获取用户。
- 鉴权异常语义稳定：
  - 未登录 -> 401 + `{code:401}`
  - 无权限 -> 403 + `{code:403}`
- 关键链路接口（`/auth/me` `/users/profile` `/orders` `/cart`）均可正常返回。

## 自动化覆盖
- CI 已接入：
  - `ApiContractSmokeIntegrationTest`
  - `CriticalFlowIntegrationTest`
