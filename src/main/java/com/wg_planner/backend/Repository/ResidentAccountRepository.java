package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.ResidentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResidentAccountRepository extends JpaRepository<Account, Long> {
    //TODO include accId in the query
    @Query("select ra from ResidentAccount ra where ra.id = :accountId ")
    ResidentAccount getResidentAccount(Long accountId);
}
