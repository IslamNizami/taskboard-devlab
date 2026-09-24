package com.islamnizami.taskboarddevlab.model.mapper;


import com.islamnizami.taskboarddevlab.model.dto.TaskRequestDTO;
import com.islamnizami.taskboarddevlab.model.dto.TaskResponseDTO;
import com.islamnizami.taskboarddevlab.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(TaskRequestDTO dto);

    TaskResponseDTO toResponseDTO(Task task);


}
