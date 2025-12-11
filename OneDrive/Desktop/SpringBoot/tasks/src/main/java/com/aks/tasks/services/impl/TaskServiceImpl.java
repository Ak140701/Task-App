package com.aks.tasks.services.impl;

import com.aks.tasks.domain.entities.Task;
import com.aks.tasks.domain.entities.TaskList;
import com.aks.tasks.domain.entities.TaskPriority;
import com.aks.tasks.domain.entities.TaskStatus;
import com.aks.tasks.repositories.TaskListRespository;
import com.aks.tasks.repositories.TaskRespository;
import com.aks.tasks.services.TaskListService;
import com.aks.tasks.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRespository taskRespository;
    private final TaskListRespository taskListRespository;

    public TaskServiceImpl(TaskRespository taskRespository, TaskListRespository taskListRespository) {
        this.taskRespository = taskRespository;
        this.taskListRespository = taskListRespository;
    }

    @Override
    public List<Task> listTask(UUID taskListId) {
        return taskRespository.findByTaskListId(taskListId);
    }

    @Transactional
    @Override
    public Task createTask(UUID taskListId, Task task) {
        if(task.getId() != null){
            throw new IllegalArgumentException("Task already has an Id");
        }
        if(task.getTitle() == null || task.getTitle().isBlank()){
            throw new IllegalArgumentException("Task must have a title!");
        }

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority())
                .orElse(TaskPriority.MEDIUM);

        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskList taskList = taskListRespository.findById(taskListId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Task List ID provided"));

        LocalDateTime now  = LocalDateTime.now();
        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now
        );

        return taskRespository.save(taskToSave);
    }

    @Override
    public Optional<Task> getTask(UUID taskListId, UUID taskId) {
        return taskRespository.findByTaskListIdAndId(taskListId,taskId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if(task.getId() == null){
            throw new IllegalArgumentException("Task must have an ID!");
        }
        if(!Objects.equals(taskId,task.getId())){
            throw new IllegalArgumentException("Task IDs do not match");
        }
        if(task.getPriority() == null){
            throw new IllegalArgumentException("Task must have a valid priority");
        }
        if(task.getStatus() == null){
            throw new IllegalArgumentException("Task must have a valid status");
        }

        Task existingTask = taskRespository.findByTaskListIdAndId(taskListId,taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found!"));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdated(LocalDateTime.now());

        return taskRespository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteTask(UUID taskListId, UUID taskId) {
        taskRespository.deleteByTaskListIdAndId(taskListId,taskId);
    }
}
