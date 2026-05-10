package org.example.repository;

import org.example.entity.SportField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportFieldRepository extends JpaRepository<SportField, Long> {
}
