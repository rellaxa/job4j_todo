package ru.job4j.todo.dto.mapper;

import org.mapstruct.Mapper;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;

@Mapper
public interface TaskMapper {

    TaskDto getDtoFromEntity(Task entity);

    Task getEntityFromDto(TaskDto dto);

}
