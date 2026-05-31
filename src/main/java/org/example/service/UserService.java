package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.CUSTOMER);
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


    @Transactional
    public void changeUserRole(Long userId, org.example.enums.UserRole newRole) {
        org.example.entity.User user = userRepository.findById(userId).orElseThrow(() ->
                new org.example.exceptions.ResourceNotFoundException("Foydalanuvchi topilmadi"));
        user.setRole(newRole);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        org.example.entity.User user = userRepository.findById(userId).orElseThrow(() ->
                new org.example.exceptions.ResourceNotFoundException("Foydalanuvchi topilmadi"));
        user.setDeleted(true);
        userRepository.save(user);
    }


    @Transactional
    public boolean toggleBlock(Long userId) {
        User user = userRepository.findByIdIncludingDeleted(userId).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + userId));

        boolean newStatus = !user.getDeleted();
        user.setDeleted(newStatus);
        userRepository.save(user);

        return newStatus;
    }

    @Transactional
    public void resetPassword(Long userId) {
        User user = userRepository.findByIdIncludingDeleted(userId).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + userId));

        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }
}
