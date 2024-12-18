package com.devteria.identity.controller;

import com.devteria.identity.dto.response.WebInfoDTO;
import com.devteria.identity.entity.SystemSettings;
import com.devteria.identity.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SystemSettingsController {

    @Autowired
    private SystemSettingsService systemSettingsService;

    /**
     * API để lưu cấu hình website (tên website và icon)
     */
    @PostMapping("/website")
    public ResponseEntity<SystemSettings> saveWebsiteSettings(@RequestBody Map<String, String> websiteSettings) {
        String websiteName = websiteSettings.get("websiteName");
        String websiteIcon = websiteSettings.get("websiteIcon");

        // Lưu cấu hình website
        SystemSettings websiteSetting1 = new SystemSettings();
        websiteSetting1.setConfigGroup("Website");
        websiteSetting1.setConfigKey("websiteName");
        websiteSetting1.setConfigValue(websiteName);
        systemSettingsService.saveOrUpdateSetting(websiteSetting1);

        SystemSettings websiteSetting2 = new SystemSettings();
        websiteSetting2.setConfigGroup("Website");
        websiteSetting2.setConfigKey("websiteIcon");
        websiteSetting2.setConfigValue(websiteIcon);
        systemSettingsService.saveOrUpdateSetting(websiteSetting2);

        return new ResponseEntity<>(websiteSetting1, HttpStatus.CREATED);
    }

    /**
     * API để lưu cấu hình chat (botId, script, token, chatTitle)
     */
    @PostMapping("/chat")
    public ResponseEntity<SystemSettings> saveChatSettings(@RequestBody Map<String, String> chatSettings) {
        String botId = chatSettings.get("botId");
        String script = chatSettings.get("script");
        String token = chatSettings.get("token");
        String chatTitle = chatSettings.get("chatTitle");

        // Lưu các cài đặt chat
        SystemSettings chatSetting1 = new SystemSettings();
        chatSetting1.setConfigGroup("Chat");
        chatSetting1.setConfigKey("botId");
        chatSetting1.setConfigValue(botId);
        systemSettingsService.saveOrUpdateSetting(chatSetting1);

        SystemSettings chatSetting2 = new SystemSettings();
        chatSetting2.setConfigGroup("Chat");
        chatSetting2.setConfigKey("script");
        chatSetting2.setConfigValue(script);
        systemSettingsService.saveOrUpdateSetting(chatSetting2);

        SystemSettings chatSetting3 = new SystemSettings();
        chatSetting3.setConfigGroup("Chat");
        chatSetting3.setConfigKey("token");
        chatSetting3.setConfigValue(token);
        systemSettingsService.saveOrUpdateSetting(chatSetting3);

        SystemSettings chatSetting4 = new SystemSettings();
        chatSetting4.setConfigGroup("Chat");
        chatSetting4.setConfigKey("chatTitle");
        chatSetting4.setConfigValue(chatTitle);
        systemSettingsService.saveOrUpdateSetting(chatSetting4);

        return new ResponseEntity<>(chatSetting1, HttpStatus.CREATED);
    }
    /**
     * Lấy tất cả cấu hình hệ thống.
     *
     * @return Danh sách cấu hình hệ thống
     */
    @GetMapping
    public List<SystemSettings> getAllSettings() {
        return systemSettingsService.getAllSettings();
    }

    /**
     * Lấy cấu hình hệ thống theo nhóm và key.
     *
     * @param configGroup Nhóm cấu hình
     * @param configKey   Key cấu hình
     * @return Cấu hình hệ thống
     */
    @GetMapping("/{configGroup}/{configKey}")
    public SystemSettings getSetting(String configGroup, String configKey) {
        return systemSettingsService.getSetting(configGroup, configKey);
    }
    @GetMapping("/chat-config")
    public ResponseEntity<List<SystemSettings>> getAllChatConfigs() {
        // Lấy cấu hình chat từ nhóm "Chat"
        List<SystemSettings> chatConfigs = systemSettingsService.getSettingsByGroup("Chat");
        if (chatConfigs.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(chatConfigs, HttpStatus.OK);
    }
    @GetMapping("/{configKey}")
    public ResponseEntity<SystemSettings> getChatConfig(@PathVariable String configKey) {
        SystemSettings chatConfig = systemSettingsService.getSetting("Chat", configKey);
        if (chatConfig != null) {
            return new ResponseEntity<>(chatConfig, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/web-info")
    public ResponseEntity<WebInfoDTO> getWebInfo() {
        // Lấy tên website và icon từ hệ thống settings
        String websiteName = systemSettingsService.getSetting("Website", "websiteName").getConfigValue();
        String websiteIcon = systemSettingsService.getSetting("Website", "websiteIcon").getConfigValue();

        // Tạo một đối tượng WebInfoDTO để trả về thông tin website
        WebInfoDTO webInfo = new WebInfoDTO(
                websiteName != null ? websiteName : "Website Name",
                websiteIcon != null ? websiteIcon : "/images/logo/default-icon.png"
        );

        return new ResponseEntity<>(webInfo, HttpStatus.OK);
    }
}

