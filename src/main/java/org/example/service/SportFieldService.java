package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFileldSearchDto;
import org.example.entity.District;
import org.example.entity.SportField;
import org.example.entity.User;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.SportFieldMapper;
import org.example.repository.DistrictRepository;
import org.example.repository.SportFieldRepository;
import org.example.repository.UserRepository;
import org.example.repository.specification.SportFieldSpecification;
import org.example.utils.DataList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportFieldService {

    private final UserRepository userRepository;
    private final SportFieldRepository sportFieldRepository;
    private final DistrictRepository districtRepository;
    private final SportFieldMapper mapper;

    public SportFieldResponceDto create(String currentUsername, SportFieldCreateDto dto){

        District district = districtRepository.findById(dto.getDistrictId()).orElseThrow(() ->
                new ResourceNotFoundException("Tuman topilmadi: " + dto.getDistrictId()));

        User owner = userRepository.findByUsername(currentUsername).orElseThrow(() ->
                new ResourceNotFoundException("Foydalanuvchi topilmadi: " + currentUsername));

        SportField sportField = mapper.toEntity(dto);
        sportField.setDistrict(district);
        sportField.setOwner(owner);
        sportField.setAvarageRating(0.0);
        sportField.setRatingCount(0);

        SportField save = sportFieldRepository.save(sportField);

        SportFieldResponceDto responceDto = mapper.toDto(save);
        responceDto.setDistrictName(district.getDistrictName());
        responceDto.setRegionName(district.getRegion().getRegionName());

        return responceDto;
    }

    public DataList<List<SportFieldResponceDto>> getAll(SportFileldSearchDto searchDto, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());

        if (searchDto == null) {
            searchDto = new SportFileldSearchDto();
        }

        Specification<SportField> search = SportFieldSpecification.buildSpecification(searchDto);
        Page<SportField> fieldPage = sportFieldRepository.findAll(search, pageable);

        List<SportFieldResponceDto> dtoList = mapper.toDtoList(fieldPage.getContent());

        return new DataList<>(dtoList, fieldPage.getTotalElements(), fieldPage.getTotalPages());
    }

    public SportFieldResponceDto getById(long id) {
        SportField sportField = sportFieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + id));

        SportFieldResponceDto responceDto = mapper.toDto(sportField);
        responceDto.setDistrictName(sportField.getDistrict().getDistrictName());
        responceDto.setRegionName(sportField.getDistrict().getRegion().getRegionName());
        return responceDto;
    }


    public List<SportFieldResponceDto> getFieldsByOwner(String username) {
        List<SportField> byOwnerUsername = sportFieldRepository.findByOwnerUsername(username);
        return mapper.toDtoList(byOwnerUsername);
    }
}
