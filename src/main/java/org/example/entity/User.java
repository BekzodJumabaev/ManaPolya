package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.UserRole;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullname;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
