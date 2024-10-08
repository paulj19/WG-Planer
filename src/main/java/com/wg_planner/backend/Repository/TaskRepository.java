package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select t from Task t where t.id = :taskId and t.active = true ")
    Task findTaskByTaskId(Long taskId);

    @Query("select t from Task t where t.assignedRoom.id = :roomId and t.active = true ")
    List<Task> findAllTasksOfRoom(Long roomId);
}