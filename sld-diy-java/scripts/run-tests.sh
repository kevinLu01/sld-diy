#!/bin/bash
# 运行测试脚本

set -e

echo "================================"
echo "运行测试"
echo "================================"

# 运行单元测试
echo "1. 运行单元测试..."
mvn test -Dtest="*Test" -DfailIfNoTests=false

# 生成测试报告
echo "2. 生成测试报告..."
mvn jacoco:report

# 运行集成测试
echo "3. 运行集成测试..."
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false -P integration-test

echo "================================"
echo "测试完成"
echo "================================"
