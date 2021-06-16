package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.ResidentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResidentAccountRepository extends JpaRepository<ResidentAccount, Long> {
    @Query("select ra from ResidentAccount ra where ra.id = :accountId and ra.active = true")
    ResidentAccount getResidentAccount(Long accountId);

    @Query(value = "select ra from ResidentAccount ra where ra.room.id = :roomId and ra.active = true")
    ResidentAccount getResidentAccountByRoom(Long roomId);
}
