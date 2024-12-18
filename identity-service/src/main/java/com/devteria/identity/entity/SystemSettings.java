package com.devteria.identity.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "system_settings")
public class SystemSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Nhóm cấu hình
    @Column(name = "config_group", nullable = false)
    String configGroup;

    // Khóa cài đặt (key)
    @Column(name = "config_key", nullable = false)
    String configKey;

    // Giá trị của cài đặt
    @Column(name = "config_value", columnDefinition = "TEXT")
    String configValue;

    // Mô tả về cài đặt
    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    // Thông tin metadata bổ sung, có thể lưu dưới dạng JSON
    @Column(name = "metadata", columnDefinition = "TEXT")
    String metadata;

    // Trạng thái cài đặt (active/inactive)
    @Column(name = "active", nullable = false)
    Boolean active = true;
}
