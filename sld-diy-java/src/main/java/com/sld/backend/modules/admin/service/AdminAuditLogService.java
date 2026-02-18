package com.sld.backend.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sld.backend.modules.admin.dto.response.AdminAuditLogVO;
import com.sld.backend.modules.admin.entity.AdminAuditLog;

public interface AdminAuditLogService {

    void save(AdminAuditLog log);

    Page<AdminAuditLogVO> query(Long page, Long limit, Long operatorId, String action, String entityType);
}
