package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select r from Room r " +
            "where lower( r.roomName ) like lower(concat( '%', :roomNumberToSearch, '%' )) and r.floor.id = :floorId and r.active = true ")
    Room findRoomByNumber(@Param("roomNumberToSearch") String roomNumberToSearch, @Param("floorId") Long floorId);

    @Query("select r from Room r where r.residentAccount.id = :residentId and r.active = true and r.residentAccount.active = true ")
    Room findRoomByResidentId(Long residentId);

    @Query("select r from Room r where r.id = :roomId and r.active = true ")
    Room findRoomByRoomId(Long roomId);
}
