package org.example.dto;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.District;
import org.example.entity.ImageField;
import org.example.entity.User;
import org.example.enums.FieldType;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SportFieldResponceDto {

    private Long id;
    private String name;
    private String address;
    private String description;
    private BigDecimal priceHour;
    private FieldType fieldType;
    private String districtName;
    private String regionName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean hasShower;
    private boolean hasLight;
    private Double latitude;
    private Double longitude;
    private Double avarageRating;

}
