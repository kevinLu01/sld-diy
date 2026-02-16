package com.sld.backend.modules.solution.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.common.exception.BusinessException;
import com.sld.backend.common.result.ErrorCode;
import com.sld.backend.common.result.PageResult;
import com.sld.backend.common.enums.OrderStatus;
import com.sld.backend.modules.order.dto.response.OrderVO;
import com.sld.backend.modules.order.entity.Order;
import com.sld.backend.modules.order.entity.OrderItem;
import com.sld.backend.modules.order.mapper.OrderMapper;
import com.sld.backend.modules.order.mapper.OrderItemMapper;
import com.sld.backend.modules.product.entity.Product;
import com.sld.backend.modules.product.mapper.ProductMapper;
import com.sld.backend.modules.solution.dto.response.SolutionCaseVO;
import com.sld.backend.modules.solution.dto.response.SolutionVO;
import com.sld.backend.modules.solution.entity.Solution;
import com.sld.backend.modules.solution.entity.SolutionCase;
import com.sld.backend.modules.solution.entity.SolutionProduct;
import com.sld.backend.modules.solution.mapper.SolutionCaseMapper;
import com.sld.backend.modules.solution.mapper.SolutionMapper;
import com.sld.backend.modules.solution.mapper.SolutionProductMapper;
import com.sld.backend.modules.solution.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 解决方案服务实现
 */
@Service
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionMapper solutionMapper;
    private final SolutionCaseMapper solutionCaseMapper;
    private final SolutionProductMapper solutionProductMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public PageResult<SolutionVO> listSolutions(String industry, String scenario, String temperatureRange, Long page, Long limit) {
        LambdaQueryWrapper<Solution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Solution::getStatus, "published");

        if (industry != null && !industry.isEmpty()) {
            wrapper.eq(Solution::getIndustry, industry);
        }
        if (scenario != null && !scenario.isEmpty()) {
            wrapper.eq(Solution::getScenario, scenario);
        }
        if (temperatureRange != null && !temperatureRange.isEmpty()) {
            wrapper.eq(Solution::getTemperatureRange, temperatureRange);
        }

        wrapper.orderByDesc(Solution::getUsageCount);

        Page<Solution> solutionPage = solutionMapper.selectPage(new Page<>(page, limit), wrapper);
        List<SolutionVO> vos = solutionPage.getRecords().stream()
            .map(this::toVO)
            .collect(Collectors.toList());

        return PageResult.of(solutionPage.getTotal(), solutionPage.getCurrent(), solutionPage.getSize(), vos);
    }

    @Override
    public SolutionVO getSolution(Long id) {
        Solution solution = solutionMapper.selectById(id);
        if (solution == null) {
            throw new BusinessException(ErrorCode.SOLUTION_NOT_FOUND);
        }
        
        // 增加浏览量
        solution.setViewCount(solution.getViewCount() + 1);
        solutionMapper.updateById(solution);
        
        SolutionVO vo = toVO(solution);
        
        // 获取方案包含的产品
        List<SolutionProduct> products = solutionProductMapper.selectBySolutionId(id);
        List<SolutionVO.ProductItemVO> productVOs = new ArrayList<>();
        
        for (SolutionProduct sp : products) {
            Product product = productMapper.selectById(sp.getProductId());
            if (product != null) {
                SolutionVO.ProductItemVO item = new SolutionVO.ProductItemVO();
                item.setProductId(sp.getProductId());
                item.setQuantity(sp.getQuantity());
                item.setNotes(sp.getNotes());
                // SolutionProduct has no isRequired field
                
                SolutionVO.ProductInfoVO info = new SolutionVO.ProductInfoVO();
                info.setId(product.getId());
                info.setName(product.getName());
                info.setPrice(product.getPrice());
                info.setImages(product.getImages());
                item.setProduct(info);
                
                productVOs.add(item);
            }
        }
        vo.setProducts(productVOs);
        
        return vo;
    }

    @Override
    public List<SolutionCaseVO> getSolutionCases(Long solutionId) {
        List<SolutionCase> cases = solutionCaseMapper.selectBySolutionId(solutionId);
        return cases.stream().map(this::toCaseVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrderFromSolution(Long solutionId, Long userId) {
        Solution solution = solutionMapper.selectById(solutionId);
        if (solution == null) {
            throw new BusinessException(ErrorCode.SOLUTION_NOT_FOUND);
        }
        
        // 获取方案产品
        List<SolutionProduct> solutionProducts = solutionProductMapper.selectBySolutionId(solutionId);
        if (solutionProducts.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该方案没有产品");
        }
        
        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (SolutionProduct sp : solutionProducts) {
            Product product = productMapper.selectById(sp.getProductId());
            if (product == null) continue;
            
            // 检查库存
            if (product.getStockQuantity() < sp.getQuantity()) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK, 
                    product.getName() + " 库存不足");
            }
            
            BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(sp.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            OrderItem item = new OrderItem();
            item.setProductId(sp.getProductId());
            item.setProductName(product.getName());
            item.setSku(product.getSku());
            item.setPrice(product.getPrice());
            item.setQuantity(sp.getQuantity());
            item.setTotal(subtotal);
            orderItems.add(item);
            
            // 扣减库存
            product.setStockQuantity(product.getStockQuantity() - sp.getQuantity());
            productMapper.updateById(product);
        }
        
        order.setTotalAmount(totalAmount);
        order.setFinalAmount(totalAmount);
        orderMapper.insert(order);
        
        // 保存订单项
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }
        
        // 增加方案使用次数
        solution.setUsageCount(solution.getUsageCount() + 1);
        solutionMapper.updateById(solution);
        
        return convertToOrderVO(order, orderItems);
    }

    private SolutionVO toVO(Solution solution) {
        SolutionVO vo = new SolutionVO();
        BeanUtils.copyProperties(solution, vo);
        return vo;
    }

    private SolutionCaseVO toCaseVO(SolutionCase solutionCase) {
        SolutionCaseVO vo = new SolutionCaseVO();
        BeanUtils.copyProperties(solutionCase, vo);
        return vo;
    }

    private OrderVO convertToOrderVO(Order order, List<OrderItem> items) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setFinalAmount(order.getFinalAmount());
        vo.setStatus(order.getStatus() != null ? order.getStatus().getCode() : null);
        vo.setCreateTime(order.getCreateTime());
        return vo;
    }

    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "SLD" + dateStr + random;
    }
}
