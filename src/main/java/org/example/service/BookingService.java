package org.example.service;

import jdk.dynalink.linker.LinkerServices;
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
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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


        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().equals(dto.getEndTime())) {
            throw new BadRequestException("Boshlanish vaqt tugash vaqtda oldin bo'lishi kerak");
        }

        LocalTime bookingStart = dto.getStartTime().toLocalTime();
        LocalTime bookingEnd = dto.getEndTime().toLocalTime();

        if (bookingStart.isBefore(sportField.getOpenTime()) || bookingEnd.isAfter(sportField.getCloseTime())) {
            throw new BadRequestException("Tanlangan vaqt maydonning ish vaqtiga to'g'ri kelmaydi! " +
                    "Ish vaqti: " +sportField.getOpenTime() + " - " + sportField.getCloseTime());
        }

        if (bookingRepository.existsOverLappingBooking(dto.getFieldId(), dto.getStartTime(), dto.getEndTime())) {
            throw new BadRequestException("Bu vaqt oralig'i band: ");
        }
        long minutes = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();
        if (minutes < 60) throw new BadRequestException("Bron kamida 1 soat bo'lishi kerak: ");

        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = sportField.getPriceHour().multiply(hours);


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

    public List<BookingResponceDto> getActiveBookings(Long fieldId) {
        List<Booking> activeBookings = bookingRepository.findBySportFieldIdAndStatusAndEndTimeAfterOrderByStartTimeAsc(fieldId, BookingStatus.CONFIRMED, LocalDateTime.now());
        return mapper.toDtoList(activeBookings);
    }
}
