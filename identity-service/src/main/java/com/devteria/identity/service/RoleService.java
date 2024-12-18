package com.devteria.identity.service;

import java.util.HashSet;
import java.util.List;

import com.devteria.identity.entity.Permission;
import com.devteria.identity.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.devteria.identity.dto.request.RoleRequest;
import com.devteria.identity.dto.response.RoleResponse;
import com.devteria.identity.mapper.RoleMapper;
import com.devteria.identity.repository.PermissionRepository;
import com.devteria.identity.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    @Transactional
    public Role updateRolePermissions(String roleName, Set<String> permissions) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Set<Permission> permissionEntities = new HashSet<>();
        for (String permissionName : permissions) {
            Permission permission = permissionRepository.findById(permissionName)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));
            permissionEntities.add(permission);
        }

        role.setPermissions(permissionEntities);
        return roleRepository.save(role);
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
