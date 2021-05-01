package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.ResidentDevice;
import com.wg_planner.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResidentDeviceRepository extends JpaRepository<ResidentDevice, Long> {
}
