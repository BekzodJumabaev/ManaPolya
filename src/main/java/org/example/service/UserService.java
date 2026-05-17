package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.entity.User;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;


    public UserResponceDto register(UserCreateDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new BadRequestException("Parollar bir biriga mos kelmadi");
        }
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BadRequestException("Bu username mavjud");
        }
        User user = mapper.toEntity(dto);
/*
        user.setRole(UserRole.USER);
*/
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User save = userRepository.save(user);
        return mapper.toDto(save);
    }

    public UserResponceDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + id));
        return mapper.toDto(user);
    }

    public String findByUsername(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + name));
        return user.getFullname();
    }
}
