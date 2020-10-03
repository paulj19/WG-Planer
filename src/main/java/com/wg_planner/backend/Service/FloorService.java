package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FloorService {
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
        this.accountRepository = accountRepository;
        this.residentAccountRepository = residentAccountRepository;
    }

    public List<ResidentAccount> getAllResidents(Floor floor) {
        List<ResidentAccount> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).forEach(residents::add);
        return residents;
    }

    public List<ResidentAccount> getAllAvailableResidents(Floor floor) {
        List<ResidentAccount> residents = new ArrayList<>();
        floorRepository.findAllRoomsInFloor(floor.getId()).stream().map(Room::getResidentAccount).filter(residentAccount -> !residentAccount.isAway()).forEach(residents::add);
        return residents;
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