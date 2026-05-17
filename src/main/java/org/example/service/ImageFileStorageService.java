package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageFileStorageService {

    private final Path root = Paths.get("uploads");

    public String saveImage(MultipartFile file) {
        try {
            if (Files.notExists(root)) {
                Files.createDirectories(root);
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Rasmni saqlashda xatolik yuz berdi" + e.getMessage());
        }
    }
}
