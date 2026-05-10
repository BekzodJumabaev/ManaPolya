package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.UserRole;

@Getter
@Setter
@Builder
public class UserResponceDto {

    private Long id;
    private String username;
    private String fullname;
    private String phoneNumber;
    private UserRole role;
}
