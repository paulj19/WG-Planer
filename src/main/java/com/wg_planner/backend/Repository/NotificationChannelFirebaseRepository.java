package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.NotificationChannelFirebase;
import com.wg_planner.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationChannelFirebaseRepository extends JpaRepository<NotificationChannelFirebase, Long> {
}
