package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;


    public UserResponceDto register(UserCreateDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new BadRequestException("Parollar bir biriga mos kelmadi");
        }
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BadRequestException("Bu username mavjud");
        }
        User user = modelMapper.map(dto, User.class);
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User save = userRepository.save(user);
        return modelMapper.map(save, UserResponceDto.class);
    }

    public UserResponceDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + id));
        return modelMapper.map(user, UserResponceDto.class);
    }

}
