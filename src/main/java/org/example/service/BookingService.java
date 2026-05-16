package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.BookingCreateDto;
import org.example.dto.BookingResponceDto;
import org.example.entity.Booking;
import org.example.entity.SportField;
import org.example.entity.User;
import org.example.enums.BookingStatus;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.BookingMapper;
import org.example.repository.BookingRepository;
import org.example.repository.SportFieldRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SportFieldRepository sportFieldRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper mapper;

    @Transactional
    public BookingResponceDto createBooking(BookingCreateDto dto, String username){
        SportField sportField = sportFieldRepository.findById(dto.getFieldId()).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + dto.getFieldId()));

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi " + username + " topilmadi"));

        if (bookingRepository.existsOverLappingBooking(dto.getFieldId(), dto.getStartTime(), dto.getEndTime())) {
            throw new BadRequestException("Bu vaqt oralig'i band: ");
        }
        long hours = Duration.between(dto.getStartTime(), dto.getEndTime()).toHours();
        if (hours < 1) throw new BadRequestException("Bron kamida 1 soat bo'lishi kerak: ");

        BigDecimal totalPrice = sportField.getPriceHour().multiply(new java.math.BigDecimal(hours));

        Booking booking = Booking
                .builder()
                .customer(user)
                .sportField(sportField)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .totalPrice(totalPrice)
                .status(BookingStatus.CONFIRMED)
                .build();
        Booking save = bookingRepository.save(booking);
        BookingResponceDto responceDto = mapper.toDto(save);
        responceDto.setFieldName(sportField.getName());
        responceDto.setCustomerFullName(user.getFullname());
        return responceDto;
    }
}
