package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProfileDashboardDto;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileSrevice {


    private final UserRepository userRepository;
    private final SportFieldService sportFieldService;
    private final BookingService bookingService;

    public ProfileDashboardDto getDashboardData(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi"));

        ProfileDashboardDto.ProfileDashboardDtoBuilder builder = ProfileDashboardDto.builder()
                .role(user.getRole())
                .myOwnBookings(bookingService.getBookinsByCustomer(username));

        if (user.getRole() == UserRole.OWNER) {
            builder.isOwner(true)
                    .myFields(sportFieldService.getFieldsByOwner(username))
                    .myCustomerBookings(bookingService.getBookinsByOwner(username));
        }else {
            builder.isOwner(false)
                    .myOwnBookings(bookingService.getBookinsByCustomer(username));
        }
        return builder.build();
    }
}
