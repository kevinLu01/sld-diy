# Step2 I1 - 微信登录接入设计（V1）

## 1. 目标
- 支持 H5 微信 OAuth2 登录。
- 首次登录自动创建账号；已存在账号可绑定。
- 最终签发系统 JWT，与现有权限体系兼容。

## 2. 流程
1. 前端点击“微信登录”
- 跳转后端 `/api/v1/auth/wechat/authorize`。

2. 后端构造微信授权地址
- 附带 `state`（防 CSRF）并重定向到微信。

3. 微信回调后端 `/api/v1/auth/wechat/callback`
- 后端用 `code` 换取 `access_token/openid/unionid`。
- 获取用户基础信息（昵称、头像）。

4. 账号处理
- 优先按 `unionid` 匹配；其次按已绑定 openid。
- 无匹配则创建新用户（userType=personal）。
- 已登录状态下可执行“绑定微信”。

5. 签发系统 JWT
- 返回 `{ token, user }` 给前端。

## 3. 数据模型变更
- `t_user` 新增：
  - `wechat_unionid` varchar(128) unique null
  - `wechat_openid` varchar(128) null
  - `wechat_nickname` varchar(100) null
  - `wechat_avatar` varchar(500) null
  - `wechat_bind_time` datetime null

## 4. 接口草案
- `GET /api/v1/auth/wechat/authorize?redirect=/target`
- `GET /api/v1/auth/wechat/callback?code=...&state=...`
- `POST /api/v1/auth/wechat/bind`（登录态）
- `POST /api/v1/auth/wechat/unbind`（登录态，可选）

## 5. 安全要求
- `state` 一次性 + 过期校验（5分钟）。
- 回调 code 仅可使用一次。
- 绑定/解绑操作记录审计日志。
- 对同 unionid 多账号冲突进行拦截与告警。

## 6. 验收标准
- 微信授权成功可登录并返回系统 JWT。
- 取消授权/过期 code/非法 state 均有明确错误提示。
- 绑定后同一微信再次登录可命中同用户。
