package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Account, Long> {
}
