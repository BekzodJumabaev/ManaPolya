package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "field_ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "sport_field_id", nullable = false)
    private Long sportFieldId;

    @Column(name = "stars", nullable = false)
    private Integer stars;
}