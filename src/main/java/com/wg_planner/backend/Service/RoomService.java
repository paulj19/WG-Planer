package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
    }


    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room getRoomByRoomNumber(String roomNumber) {
        if(roomNumber == null || roomNumber.isEmpty()) {
            return null;
        } else {
            return roomRepository.search(roomNumber);
        }
    }

    public long count() { return roomRepository.count(); }

    public Room getMyRoom(ResidentAccount residentAccount) {
        return roomRepository.getMyRoom(residentAccount.getId());
    }

    @PostConstruct
    public void populateTestData() {
        Random r = new Random(0);
        List<Task>  tasks = Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                .map(taskName -> {
                    Task task = new Task();
                    task.setTaskName(taskName);
                    return task;
                }).collect(Collectors.toList());

        Floor floor = new Floor.FloorBuilder("2A", "9", "300").setTasks(tasks).build();

        if(roomRepository.count() == 0) {
            roomRepository.saveAll(Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                    .map(roomName -> {
                        Room room = new Room(roomName, floor);
//                        room.setRoomNumber(roomName);
                        return room;
                    }).collect(Collectors.toList()));
        }
    }
}
