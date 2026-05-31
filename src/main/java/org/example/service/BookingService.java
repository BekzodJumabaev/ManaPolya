package org.example.service;

import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.example.dto.BookingCreateDto;
import org.example.dto.BookingOfflineCreateDto;
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
import org.springframework.scheduling.annotation.Scheduled;
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

        LocalDateTime startTime = dto.getStartTime().withSecond(0).withNano(0);
        LocalDateTime endTime = dto.getEndTime().withSecond(0).withNano(0);

        if (startTime.isBefore(LocalDateTime.now().withSecond(0).withNano(0).minusMinutes(5))) {
            throw new BadRequestException("Xatolik: O'tib ketgan vaqtdan boshlab bronlay olmaysiz");
        }

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)){
            throw new BadRequestException("Boshlanish vaqt tugash vaqtda oldin bo'lishi kerak");
        }

        LocalTime bookingStart = dto.getStartTime().toLocalTime();
        LocalTime bookingEnd = dto.getEndTime().toLocalTime();
        LocalTime openTime = sportField.getOpenTime();
        LocalTime closeTime = sportField.getCloseTime();

        boolean isValidTime = false;
        if (closeTime.isAfter(openTime)) {
            if (!bookingStart.isBefore(openTime) && !bookingEnd.isAfter(closeTime)) {
                isValidTime = true;
            }
        } else {
            if ((!bookingStart.isBefore(openTime) || bookingStart.isBefore(closeTime)) &&
                    (!bookingEnd.isBefore(openTime) || bookingEnd.isBefore(closeTime))) {
                isValidTime = true;
            }
        }
        if (!isValidTime) {
            throw new BadRequestException("Tanlangan vaqt maydonning ish vaqtiga to'g'ri kelmaydi! " +
                    "Maydon ish vaqti: " + openTime + " - " + closeTime);
        }

        if (bookingRepository.existsOverLappingBooking(dto.getFieldId(), startTime, endTime)) {
            throw new BadRequestException("Kechirasiz bu vaqt oralig'i band qilingan:");
        }

        if (!sportField.getOwner().getUsername().equals(username)) {
            boolean hasActiveBooking = bookingRepository.existsByCustomerUsernameAndStatusAndEndTimeAfter(
                    username, BookingStatus.CONFIRMED, LocalDateTime.now());
            if (hasActiveBooking) {
                throw new BadRequestException("Xatolik: Sizda hozirda faol ijara mavjud! Avvalgi ijarangizni yakunlamasdan yoki uni bekor qilmasdan turib, yangi maydon bron qila olmaysiz.");
            }
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
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

    public List<BookingResponceDto> getBookinsByOwner(String username) {
        List<Booking> allBookingsByOwnerUsername = bookingRepository.findAllBookingsByOwnerUsername(username);
        return mapper.toDtoList(allBookingsByOwnerUsername);
    }

    public List<BookingResponceDto> getBookinsByCustomer(String username) {
        List<Booking> customerBookings = bookingRepository.findByCustomerUsernameOrderByStartTimeDesc(username);
        return mapper.toDtoList(customerBookings);
    }

    public void cancelBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new ResourceNotFoundException("Ijara topilmadi: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return;
        }

        if (!booking.getCustomer().getUsername().equals(username) &&
            !booking.getSportField().getOwner().getUsername().equals(username)) {
            throw new BadRequestException("Xatolik: Siz bu ijarani bekor qilish huquqiga ega emassiz");
        }

        LocalDateTime now = LocalDateTime.now();

        long oyinBoshlanishgachaQolganVaqt = Duration.between(now, booking.getStartTime()).toMinutes();
        long ijaraYaratilgandanHozirgachaVaqt = Duration.between(booking.getCreateAt(), now).toMinutes();

        if (oyinBoshlanishgachaQolganVaqt < 30) {
            if (ijaraYaratilgandanHozirgachaVaqt >10) {
                throw new BadRequestException("Xatolik: O'yin boshlanishiga 30 daqiqadan kam vaqt qoldi! " +
                        "Ushbu ijarani endi bekor qilib bo'lmaydi.");
            }
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Transactional
    public void cancelFutureBookingsForField(Long fieldId) {
        List<Booking> futureBookings = bookingRepository.findBySportFieldIdAndStatusAndEndTimeAfterOrderByStartTimeAsc(
                fieldId, BookingStatus.CONFIRMED, LocalDateTime.now());

        if (!futureBookings.isEmpty()) {
            for (Booking booking : futureBookings) {
                booking.setStatus(BookingStatus.CANCELLED);
            }
            bookingRepository.saveAll(futureBookings);
        }
    }


    @Scheduled(cron = "0 */15 * * * *")
    @Transactional
    public void autoCompletePastBookings() {
        List<Booking> pastBookings = bookingRepository.findByStatusAndEndTimeBefore(
                BookingStatus.CONFIRMED, LocalDateTime.now());

        for (Booking booking : pastBookings) {
            booking.setStatus(BookingStatus.COMPLETED);
        }
        bookingRepository.saveAll(pastBookings);
    }

    public BookingResponceDto createOfflineBooking(BookingOfflineCreateDto dto, String ownerUsername) {
        SportField sportField = sportFieldRepository.findById(dto.getFieldId()).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + dto.getFieldId()));

        if (!sportField.getOwner().getUsername().equals(ownerUsername)) {
            throw new BadRequestException("Xatolik: Siz ushbu maydonning egasi emassiz, offline bron qo'sha olmaysiz!");
        }

        LocalDateTime startTime = dto.getStartTime().withSecond(0).withNano(0);
        LocalDateTime endTime = dto.getEndTime().withSecond(0).withNano(0);

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new BadRequestException("Boshlanish vaqti tugash vaqtidan oldin bo'lishi kerak:");
        }

        if (bookingRepository.existsOverLappingBooking(dto.getFieldId(), startTime, endTime)) {
            throw new BadRequestException("Bu vaqt oralig'i band qilingan:");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = sportField.getPriceHour().multiply(hours);

        Booking booking = Booking.builder()
                .customer(null)
                .sportField(sportField)
                .startTime(startTime)
                .endTime(endTime)
                .totalPrice(totalPrice)
                .status(BookingStatus.CONFIRMED)
                .notes(dto.getNotes())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        BookingResponceDto responseDto = mapper.toDto(savedBooking);
        responseDto.setFieldName(sportField.getName());
        responseDto.setCustomerFullName(dto.getNotes() != null ? dto.getNotes() : "Offline Mijoz");

        return responseDto;
    }
}
