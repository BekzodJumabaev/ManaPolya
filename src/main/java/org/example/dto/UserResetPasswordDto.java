package org.example.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResetPasswordDto {
    private String phoneNumber;
    private String newPassword;
}