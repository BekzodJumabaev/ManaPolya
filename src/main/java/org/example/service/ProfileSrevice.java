package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProfileDashboardDto;
import org.example.dto.SportFieldResponceDto;
import org.example.entity.User;
import org.example.enums.FieldStatus;
import org.example.enums.UserRole;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

            List<SportFieldResponceDto> allOwnerFields = sportFieldService.getFieldsByOwner(username);

            List<SportFieldResponceDto> activeFields = allOwnerFields.stream()
                    .filter(f -> f.getStatus() != null && f.getStatus() == FieldStatus.APPROVED)
                    .collect(Collectors.toList());

            builder.isOwner(true)
                    .myFields(allOwnerFields)
                    .myActiveFields(activeFields)
                    .myCustomerBookings(bookingService.getBookinsByOwner(username));
        }else {
            builder.isOwner(false)
                    .myOwnBookings(bookingService.getBookinsByCustomer(username));
        }
        return builder.build();
    }
}
