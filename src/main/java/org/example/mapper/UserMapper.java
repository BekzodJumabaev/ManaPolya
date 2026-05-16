package org.example.mapper;

import org.example.dto.UserCreateDto;
import org.example.dto.UserResponceDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponceDto toDto(User user);

    List<UserResponceDto> toDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt",  ignore = true)
    @Mapping(target = "updateAt",  ignore = true)
    @Mapping(target = "deleted",  ignore = true)
    @Mapping(target = "password",  ignore = true)
    User toEntity(UserCreateDto userCreateDto);
}
