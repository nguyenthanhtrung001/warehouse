package com.devteria.identity.repository;

import com.devteria.identity.entity.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {
    // Tìm một cài đặt hệ thống dựa trên nhóm và key
    SystemSettings findByConfigGroupAndConfigKey(String configGroup, String configKey);

    // Lấy cấu hình theo nhóm
    List<SystemSettings> findByConfigGroup(String configGroup);
}
