package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {
    @Query("select r from Room r " +
            "where r.floor.id = :floorIdToSearch")
    List<Room> findAllRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);
    @Query("select r from Room r " +
            "where r.floor.id = :floorIdToSearch and r.occupied = false ")
    List<Room> findAllNonOccupiedRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);
    @Query("select r from Room r " +
            "where r.floor.id = :floorIdToSearch and r.occupied = true and r.residentAccount.away = false ")
    List<Room> findAllOccupiedAndResidentNotAwayRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select f.tasks from Floor f " +
            "where f.id = :floorIdToSearch ")
    List<Task> findAllTasksInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select r from Room r " +
            "where r.floor = :floorIdToSearch and r.residentAccount.away = false ")
    List<Room> findAllAvailableRooms(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select f from Floor f where f.floorName = :floorNumber")
    Floor findFloorByNumber(@Param("floorNumber") String floorNumber);

    @Query("select f from Floor f ")
    List<Floor> findAllFloors();

    @Query("select f from Floor f where f.floorCode = :floorCodeToSearch")
    Floor findFloorByFloorCode(@Param("floorCodeToSearch") String floorCodeToSearch);
}
