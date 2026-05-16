package org.example.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.config.jwt.JwtUtil;
import org.example.dto.LoginDto;
import org.example.dto.LoginResponceDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.entity.User;
import org.example.exceptions.BadRequestException;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserRestAuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponceDto> signUp(@Valid @RequestBody UserCreateDto dto){
        UserResponceDto responceDto = userService.register(dto);
        return new ResponseEntity<>(responceDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponceDto> login(@RequestBody LoginDto dto){
        User user = userRepository.findByUsername(dto.getUsername()).orElseThrow(() ->
                new BadRequestException("Username yoki parol xato"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Username yoki parol xato");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(LoginResponceDto.builder()
                .token(token)
                .username(user.getUsername())
                .build());
    }
}
