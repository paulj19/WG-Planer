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
            "where r.floor.id = :floorIdToSearch and r.active = true ")
    List<Room> findAllRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select r from Room r " +
            "where r.floor.id = :floorIdToSearch and r.occupied = false and r.active = true ")
    List<Room> findAllNonOccupiedRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select r from Room r " +
            "where r.floor.id = :floorIdToSearch and r.occupied = true and r.residentAccount.away = false and r.active = true ")
    List<Room> findAllOccupiedAndResidentNotAwayRoomsInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select t from Task t where t.floor.id = :floorIdToSearch and t.active = true")
    List<Task> findAllTasksInFloor(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select r from Room r " +
            "where r.floor = :floorIdToSearch and r.residentAccount.away = false and r.active = true")
    List<Room> findAllAvailableRooms(@Param("floorIdToSearch") Long floorIdToSearch);

    @Query("select f from Floor f where f.floorName = :floorNumber and f.active = true ")
    Floor findFloorByNumber(@Param("floorNumber") String floorNumber);

    @Query("select f from Floor f where f.id = :floorId and f.active = true ")
    Floor findFloorById(@Param("floorId") long floorId);

    @Query("select f from Floor f where f.active = true ")
    List<Floor> findAllFloors();

    @Query("select f.floorCode from Floor f where f.active = true")
    List<String> findAllFloorCodes();

    @Query("select f from Floor f where f.floorCode = :floorCodeToSearch and f.active = true")
    Floor findFloorByFloorCode(@Param("floorCodeToSearch") String floorCodeToSearch);
}
