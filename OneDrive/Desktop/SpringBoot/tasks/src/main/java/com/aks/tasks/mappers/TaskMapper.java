package com.aks.tasks.mappers;

import com.aks.tasks.domain.dto.TaskDto;
import com.aks.tasks.domain.entities.Task;

public interface TaskMapper {
    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);
}
