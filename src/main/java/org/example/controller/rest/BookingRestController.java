package org.example.controller.rest;


import lombok.RequiredArgsConstructor;
import org.example.dto.BookingOfflineCreateDto;
import org.example.dto.BookingResponceDto;
import org.example.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingService bookingService;

    @PostMapping("/offline")
    public ResponseEntity<BookingResponceDto> createOffline(@RequestBody BookingOfflineCreateDto dto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        BookingResponceDto response = bookingService.createOfflineBooking(dto, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
