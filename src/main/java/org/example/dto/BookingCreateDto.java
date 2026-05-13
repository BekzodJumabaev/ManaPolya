package org.example.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.entity.SportField;
import org.example.entity.User;
import org.example.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    private Long fieldId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
