package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.entity.*;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomService {
    private static final Logger LOGGER = Logger.getLogger(Room.class
            .getName());
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final TaskRepository taskRepository;
    private final ResidentAccountRepository residentAccountRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, AccountRepository accountRepository, FloorRepository floorRepository, TaskRepository taskRepository, ResidentAccountRepository residentAccountRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.taskRepository = taskRepository;
        this.residentAccountRepository = residentAccountRepository;
    }

    public Room getRoomByNumber(String roomNumber) {
        Validate.notNull(roomNumber, "parameter room number must not be %s", null);
        Validate.notEmpty(roomNumber, "parameter room number must not be empty");
        return roomRepository.search(roomNumber);
    }

    public long count() {
        return roomRepository.count();
    }

    public void save(Room room) {
        Validate.notNull(room, "parameter room to save must not be %s", null);
        roomRepository.save(room);
    }

    public Room getMyRoom(ResidentAccount residentAccount) {
        Validate.notNull(residentAccount, "parameter resident account must not be %s", null);
        return roomRepository.findRoomByResidentId(residentAccount.getId());
    }

    @PostConstruct
    public void populateTestData() {
//        System.out.println(floorRepository.findFloorByNumber("2A").toString());
        if (floorRepository.count() == 0) {
            List<Task> tasks = taskRepository.findAll();
            Floor floor = new Floor.FloorBuilder("2A", "9").build();
            floorRepository.save(floor);
        }
        if (taskRepository.count() == 0) {
            taskRepository.saveAll(Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                    .map(taskName -> {
                        Task task = new Task();
                        task.setTaskName(taskName);
                        task.setFloor(floorRepository.findFloorByNumber("2A"));
                        task.setAssignedRoom(getRoomByNumber("310"));
                        return task;
                    }).collect(Collectors.toList()));
        }
        if (roomRepository.count() == 0) {
            Floor floor = floorRepository.findFloorByNumber("2A");
            roomRepository.saveAll(Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                    .map(roomName -> {
                        Room room = new Room(roomName, floor);
//                        room.setRoomNumber(roomName);
                        return room;
                    }).collect(Collectors.toList()));
            List<Room> rooms = roomRepository.findAll();
            Random r = new Random(0);
            List<Task> tasks = taskRepository.findAll();
            for (Task task : tasks) {
                task.setAssignedRoom(rooms.get(r.nextInt(rooms.size())));
                task.setFloor(floor);
                taskRepository.save(task);
            }
        }
        if (residentAccountRepository.count() == 0) {
            List<Room> rooms = roomRepository.findAll();
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            int i = 0;
//            for (Room room : rooms) {
//                List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
//                ResidentAccount residentAccount = new ResidentAccount("foo","bar", i++ + "@example.com", i++ + "@example.com", "{noop}password", room, authorities);
//                residentAccountRepository.save(residentAccount);
//            }
//            Room room2 = getRoomByNumber("307");
//            room2.getResidentAccount().setAway(true);
//            roomRepository.saveAndFlush(room2);
//            Room room3 = getRoomByNumber("308");
//            room3.getResidentAccount().setAway(true);
//            roomRepository.saveAndFlush(room3);
//            List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
//            List<ResidentAccount> residentAccountsx = residentAccountRepository.findAll();
        }
    }
}
