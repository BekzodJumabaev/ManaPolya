package org.example.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleNotFound(ResourceNotFoundException ex, HttpServletRequest request){
        if (request.getRequestURI().equals("/api/")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AppErrorDto.builder()
                            .message(ex.getMessage())
                            .status(404)
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build());
        }
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(BadRequestException.class)
    public Object handleBadRequest(BadRequestException ex, HttpServletRequest request){
        if (request.getRequestURI().equals("/api/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AppErrorDto.builder()
                            .message(ex.getMessage())
                            .status(400)
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build());
        }
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    // bu fielddagi error bo'lsa ushlash uchun, yani NotBlank da error bo'lsa ushlab qoladi
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
}
