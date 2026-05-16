package org.example.mapper;

import org.example.dto.ImageResponceDto;
import org.example.entity.ImageField;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageResponceDto toDto(ImageField imageField);

    List<ImageResponceDto> toDtoList(List<ImageField> entities);
}
