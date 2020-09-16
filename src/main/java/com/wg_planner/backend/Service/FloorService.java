package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.FloorRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Resident;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class FloorService {
    private final Floor floor;
    private final RoomRepository roomRepository;
    private final TaskRepository taskRepository;
    private final FloorRepository floorRepository;

    @Autowired
    public FloorService(RoomRepository roomRepository, TaskRepository taskRepository, FloorRepository floorRepository, Floor floor) {
        this.roomRepository = roomRepository;
        this.taskRepository = taskRepository;
        this.floorRepository = floorRepository;
        this.floor = floor;

    }

    public List<Resident> getAllResidents() {
        List<Resident> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResident).forEach(residents::add);
        return residents;
    }

    public List<Resident> getAllPresentResidents() {
        List<Resident> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResident).filter(resident -> !resident.isAway()).forEach(residents::add);
        return residents;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(floorRepository.findAllTasksInFloor(floor.getId()));
    }
}
