package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.FloorRepository;
import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FloorService {
    private static final Logger LOGGER = Logger.getLogger(FloorService.class
            .getName());
    private static FloorRepository floorRepositoryStaic;
    private final TaskRepository taskRepository;
    private final FloorRepository floorRepository;

    @Autowired
    public FloorService(TaskRepository taskRepository, FloorRepository floorRepository) {
        this.taskRepository = taskRepository;
        this.floorRepository = floorRepository;
        floorRepositoryStaic = floorRepository;
    }

    //    public List<ResidentAccount> getAllResidents(@NotNull Floor floor) {
//        Validate.notNull(floor, "parameter floor must not be %s", null);
//
//        List<ResidentAccount> residents = new ArrayList<>();
//        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).forEach(residents::add);
//        return residents;
//    }
    public static List<Floor> getAllFloors() {
        return floorRepositoryStaic.findAllFloors();
    }

    public static List<Room> getAllNonOccupiedRoomsInFloor(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        List<Room> nonOccupiedRoomsInFloor = floorRepositoryStaic.findAllNonOccupiedRoomsInFloor(floor.getId());
        if (nonOccupiedRoomsInFloor == null) {
            LOGGER.log(Level.WARNING, "the list of non occupied rooms in floor from DB is null");
        } else if (nonOccupiedRoomsInFloor.isEmpty()) {
            LOGGER.log(Level.INFO, "the list of non occupied rooms in floor from DB is empty");
        }
        return nonOccupiedRoomsInFloor;
    }

    //    public List<ResidentAccount> getAllAvailableResidentsInFloor(@NotNull Floor floor) {
//        Validate.notNull(floor, "parameter floor must not be %s", null);
//        List<ResidentAccount> residents = new ArrayList<>();
//        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).filter(residentAccount -> !residentAccount.isAway()).forEach(residents::add);
//        return residents;
//    }
//
//
    public List<Room> getAllRoomsInFloor(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        return floorRepository.findAllRoomsInFloor(floor.getId());
    }

    public List<Room> getAllOccupiedAndResidentNotAwayRooms(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        return floorRepository.findAllOccupiedAndResidentNotAwayRoomsInFloor(floor.getId());
    }

    /**/
    public Room getNextAvailableRoom(Room room) {
        return getNextAvailableRoom(room.getFloor(), room);
    }

    public Room getNextAvailableRoom(Floor floor, Room room) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        Validate.notNull(room, "parameter room must not be %s", null);

        List<Room> roomsInFloor = getAllOccupiedAndResidentNotAwayRooms(floor);
        Validate.notNull(roomsInFloor, "getAllAvailableRooms() returned %s", null);
        Validate.notEmpty(roomsInFloor, "getAllAvailableRooms() returned empty list");

        roomsInFloor.sort(Comparator.comparing(Room::getRoomName));
        //returns the room with the next index after the passed room
        Assert.isTrue(roomsInFloor.contains(room), "failed to return next available room in getNextAvailableRoom. parameter" + room.toString() + "not found in set of available rooms in floor" + floor.toString());
        return roomsInFloor.get((roomsInFloor.indexOf(room) + 1) % roomsInFloor.size());
    }

    public List<Task> getAllTasksInFloor(Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        return new ArrayList<>(floorRepository.findAllTasksInFloor(floor.getId()));
    }

    public Floor getFloorByName(String floorName) {
        Validate.notNull(floorName, "parameter floor must not be %s", null);
        Validate.notEmpty(floorName, "parameter floor number must not be empty");

        return floorRepository.findFloorByNumber(floorName);
    }

    public void deleteTaskAndUpdateFloor(Floor floor, Task task) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        Validate.notNull(task, "parameter task to delete must not be %s", null);
        Validate.isTrue(floor.getTasks().contains(task), "floor must contain the task to delete");
        floor.removeTaskFromFloor(task);
        task.getAssignedRoom().removeAssignedTask(task);
        save(floor);
        taskRepository.delete(task);
    }

    public void save(Floor floorToSave) {
        floorRepository.save(floorToSave);
    }

    public boolean isFloorCodeUnique(String floorCode) {
        return getFloorByFloorCode(floorCode) == null;
    }

    public Floor getFloorByFloorCode(String floorCode) {
        Validate.notNull(floorCode, "parameter floorCode should not be %s", null);
        Validate.notEmpty(floorCode, "parameter floorCode should not be empty");
        return floorRepository.findFloorByFloorCode(floorCode);
    }

}