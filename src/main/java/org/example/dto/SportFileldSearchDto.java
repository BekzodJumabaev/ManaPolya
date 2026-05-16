package org.example.dto;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SportFileldSearchDto {
    private String search;
    private Long regionId;
    private Long districtId;
}
