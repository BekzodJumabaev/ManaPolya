package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingOfflineCreateDto {
    private Long fieldId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}
