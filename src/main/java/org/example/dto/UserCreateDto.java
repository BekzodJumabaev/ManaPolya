package org.example.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.UserRole;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserCreateDto {

    @NotBlank(message = "Username bo'sh bo'lmasligi kerak")
    private String username;

    @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    @Size(min = 6, message = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
    private String password;

    @NotBlank(message = "Parolni tasdiqlash shart")
    private String rePassword;


    @NotBlank(message = "FIO bo'sh bo'lmasligi kerak")
    private String fullname;

    @Pattern(regexp = "^\\+998\\d{9}$", message = "Telofon raqam +998******** formada bo'lishi kerak")
    private String phoneNumber;

    private UserRole role;
}
