package org.example.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginResponceDto {
    private String token;
    private String username;
}
