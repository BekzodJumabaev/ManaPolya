package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.enums.FieldStatus;
import org.example.enums.FieldType;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@SQLRestriction("deleted = false")
public class SportField extends BaseEntity {

    private String name;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal priceHour;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @ManyToOne(fetch = FetchType.LAZY)
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    private LocalTime openTime;
    private LocalTime closeTime;

    private boolean hasShower;
    private boolean hasLight;

    private Double latitude;
    private Double longitude;

    private Double avarageRating = 0.0;
    private Integer ratingCount = 0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "field")
    private List<ImageField> images;

    @Enumerated(EnumType.STRING)
    private FieldStatus status = FieldStatus.PENDING;

}
