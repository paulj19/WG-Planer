package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.TaskNotificationContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskNotificationContentRepository extends JpaRepository<TaskNotificationContent, Long> {
}
