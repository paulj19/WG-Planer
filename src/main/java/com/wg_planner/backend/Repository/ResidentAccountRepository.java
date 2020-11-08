package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResidentAccountRepository extends JpaRepository<ResidentAccount, Long> {
    //TODO include accId in the query
    @Query("select ra from ResidentAccount ra where ra.id = :accountId ")
    ResidentAccount getResidentAccount(Long accountId);

//    @Query(value = "select ra from ResidentAccount ra where ra.room.id = :roomId ", nativeQuery = true)
    @Query(value = "select ra from ResidentAccount ra where ra.room.id = :roomId ")
    ResidentAccount getResidentAccountByRoom(Long roomId);
//    @Query(value = "select ra from Account ra where ra.userName LIKE :username")
//    ResidentAccount getResidentAccountByUsername(String username);
}
