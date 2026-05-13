package org.example.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.LoginDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserRestAuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponceDto> signUp(@Valid @RequestBody UserCreateDto dto){
        UserResponceDto responceDto = userService.register(dto);
        return new ResponseEntity<>(responceDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto){
        return ResponseEntity.ok("Xush kelibsiz");
    }
}
