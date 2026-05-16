package org.example.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.UserRole;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponceDto {

    private Long id;
    private String username;
    private String fullname;
    private String phoneNumber;
    private UserRole role;
}
