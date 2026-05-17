package org.example.mapper;

import org.example.dto.SportFieldCreateDto;
import org.example.dto.SportFieldResponceDto;
import org.example.dto.SportFieldUpdateDto;
import org.example.entity.SportField;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface SportFieldMapper {


    @Mapping(target = "districtName", source = "district.districtName")
    @Mapping(target = "regionName", source = "district.region.regionName")
    @Mapping(target = "ownerPhone", source = "owner.phoneNumber")
    SportFieldResponceDto toDto(SportField sportField);

    List<SportFieldResponceDto> toDtoList(List<SportField> sportFields);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "avarageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "images", ignore = true)
    SportField toEntity(SportFieldCreateDto createDto);


    @Mapping(source = "district.id", target = "districtId")
    SportFieldUpdateDto toUpdateDto(SportField entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "avarageRating", ignore = true)
    @Mapping(target = "ratingCount", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntityFromDto(SportFieldUpdateDto dto, @MappingTarget SportField entity);
}
