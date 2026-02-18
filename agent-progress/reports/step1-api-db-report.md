# Step1 接口+DB 全量测试报告

- 时间: 2026-02-18 09:41:10 CST
- 环境: production ffjj.sldbd.com:9000

## A. 基础与认证
- [PASS] health => http=200 code=200 msg=操作成功
- [PASS] health/ready => http=200 code=200 msg=操作成功
- [PASS] health/live => http=200 code=200 msg=操作成功
- [PASS] health/details => http=200 code=200 msg=操作成功
- [PASS] auth/register(user) => http=200 code=200 msg=操作成功
- [PASS] auth/login(user) => http=200 code=200 msg=操作成功
- [PASS] auth/login(admin) => http=200 code=200 msg=操作成功

## B. 前台业务接口
- [PASS] auth/me => http=200 code=200 msg=操作成功
- [PASS] categories/list => http=200 code=200 msg=操作成功
- [PASS] categories/tree => http=200 code=200 msg=操作成功
- [PASS] categories/detail => http=200 code=200 msg=操作成功
- [PASS] products/list => http=200 code=200 msg=操作成功
- [PASS] products/detail => http=200 code=200 msg=操作成功
- [PASS] products/compatibility => http=200 code=200 msg=操作成功
- [WARN] products/reviews => http=200 code=500 msg=服务器内部错误
- [PASS] solutions/list => http=200 code=200 msg=操作成功
- [WARN] solutions/detail => http=200 code=500 msg=服务器内部错误
- [PASS] solutions/cases => http=200 code=200 msg=操作成功
- [WARN] solutions/create-order => http=200 code=500 msg=服务器内部错误
- [WARN] knowledge/list => http=200 code=500 msg=服务器内部错误
- [WARN] knowledge/detail => http=200 code=500 msg=服务器内部错误
- [WARN] knowledge/helpful => http=200 code=500 msg=服务器内部错误
- [FAIL] search/all => http=400 code=NA msg=NA
- [FAIL] search/suggestions => http=400 code=NA msg=NA
- [PASS] users/profile => http=200 code=200 msg=操作成功
- [WARN] users/profile/update => http=200 code=500 msg=服务器内部错误
- [WARN] users/business-verify => http=200 code=500 msg=服务器内部错误
- [WARN] users/favorites/add => http=200 code=500 msg=服务器内部错误
- [PASS] users/favorites/list => http=200 code=200 msg=操作成功
- [WARN] users/favorites/remove => http=200 code=500 msg=服务器内部错误
- [PASS] cart/get => http=200 code=200 msg=操作成功
- [PASS] cart/add => http=200 code=200 msg=操作成功
- [PASS] cart/update => http=200 code=200 msg=操作成功
- [PASS] cart/list => http=200 code=200 msg=操作成功
- [PASS] orders/create => http=200 code=200 msg=操作成功
- [PASS] orders/detail => http=200 code=200 msg=操作成功
- [PASS] orders/cancel => http=200 code=200 msg=操作成功
- [PASS] orders/list => http=200 code=200 msg=操作成功
- [PASS] cart/clear => http=200 code=200 msg=操作成功
- [PASS] diy/config => http=200 code=200 msg=操作成功
- [PASS] diy/solutions => http=200 code=200 msg=操作成功
- [PASS] diy/recommend => http=200 code=200 msg=操作成功
- [PASS] diy/validate => http=200 code=200 msg=操作成功
- [PASS] diy/projects/save => http=200 code=200 msg=操作成功
- [PASS] diy/projects/list => http=200 code=200 msg=操作成功
- [PASS] diy/projects/detail => http=200 code=200 msg=操作成功
- [PASS] diy/projects/share => http=200 code=200 msg=操作成功
- [PASS] diy/share/token => http=200 code=200 msg=操作成功
- [WARN] services/create => http=200 code=500 msg=服务器内部错误
- [PASS] services/list => http=200 code=200 msg=操作成功

## C. 后台管理接口
- [PASS] admin/stats => http=200 code=200 msg=操作成功
- [PASS] admin/products/list => http=200 code=200 msg=操作成功
- [PASS] admin/categories/list => http=200 code=200 msg=操作成功
- [PASS] admin/brands/list => http=200 code=200 msg=操作成功
- [PASS] admin/orders/list => http=200 code=200 msg=操作成功
- [PASS] admin/solutions/list => http=200 code=200 msg=操作成功
- [PASS] admin/articles/list => http=200 code=200 msg=操作成功
- [PASS] admin/users/list => http=200 code=200 msg=操作成功
- [PASS] admin/diy-configs/list => http=200 code=200 msg=操作成功
- [PASS] admin/diy-recommendations/list => http=200 code=200 msg=操作成功
- [PASS] admin/categories/create => http=200 code=200 msg=操作成功
- [PASS] admin/categories/delete => http=200 code=200 msg=操作成功
- [PASS] admin/brands/create => http=200 code=200 msg=操作成功
- [PASS] admin/brands/delete => http=200 code=200 msg=操作成功

## D. 数据库对账（关键表行数与一致性）
- [INFO] table=t_user rows=9
- [INFO] table=User rows=3
- [INFO] table=t_product rows=0
- [INFO] table=Product rows=5
- [INFO] table=t_diy_project rows=2
- [INFO] table=DiyProject rows=0
- [INFO] table=t_diy_config rows=0
- [INFO] table=DiyConfig rows=16
- [INFO] table=t_diy_recommendation rows=0
- [INFO] table=DiyRecommendation rows=15
- [INFO] table=t_order rows=5
- [INFO] table=Order rows=0

## E. 汇总
- PASS: 51
- WARN: 11
- FAIL: 2
- 结论: 存在失败项，需修复后回归
