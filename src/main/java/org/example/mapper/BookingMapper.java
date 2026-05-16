package org.example.mapper;


import org.example.dto.BookingCreateDto;
import org.example.dto.BookingResponceDto;
import org.example.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "fieldName", source ="sportField.name")
    @Mapping(target = "customerFullName", source = "customer.fullname")
    BookingResponceDto toDto(Booking booking);

    List<BookingResponceDto> toDtoList(List<Booking> bookings);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "sportField", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toEntity(BookingCreateDto bookingCreateDto);
}
