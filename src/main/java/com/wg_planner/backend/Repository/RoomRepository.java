package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r " +
            "where lower( r.roomNumber ) like lower(concat( '%', :roomNumberToSearch, '%' ))")
    Room search(@Param("roomNumberToSearch") String roomNumberToSearch);
}
