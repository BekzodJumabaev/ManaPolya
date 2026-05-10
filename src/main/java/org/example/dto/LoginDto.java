package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {

    @NotBlank(message = "Username kiritish shart")
    private String username;

    @NotBlank(message = "Parol kiritish shart")
    private String password;
}
