package com.aks.tasks.repositories;

import com.aks.tasks.domain.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskListRespository extends JpaRepository<TaskList, UUID> {

}
