package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a ")
    Account findMyAccount();

}
