# Step1 回归（修复后）
- 时间: 2026-02-18 10:45:32 CST
- [PASS] auth_register => http=200 code=200 msg=操作成功
- [PASS] auth_login_user => http=200 code=200 msg=操作成功
- [PASS] auth_login_admin => http=200 code=200 msg=操作成功
- [PASS] health => http=200 code=200 msg=操作成功
- [PASS] products_reviews => http=200 code=200 msg=操作成功
- [PASS] solutions_detail => http=200 code=200 msg=操作成功
- [WARN] solutions_create_order => http=200 code=400 msg=该方案没有产品
- [PASS] knowledge_list => http=200 code=200 msg=操作成功
- [PASS] knowledge_detail => http=200 code=200 msg=操作成功
- [PASS] knowledge_helpful => http=200 code=200 msg=操作成功
- [PASS] search_all => http=200 code=200 msg=操作成功
- [PASS] search_suggestions => http=200 code=200 msg=操作成功
- [PASS] users_profile_update => http=200 code=200 msg=操作成功
- [PASS] users_business_verify => http=200 code=200 msg=操作成功
- [PASS] users_fav_add => http=200 code=200 msg=操作成功
- [PASS] users_fav_remove => http=200 code=200 msg=操作成功
- [PASS] services_create => http=200 code=200 msg=操作成功
- [PASS] services_detail => http=200 code=200 msg=操作成功
- [WARN] services_rate => http=200 code=400 msg=服务未完成，无法评价
- [PASS] admin_articles_list => http=200 code=200 msg=操作成功
- [PASS] admin_users_list => http=200 code=200 msg=操作成功

## 汇总
- PASS: 19
- WARN: 2
- FAIL: 0
