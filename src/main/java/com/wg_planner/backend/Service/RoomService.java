package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.entity.*;
import com.wg_planner.backend.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoomService {
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

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room getRoomByNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.isEmpty()) {
            return null;
        } else {
            return roomRepository.search(roomNumber);
        }
    }

    public long count() {
        return roomRepository.count();
    }

    public Room getMyRoom(ResidentAccount residentAccount) {
        return roomRepository.getMyRoom(residentAccount.getId());
    }

    @PostConstruct
    public void populateTestData() {
        if (taskRepository.count() == 0) {

            taskRepository.saveAll(Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                    .map(taskName -> {
                        Task task = new Task();
                        task.setTaskName(taskName);
                        return task;
                    }).collect(Collectors.toList()));
        }
        if (floorRepository.count() == 0) {
            List<Task> tasks = taskRepository.findAll();
            Floor floor = new Floor.FloorBuilder("2A", "9", "300").setTasks(tasks).build();
            floorRepository.save(floor);
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
                taskRepository.save(task);
            }
        }
        if (residentAccountRepository.count() == 0) {
            List<Room> rooms = roomRepository.findAll();
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
            int i = 0;
            for (Room room : rooms) {
                List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
                ResidentAccount residentAccount = new ResidentAccount(room, i++ + "@example.com", "{noop}password", authorities);
                residentAccountRepository.save(residentAccount);
            }
            Room room2 = getRoomByNumber("307");
            room2.getResidentAccount().setAway(true);
            roomRepository.saveAndFlush(room2);
            Room room3 = getRoomByNumber("308");
            room3.getResidentAccount().setAway(true);
            roomRepository.saveAndFlush(room3);
            List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
            List<ResidentAccount> residentAccountsx = residentAccountRepository.findAll();
        }
    }
}
