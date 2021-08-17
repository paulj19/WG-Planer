package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.FloorRepository;
import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.apache.juli.logging.LogFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FloorService.class);

    private final TaskRepository taskRepository;
    private final FloorRepository floorRepository;

    @Autowired
    public FloorService(TaskRepository taskRepository, FloorRepository floorRepository) {
        this.taskRepository = taskRepository;
        this.floorRepository = floorRepository;
    }

    public List<Floor> getAllFloors() {
        return floorRepository.findAllFloors();
    }

    public List<Room> getAllNonOccupiedRoomsInFloor(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        List<Room> nonOccupiedRoomsInFloor = floorRepository.findAllNonOccupiedRoomsInFloor(floor.getId());
        if (nonOccupiedRoomsInFloor == null) {
            LOGGER.log(Level.WARNING, "the list of non occupied rooms in floor from DB is null");
        } else if (nonOccupiedRoomsInFloor.isEmpty()) {
            LOGGER.log(Level.INFO, "the list of non occupied rooms in floor from DB is empty");
        }
        return nonOccupiedRoomsInFloor;
    }

    public List<Room> getAllRoomsInFloorByFloorId(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        return floorRepository.findAllRoomsInFloor(floor.getId());
    }

    public List<Room> getAllRoomsInFloorByFloorId(Long floorId) {
        return floorRepository.findAllRoomsInFloor(floorId);
    }

    public List<Room> getAllOccupiedAndResidentNotAwayRooms(@NotNull Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        return floorRepository.findAllOccupiedAndResidentNotAwayRoomsInFloor(floor.getId());
    }

    public Room getNextAvailableRoom(Room room) {
        return getNextAvailableRoom(room.getFloor(), room);
    }

    public Room getNextAvailableRoom(Floor floor, Room room) {
        Validate.notNull(floor, "parameter floor must not be %s", null);
        Validate.notNull(room, "parameter room must not be %s", null);
        Validate.isTrue(floorRepository.findAllRoomsInFloor(floor.getId()).contains(room),
                " parameter " + room.toString() + " not found in set of available rooms in floor" + floor.toString());

        List<Room> roomsInFloor = getAllOccupiedAndResidentNotAwayRooms(floor);
        Validate.notNull(roomsInFloor, "getAllAvailableRooms() returned %s", null);
        Validate.notEmpty(roomsInFloor, "getAllAvailableRooms() returned empty list");

        //returns the room with the next index after the passed room
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

    public Floor getFloorById(long floorId) {
        return floorRepository.findFloorById(floorId);
    }

    @Transactional
    public void deleteTaskAndUpdateFloor(Task task) {
        Validate.notNull(task, "parameter task to delete must not be %s", null);
        task.getFloor().removeTaskFromFloor(task);
        if(task.getAssignedRoom() != null) {
            task.getAssignedRoom().removeAssignedTask(task);
        }
        task.setActive(false);
        save(task.getFloor());
        taskRepository.save(task);
//        taskRepository.delete(task);
    }

    public void save(Floor floorToSave) {
        floorRepository.save(floorToSave);
    }

    public boolean isFloorCodeUnique(String floorCode) {
        Validate.notNull(floorCode, "parameter floorCode should not be %s", null);
        Validate.notEmpty(floorCode, "parameter floorCode should not be empty");
        return !floorRepository.findAllFloorCodes().contains(floorCode);
    }

    public Floor getFloorByFloorCode(String floorCode) {
        Validate.notNull(floorCode, "parameter floorCode should not be %s", null);
        Validate.notEmpty(floorCode, "parameter floorCode should not be empty");
        return floorRepository.findFloorByFloorCode(floorCode);
    }
}