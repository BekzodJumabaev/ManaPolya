package org.example.dto;

import lombok.*;
import org.example.enums.UserRole;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponceDto {

    private Long id;
    private String username;
    private String fullname;
    private String phoneNumber;
    private UserRole role;
}
