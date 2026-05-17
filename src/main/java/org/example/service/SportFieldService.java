package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFieldUpdateDto;
import org.example.dto.SportFileldSearchDto;
import org.example.entity.District;
import org.example.entity.SportField;
import org.example.entity.User;
import org.example.enums.UserRole;
import org.example.exceptions.BadRequestException;
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
import org.springframework.transaction.annotation.Transactional;

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

        if (owner.getRole() == UserRole.CUSTOMER || owner.getRole() == null) {
            owner.setRole(UserRole.OWNER);
            userRepository.save(owner);
        }

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

        for (int i = 0; i < fieldPage.getContent().size(); i++) {
            SportField entity = fieldPage.getContent().get(i);
            SportFieldResponceDto dto = dtoList.get(i);
            dto.setAvarageRating(entity.getAvarageRating() != null ? entity.getAvarageRating() : 0.0);
            dto.setRatingCount(entity.getRatingCount() != null ? entity.getRatingCount() : 0);
        }

        return new DataList<>(dtoList, fieldPage.getTotalElements(), fieldPage.getTotalPages());
    }

    public SportFieldResponceDto getById(long id) {
        SportField sportField = sportFieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + id));

        SportFieldResponceDto responceDto = mapper.toDto(sportField);
        responceDto.setDistrictName(sportField.getDistrict().getDistrictName());
        responceDto.setRegionName(sportField.getDistrict().getRegion().getRegionName());

        responceDto.setAvarageRating(sportField.getAvarageRating() != null ? sportField.getAvarageRating() : 0.0);
        responceDto.setRatingCount(sportField.getRatingCount() != null ? sportField.getRatingCount() : 0);

        return responceDto;
    }


    public List<SportFieldResponceDto> getFieldsByOwner(String username) {
        List<SportField> byOwnerUsername = sportFieldRepository.findByOwnerUsername(username);
        List<SportFieldResponceDto> dtoList = mapper.toDtoList(byOwnerUsername);

        for (int i = 0; i < byOwnerUsername.size(); i++) {
            SportField entity = byOwnerUsername.get(i);
            SportFieldResponceDto dto = dtoList.get(i);
            dto.setAvarageRating(entity.getAvarageRating() != null ? entity.getAvarageRating() : 0.0);
            dto.setRatingCount(entity.getRatingCount() != null ? entity.getRatingCount() : 0);
        }
        return mapper.toDtoList(byOwnerUsername);
    }


    public SportFieldResponceDto update(long id, SportFieldUpdateDto dto, String currentUsername) {

        SportField sportField = sportFieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + id));

        if (!sportField.getOwner().getUsername().equals(currentUsername)) {
            throw new BadRequestException("Siz bu maydonni tahrirlay olmaysix");
        }

        District district = districtRepository.findById(dto.getDistrictId()).orElseThrow(() ->
                new ResourceNotFoundException("Tuman topilmadi" + dto.getDistrictId()));

        mapper.updateEntityFromDto(dto, sportField);
        sportField.setDistrict(district);

        SportField updateEntity = sportFieldRepository.save(sportField);

        SportFieldResponceDto responceDto = mapper.toDto(updateEntity);
        responceDto.setDistrictName(district.getDistrictName());
        responceDto.setRegionName(district.getRegion().getRegionName());
        return responceDto;
    }


    public SportFieldUpdateDto getForUpdate(Long id) {
        SportField sportField = sportFieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + id));
        return mapper.toUpdateDto(sportField);
    }

    public void delete(Long id, String username) {
        SportField sportField = sportFieldRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + id));

        if (!sportField.getOwner().getUsername().equals(username)) {
            throw new org.example.exceptions.BadRequestException("Siz bu maydonni o'chira olmaysiz: ");
        }
        sportField.setDeleted(true);
        sportFieldRepository.save(sportField);
    }

    @Transactional
    public void addRating(Long fieldId, Integer stars) {

        if (stars < 1 || stars > 5) {
            throw new BadRequestException("Xatolik: Reyting balli 1 va 5 oralig'ida bo'lishi shart ");
        }

        SportField sportField = sportFieldRepository.findById(fieldId).orElseThrow(() ->
                new ResourceNotFoundException("Maydon topilmadi: " + fieldId));

        double currentAvg = sportField.getAvarageRating() != null ? sportField.getAvarageRating() : 0.0;
        int currentCount = sportField.getRatingCount() != null ? sportField.getRatingCount() : 0;

        if (currentCount == 0){
            sportField.setAvarageRating(stars.doubleValue());
            sportField.setRatingCount(1);
        }else {
            double newTotalScore = (currentAvg * currentCount) + stars;
            int newCount = currentCount + 1;
            double newAvg = newTotalScore / newCount;

            sportField.setAvarageRating(Math.round(newAvg * 10.0) / 10.0);
            sportField.setRatingCount(newCount);
        }

        sportFieldRepository.save(sportField);
    }
}
