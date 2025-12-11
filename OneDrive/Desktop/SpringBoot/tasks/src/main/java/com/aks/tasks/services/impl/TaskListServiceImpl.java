package com.aks.tasks.services.impl;

import com.aks.tasks.domain.entities.TaskList;
import com.aks.tasks.repositories.TaskListRespository;
import com.aks.tasks.services.TaskListService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskListServiceImpl implements TaskListService {
    private final TaskListRespository taskListRespository;

    public TaskListServiceImpl(TaskListRespository taskListRespository) {
        this.taskListRespository = taskListRespository;
    }

    @Override
    public List<TaskList> listTaskLists() {
        return taskListRespository.findAll();
    }

    @Override
    public TaskList createTaskList(TaskList taskList) {
        if(taskList.getId() != null){
            throw new IllegalArgumentException("Task list already has an ID!");
        }
        if(taskList.getTitle() == null || taskList.getTitle().isBlank()){
            throw new IllegalArgumentException("Task list title must be present!");
        }
        LocalDateTime now = LocalDateTime.now();
        return taskListRespository.save(new TaskList(
                null,
                taskList.getTitle(),
                taskList.getDescription(),
                null,
                now,
                now
        ));
    }

    @Override
    public Optional<TaskList> getTaskList(UUID id) {
        return taskListRespository.findById(id);
    }

    @Transactional
    @Override
    public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
        if(taskList.getId() == null){
            throw new IllegalArgumentException("Task list must have an ID!");
        }

        if(!Objects.equals(taskList.getId(),taskListId)){
            throw new IllegalArgumentException("Attempting to change id!");
        }

        TaskList existingTaskList = taskListRespository.findById(taskListId).orElseThrow(() ->
                new IllegalArgumentException("Task list not found!"));

        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setUpdated(LocalDateTime.now());
        return taskListRespository.save(existingTaskList);
    }

    @Override
    public void deleteTaskList(UUID taskListId) {
        taskListRespository.deleteById(taskListId);
    }
}
