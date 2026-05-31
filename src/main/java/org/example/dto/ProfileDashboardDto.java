package org.example.dto;

import lombok.*;
import org.example.enums.UserRole;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDashboardDto {

    private UserRole role;
    private boolean isOwner;

    private List<SportFieldResponceDto> myFields;
    private List<SportFieldResponceDto> myActiveFields;
    private List<BookingResponceDto> myCustomerBookings;
    private List<BookingResponceDto> myOwnBookings;
}
