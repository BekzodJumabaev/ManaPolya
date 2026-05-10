package org.example.repository;

import org.example.entity.ImageField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageField, Long> {
}
