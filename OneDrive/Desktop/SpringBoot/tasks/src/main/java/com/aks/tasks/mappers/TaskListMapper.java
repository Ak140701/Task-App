package com.aks.tasks.mappers;

import com.aks.tasks.domain.dto.TaskListDto;
import com.aks.tasks.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
