package org.example.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppErrorDto {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String path;
}
