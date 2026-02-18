# Step2 G1/G2/G3 DIY推荐可解释性

## 完成时间
- 2026-02-18

## G1 规则解释模型
- 新增 `DiyRecommendResponse.ExplanationVO`：
  - `productType`
  - `score`
  - `reason`
  - `alternatives[]`

## G2 推荐接口增强
- `POST /api/v1/diy/recommend` 响应新增 `explanations`。
- 对每类推荐输出基础评分与命中原因。

## G3 前台解释UI
- `frontend/src/pages/DIYTool.tsx` 在“选择配件”步骤新增“推荐解释”信息块。
- 展示推荐评分、命中原因与替代建议。

## 影响文件
- `sld-diy-java/src/main/java/com/sld/backend/modules/diy/dto/response/DiyRecommendResponse.java`
- `sld-diy-java/src/main/java/com/sld/backend/modules/diy/service/impl/DiyServiceImpl.java`
- `frontend/src/pages/DIYTool.tsx`
- `frontend/src/types/index.ts`

## 验证
- 后端编译通过：`mvn -DskipTests compile`
- 前端构建通过：`npm run build`
