package com.klymenko.newmarketapi.mappers;

import com.klymenko.newmarketapi.dto.user.UserDTO;
import com.klymenko.newmarketapi.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUserEntity(UserDTO userDTO);
}
