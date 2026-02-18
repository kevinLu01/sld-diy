-- Step2 I2: 微信登录字段扩展
ALTER TABLE t_user
    ADD COLUMN wechat_openid VARCHAR(128) NULL COMMENT '微信openid' AFTER nickname,
    ADD COLUMN wechat_unionid VARCHAR(128) NULL COMMENT '微信unionid' AFTER wechat_openid,
    ADD COLUMN wechat_nickname VARCHAR(100) NULL COMMENT '微信昵称' AFTER wechat_unionid,
    ADD COLUMN wechat_avatar VARCHAR(500) NULL COMMENT '微信头像' AFTER wechat_nickname;

CREATE UNIQUE INDEX uk_t_user_wechat_openid ON t_user(wechat_openid);
CREATE INDEX idx_t_user_wechat_unionid ON t_user(wechat_unionid);

