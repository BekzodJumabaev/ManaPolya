package org.example.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.example.dto.LoginDto;
import org.example.dto.UserCreateDto;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserRestAuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserCreateDto dto){
        userService.register(dto);
        return ResponseEntity.ok("Muvaffaqiyatli ro'yhatdan o'tdingiz!");
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto){
        return ResponseEntity.ok("Xush kelibsiz");
    }
}
