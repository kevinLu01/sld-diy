# Step2 I2/I3 微信登录开发记录

## 完成时间
- 2026-02-18

## I2 后端实现
- 新增接口：
  - `GET /api/v1/auth/wechat/authorize-url`
  - `POST /api/v1/auth/wechat/login`
- 关键能力：
  - state 生成与 Redis 5 分钟有效期校验（防 CSRF）
  - 通过 code 换取 `access_token/openid/unionid`
  - 拉取微信用户信息并自动创建/登录本地账号
  - 签发现有 JWT，保持登录态一致
- 配置项（`application.yml`）：
  - `auth.wechat.enabled`
  - `auth.wechat.app-id`
  - `auth.wechat.app-secret`
  - `auth.wechat.redirect-uri`
  - `auth.wechat.scope`
- 数据模型：
  - `t_user` 新增 `wechat_openid/wechat_unionid/wechat_nickname/wechat_avatar`
  - 提供迁移/回滚 SQL。

## I3 前端实现
- 登录页“微信登录”按钮接入真实逻辑：
  - 点击后调用后端授权地址接口并跳转微信授权页。
- 在登录页回调处理 `code/state`：
  - 自动调用 `/auth/wechat/login`
  - 获取 JWT 后拉取用户资料并登录。

## 变更文件
- 后端：
  - `sld-diy-java/src/main/java/com/sld/backend/config/WechatAuthProperties.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/auth/controller/AuthController.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/auth/service/AuthService.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/auth/service/impl/AuthServiceImpl.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/auth/dto/request/WechatLoginRequest.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/auth/dto/response/WechatAuthorizeResponse.java`
  - `sld-diy-java/src/main/java/com/sld/backend/modules/user/entity/User.java`
  - `sld-diy-java/src/main/resources/application.yml`
  - `sld-diy-java/src/main/resources/schema.sql`
  - `sld-diy-java/db/migrations/V20260218_02_wechat_login.sql`
  - `sld-diy-java/db/migrations/R20260218_02_wechat_login.sql`
- 前端：
  - `frontend/src/services/auth.ts`
  - `frontend/src/pages/Login.tsx`

## 验证
- 后端编译：`mvn -DskipTests compile` 通过。
- 前端构建：`npm run build` 通过。

## 待联调项（I4 前置）
- 需在服务器注入正式微信参数后，进行真机授权回调联调。
