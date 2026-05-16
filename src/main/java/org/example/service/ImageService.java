package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ImageResponceDto;
import org.example.entity.ImageField;
import org.example.entity.SportField;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.ImageMapper;
import org.example.repository.ImageRepository;
import org.example.repository.SportFieldRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final SportFieldRepository sportFieldRepository;
    private final ImageFileStorageService imageFileStorageService;
    private final ImageRepository imageRepository;
    private final ImageMapper mapper;

    public ImageResponceDto uploadImage(Long fieldId, MultipartFile file) {

        SportField sportField = sportFieldRepository.findById(fieldId).orElseThrow(()
                -> new ResourceNotFoundException("Maydon topilmadi: " + fieldId));

        String fileName = imageFileStorageService.saveImage(file);

        ImageField imageField = new ImageField();
        imageField.setUrl("/uploads/" + fileName);
        imageField.setField(sportField);
        imageRepository.save(imageField);
        return mapper.toDto(imageField);
    }
}
