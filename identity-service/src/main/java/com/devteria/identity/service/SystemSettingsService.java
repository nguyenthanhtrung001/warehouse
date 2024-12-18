package com.devteria.identity.service;

import com.devteria.identity.entity.SystemSettings;
import com.devteria.identity.repository.SystemSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    /**
     * Lưu hoặc cập nhật một cấu hình hệ thống.
     *
     * @param systemSettings Cấu hình hệ thống cần lưu
     * @return Cấu hình hệ thống đã lưu
     */
    @Transactional
    public SystemSettings saveOrUpdateSetting(SystemSettings systemSettings) {
        // Kiểm tra nếu cấu hình đã tồn tại, cập nhật
        SystemSettings existingSetting = systemSettingsRepository.findByConfigGroupAndConfigKey(
                systemSettings.getConfigGroup(), systemSettings.getConfigKey());

        if (existingSetting != null) {
            // Cập nhật giá trị nếu đã tồn tại
            existingSetting.setConfigValue(systemSettings.getConfigValue());
            return systemSettingsRepository.save(existingSetting);
        } else {
            // Nếu không tồn tại, tạo mới và lưu
            return systemSettingsRepository.save(systemSettings);
        }
    }

    /**
     * Lấy cấu hình hệ thống theo nhóm và key.
     *
     * @param configGroup Nhóm cấu hình
     * @param configKey   Key cấu hình
     * @return Cấu hình hệ thống
     */
    public SystemSettings getSetting(String configGroup, String configKey) {
        return systemSettingsRepository.findByConfigGroupAndConfigKey(configGroup, configKey);
    }
    /**
     * Lấy tất cả cấu hình hệ thống.
     *
     * @return Danh sách tất cả các cấu hình hệ thống
     */
    public List<SystemSettings> getAllSettings() {
        return systemSettingsRepository.findAll();
    }
    public List<SystemSettings> getSettingsByGroup(String configGroup) {
        return systemSettingsRepository.findByConfigGroup(configGroup);
    }
}
