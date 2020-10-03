package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a ")
    Account findMyAccount();
    //TODO include accId in the query
    @Query("select a from Account a ")
    Account getAccount(Long accountId);

}
