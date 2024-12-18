package com.devteria.identity.dto.request;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RolePermissionUpdate {
    private String role;
    private Set<String> permissions;
}
