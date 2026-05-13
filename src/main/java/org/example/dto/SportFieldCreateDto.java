package org.example.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SportFieldCreateDto {

    @NotBlank(message = "Nomi bosh bo'lmasligi kerak:")
    private String name;

    @NotBlank(message = "Manzil kiritish shart:")
    private String address;

    private String description;

    @NotNull(message = "Soatbay narxi kiritilishi shart:")
    private BigDecimal priceHour;

    @NotNull(message = "Maydon turini kiriting:")
    private FieldType fieldType;

    @NotNull(message = "Tuman tanlanishi kerak:")
    private Long districtId;

    private LocalTime openTime;
    private LocalTime closeTime;

    private boolean hasShower;
    private boolean hasLight;

    private Double latitude;
    private Double longitude;
}
