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
    private final AccountRepository accountRepository;
    private final FloorRepository floorRepository;
    private final TaskRepository taskRepository;
    private final ResidentAccountRepository residentAccountRepository;

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
//                        task.setAssignedRoom(allRooms.get(r.nextInt(allRooms.size())));
                        return task;
                    }).collect(Collectors.toList()));
        List<Task> tasks = taskRepository.findAll();

        Floor floor = new Floor.FloorBuilder("2A", "9", "300").setTasks(tasks).build();
        floorRepository.save(floor);

        if (roomRepository.count() == 0) {
            roomRepository.saveAll(Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                    .map(roomName -> {
                        Room room = new Room(roomName, floor);
//                        room.setRoomNumber(roomName);
                        return room;
                    }).collect(Collectors.toList()));
            List<Room> rooms = roomRepository.findAll();
            Random r = new Random(0);
            for (Task task : tasks) {
                task.setAssignedRoom(rooms.get(r.nextInt(rooms.size())));
                taskRepository.save(task);
            }
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("USER"));
//        User user = new User("user@example.com", passwordEncoder.encode("s3cr3t"), authorities);
//            for(int i= 0 ; i < 9 ; i++) {
//                UserDetails user =
//                        User.withUsername(i + "@example.com")
//                                .password("{noop}password")
//                                .roles("USER")
//                                .build();
//            }
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            SecurityConfiguration securityConfiguration = new SecurityConfiguration();
            List<User> users = new ArrayList<>();

            for (int i = 0; i < 9; i++) {
                users.add((User) securityConfiguration.userDetailsService().loadUserByUsername(i + "@example.com"));
            }
            int i = 0;
            for (Room room : rooms) {
                List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
                ResidentAccount residentAccount = new ResidentAccount(room, users.get(i++).getUsername());
                residentAccountRepository.save(residentAccount);
//            ResidentAccount residentAccount1 = (room.getId());
            }
            Room room2 = getRoomByNumber("307");
            room2.getResidentAccount().setAway(true);
            roomRepository.saveAndFlush(room2);
            Room room3 = getRoomByNumber("308");
            room3.getResidentAccount().setAway(true);
            roomRepository.saveAndFlush(room3);
//            roomRepository.
//            ResidentAccount residentAccount = room2.getResidentAccount();
//            residentAccountRepository.save(room2.getResidentAccount());
//            int ix = 1;
//            for (Task task : tasks) {
//                if(ix == 1){
//                    ix += 1;
//                    continue;
//                }
//                task.setAssignedRoom(getRoomByNumber("309"));
//                taskRepository.save(task);
//            }
//            roomRepository.findAll().stream().forEach(room -> residentAccountRepository.save(new ResidentAccount(room)));
            List<ResidentAccount> residentAccounts = residentAccountRepository.findAll();
            List<ResidentAccount> residentAccountsx = residentAccountRepository.findAll();
        }

        }
    }
}
