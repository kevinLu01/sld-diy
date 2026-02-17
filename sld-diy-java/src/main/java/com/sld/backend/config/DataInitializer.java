package com.sld.backend.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sld.backend.common.enums.UserStatus;
import com.sld.backend.common.enums.UserType;
import com.sld.backend.common.util.PasswordUtil;
import com.sld.backend.modules.user.entity.User;
import com.sld.backend.modules.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化器
 * 应用启动时自动创建默认管理员账户
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;

    @Override
    public void run(String... args) {
        log.info("开始检查并初始化默认数据...");
        initAdminUser();
        log.info("数据初始化完成");
    }

    /**
     * 初始化默认管理员账户
     */
    private void initAdminUser() {
        // 检查是否已存在管理员账户
        User adminUser = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, "admin")
        );

        if (adminUser != null) {
            log.info("管理员账户已存在，跳过初始化");
            return;
        }

        // 创建默认管理员账户
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@sld.com");
        admin.setPassword(PasswordUtil.encode("admin123"));
        admin.setPhone("13800138000");
        admin.setNickname("系统管理员");
        admin.setUserType(UserType.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());

        userMapper.insert(admin);
        log.info("默认管理员账户创建成功: username=admin, password=admin123");
    }
}
