package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.DistrictDto;
import org.example.repository.DistrictRepository;
import org.example.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;
    private final RegionRepository regionRepository;

    public List<DistrictDto> getDistrictByRegion(Long regionId) {
        return districtRepository.findAll().stream()
                .filter(d -> d.getRegion().getId().equals(regionId) &&
                        d.getDeleted() != null && !d.getDeleted())
                .map(d -> DistrictDto.builder()
                        .id(d.getId())
                        .districtName(d.getDistrictName())
                        .build())
                .toList();
    }

    public String getCurrentRegionName(Long regionId, Long districtId) {
        if (districtId != null){
            return districtRepository.findById(districtId)
                    .map(d -> d.getDistrictName())
                    .orElse("Butun O'zbekiston");
        }else if (regionId != null){
            return regionRepository.findById(regionId)
                    .map(r -> r.getRegionName())
                    .orElse("Butun O'zbekiston");
        }
        return "Butun O'zbekiston";
    }

}
