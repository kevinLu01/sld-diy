-- Step2 I2 rollback: 移除微信登录字段
DROP INDEX uk_t_user_wechat_openid ON t_user;
DROP INDEX idx_t_user_wechat_unionid ON t_user;

ALTER TABLE t_user
    DROP COLUMN wechat_avatar,
    DROP COLUMN wechat_nickname,
    DROP COLUMN wechat_unionid,
    DROP COLUMN wechat_openid;

