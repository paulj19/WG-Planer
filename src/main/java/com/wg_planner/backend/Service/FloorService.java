package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@Service
public class FloorService {
    private static final Logger LOGGER = Logger.getLogger(FloorService.class
            .getName());
    private static FloorRepository floorRepositoryStaic;
    private final RoomRepository roomRepository;
    private final TaskRepository taskRepository;
    private final FloorRepository floorRepository;
    private final AccountRepository accountRepository;
    private final ResidentAccountRepository residentAccountRepository;

    @Autowired
    public FloorService(RoomRepository roomRepository, TaskRepository taskRepository, FloorRepository floorRepository, AccountRepository accountRepository, ResidentAccountRepository residentAccountRepository) {
        this.roomRepository = roomRepository;
        this.taskRepository = taskRepository;
        this.floorRepository = floorRepository;
        floorRepositoryStaic = floorRepository;
        this.accountRepository = accountRepository;
        this.residentAccountRepository = residentAccountRepository;
    }

    public List<ResidentAccount> getAllResidents(Floor floor) {
        List<ResidentAccount> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).forEach(residents::add);
        return residents;
    }

    public static List<Floor> getAllFloors() {
        return floorRepositoryStaic.findAllFloors();
    }

    public static List<Room> getAllNonOccupiedRoomsInFloor(Floor floor) {
        if (floor != null) {
            return floorRepositoryStaic.findAllNonOccupiedRoomsInFloor(floor.getId());
        } else {
            LOGGER.log(Level.SEVERE, "getAllNonOccupiedRoomsInFloor: floor parameter is null");
            return null;
        }
    }

    public List<ResidentAccount> getAllAvailableResidents(Floor floor) {
        List<ResidentAccount> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).filter(residentAccount -> !residentAccount.isAway()).forEach(residents::add);
        return residents;
    }

    public List<Room> getAllAvailableRooms(Floor floor) {
        List<Room> rooms = new ArrayList<>();
//        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).filter(residentAccount -> !residentAccount.isAway()).forEach(rooms::add);
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().filter(room -> {
            if (room.isOccupied()) {
                return !room.getResidentAccount().isAway();
            }
            return false;
        }).forEach(rooms::add);
        return rooms;
    }

    public Room getNextAvailableRoom(Floor floor, Room room) {
        List<Room> roomsInFloor = getAllAvailableRooms(floor);
        roomsInFloor.sort(Comparator.comparing(Room::getRoomNumber));
        if (roomsInFloor.contains(room)) {
            int idx = roomsInFloor.indexOf(room);
            if (idx + 1 == roomsInFloor.size()) {
                return roomsInFloor.get(0);
            } else {
                return roomsInFloor.get(idx + 1);
            }
        } else {
            return null;
        }
    }

    public List<Task> getAllTasks(Floor floor) {
        return new ArrayList<>(floorRepository.findAllTasksInFloor(floor.getId()));
    }

//    @PostConstruct
//    public void populateTestData() {
//        if(roomRepository.count() == 0) {
//            roomRepository.saveAll(Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
//                    .map(roomName -> {
//                        Room room = new Room(roomName);
////                        room.setRoomNumber(roomName);
//                        return room;
//                    }).collect(Collectors.toList()));
//        }
//
//        Room room = roomRepository.search("311");
//        Account account = new Account(room);
//        accountRepository.save(account);
//    }
}