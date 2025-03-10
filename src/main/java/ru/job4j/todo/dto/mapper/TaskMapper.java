package ru.job4j.todo.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;

@Mapper
public interface TaskMapper {

    @Mapping(target = "userName", expression = "java(taskEntity.getUser().getName())")
    TaskDto getDtoFromEntity(Task taskEntity);

    Task getEntityFromDto(TaskDto dto);

}
