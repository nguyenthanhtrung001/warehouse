package com.devteria.identity.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    private String newPassword;
}
