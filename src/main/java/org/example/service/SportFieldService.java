package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.entity.District;
import org.example.entity.SportField;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.DistrictRepository;
import org.example.repository.SportFieldRepository;
import org.example.repository.UserRepository;
import org.example.utils.DataList;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SportFieldService {

    private final UserRepository userRepository;
    private final SportFieldRepository sportFieldRepository;
    private final DistrictRepository districtRepository;
    private final ModelMapper modelMapper;

    public SportFieldResponceDto create(String currentUsername, SportFieldCreateDto dto){

        District district = districtRepository.findById(dto.getDistrictId()).orElseThrow(() ->
                new ResourceNotFoundException("Tuman topilmadi: " + dto.getDistrictId()));

        User owner = userRepository.findByUsername(currentUsername).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + currentUsername));

        SportField sportField = modelMapper.map(dto, SportField.class);
        sportField.setDistrict(district);
        sportField.setOwner(owner);
        sportField.setAvarageRating(0.0);
        sportField.setRatingCount(0);

        SportField save = sportFieldRepository.save(sportField);

        SportFieldResponceDto responceDto = modelMapper.map(save, SportFieldResponceDto.class);
        responceDto.setDistrictName(district.getDistrictName());
        responceDto.setRegionName(district.getRegion().getRegionName());

        return responceDto;
    }

    public DataList<List<SportFieldResponceDto>> getAll(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SportField> fieldPage = sportFieldRepository.findByCriteria(search, pageable);

        List<SportFieldResponceDto> dtoList = fieldPage.getContent().stream()
                .map(field -> {
                    SportFieldResponceDto dto = modelMapper.map(field, SportFieldResponceDto.class);
                    dto.setDistrictName(field.getDistrict().getDistrictName());
                    dto.setRegionName(field.getDistrict().getRegion().getRegionName());
                    return dto;
                })
                .collect(Collectors.toList());

        return new DataList<>(dtoList, fieldPage.getTotalElements(), fieldPage.getTotalPages());
    }

}
