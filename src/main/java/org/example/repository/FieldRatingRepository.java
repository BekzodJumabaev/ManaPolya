package org.example.repository;

import org.example.entity.FieldRating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FieldRatingRepository extends JpaRepository<FieldRating, Long> {

    Optional<FieldRating> findByUserIdAndSportFieldId(Long userId, Long sportFieldId);
}