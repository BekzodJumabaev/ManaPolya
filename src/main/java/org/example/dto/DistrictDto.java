package org.example.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DistrictDto {

    private Long id;
    private String districtName;
}
