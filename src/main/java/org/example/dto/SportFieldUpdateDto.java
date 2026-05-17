package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.enums.FieldType;

import java.math.BigDecimal;
import java.time.LocalTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportFieldUpdateDto {

    @NotNull(message = "Maydon IDsi bo'sh bo'lmasligi kerak")
    private Long id;

    @NotBlank(message = "Maydon nomi bo'sh bo'lmasligi kerak")
    private String name;

    @NotBlank(message = "Manzil bo'sh bo'lmasligi kerak")
    private String address;

    private String description;

    @NotNull(message = "Soatlik narx kiritilishi shart")
    private BigDecimal priceHour;

    @NotNull(message = "Maydon turi tanlanishi shart")
    private FieldType fieldType;

    @NotNull(message = "Tuman tanlanishi shart")
    private Long districtId;

    @NotNull(message = "Ochilish vaqti kiritilishi shart")
    private LocalTime openTime;

    @NotNull(message = "Yopilish vaqti kiritilishi shart")
    private LocalTime closeTime;

    private boolean hasShower;
    private boolean hasLight;

    private Double latitude;
    private Double longitude;
}
