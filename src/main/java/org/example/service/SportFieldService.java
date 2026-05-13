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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportFieldService {

    private final UserRepository userRepository;
    private final SportFieldRepository sportFieldRepository;
    private final DistrictRepository districtRepository;
    private final ModelMapper modelMapper;

    public SportFieldResponceDto create(String currentUsername, SportFieldCreateDto dto){

        District district = districtRepository.findById(dto.getDistrictId()).orElseThrow(() ->
                new ResourceNotFoundException("Tuman topilmadi:"));

        User owner = userRepository.findByUsername(currentUsername).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi:"));

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

}
