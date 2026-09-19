package com.islamnizami.taskboarddevlab.mapper;


import com.islamnizami.taskboarddevlab.model.dto.RegisterRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.RegisterResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequestDTO dto);

    RegisterResponseDTO toResponseDTO(User user);
}
