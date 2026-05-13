package org.example.repository;

import org.example.entity.District;
import org.example.entity.SportField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportFieldRepository extends JpaRepository<SportField, Long> {
    List<SportField> findByDistrict(District district);
}
