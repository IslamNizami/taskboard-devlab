package com.islamnizami.taskboarddevlab.mapper;

import com.islamnizami.taskboarddevlab.model.dto.LabelRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.LabelResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Label toEntity(LabelRequestDTO dto);

    LabelResponseDTO toResponseDTO(Label label);

    List<LabelResponseDTO> toResponseDTOList(List<Label> labels);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(LabelRequestDTO dto, @MappingTarget Label entity);
}