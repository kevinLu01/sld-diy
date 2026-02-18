package com.sld.backend.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.admin.dto.response.AdminAuditLogVO;
import com.sld.backend.modules.admin.entity.AdminAuditLog;
import com.sld.backend.modules.admin.mapper.AdminAuditLogMapper;
import com.sld.backend.modules.admin.service.AdminAuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAuditLogServiceImpl implements AdminAuditLogService {

    private final AdminAuditLogMapper adminAuditLogMapper;

    @Override
    public void save(AdminAuditLog log) {
        adminAuditLogMapper.insert(log);
    }

    @Override
    public Page<AdminAuditLogVO> query(Long page, Long limit, Long operatorId, String action, String entityType) {
        LambdaQueryWrapper<AdminAuditLog> wrapper = new LambdaQueryWrapper<>();
        if (operatorId != null) {
            wrapper.eq(AdminAuditLog::getOperatorId, operatorId);
        }
        if (action != null && !action.isBlank()) {
            wrapper.eq(AdminAuditLog::getAction, action);
        }
        if (entityType != null && !entityType.isBlank()) {
            wrapper.eq(AdminAuditLog::getEntityType, entityType);
        }
        wrapper.orderByDesc(AdminAuditLog::getCreateTime);

        Page<AdminAuditLog> raw = adminAuditLogMapper.selectPage(new Page<>(page, limit), wrapper);
        List<AdminAuditLogVO> records = raw.getRecords().stream().map(log -> AdminAuditLogVO.builder()
            .id(log.getId())
            .operatorId(log.getOperatorId())
            .action(log.getAction())
            .entityType(log.getEntityType())
            .entityId(log.getEntityId())
            .requestPath(log.getRequestPath())
            .requestMethod(log.getRequestMethod())
            .requestPayload(log.getRequestPayload())
            .resultCode(log.getResultCode())
            .createTime(log.getCreateTime() == null ? null : log.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build()).collect(Collectors.toList());

        Page<AdminAuditLogVO> result = new Page<>(raw.getCurrent(), raw.getSize(), raw.getTotal());
        result.setRecords(records);
        return result;
    }
}
