package com.wg_planner.backend.entity.ServiceTest;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RoomServiceTest {
    @Autowired
    private FloorService floorService;
    @Autowired
    private RoomService roomService;

    Floor testFloor;
    List<Room> testRooms = new LinkedList<>();
    List<Task> testTasks = new ArrayList<>();
    List<ResidentAccount> testResidentAccounts = new ArrayList<>();

    @Before
    public void setUp() {
        setUpFloor();
        setUpRooms();
        setUpTasks();
        setUpResidentAccounts();
        testFloor.setRooms(testRooms);
        testFloor.setTasks(testTasks);
        floorService.save(testFloor);
    }

    @Test
    public void RoomService_getRoomByNumberNullRoomNumber_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> roomService.getRoomByNumber(null, testFloor));
    }

    @Test
    public void RoomService_getRoomByNumberRoomNumber_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> roomService.getRoomByNumber("", testFloor));
    }
    @Test
    public void RoomService_getRoomByNumberNullFloor_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> roomService.getRoomByNumber("407", null));
    }

    public void setUpFloor() {
        testFloor = new Floor.FloorBuilder("3A", "9").build();
    }

    public void setUpResidentAccounts() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        for (int i = 0; i < 9; i++) {
            ResidentAccount residentAccount = new ResidentAccount("first_name-" + i,
                    "last_name-" + i,
                    "test" + i + "@testcreate.com", "username_" + i, encoder.encode(
                    "password" + i), testRooms.get(i),
                    false, authorities);
            testRooms.get(i).setResidentAccount(residentAccount);
//            residentAccountService.save(residentAccount);
//            roomService.save(testRooms.get(i));
            testResidentAccounts.add(residentAccount);
        }
//        residentAccountRepository.saveAll(testResidentAccounts);
    }

    public void setUpRooms() {
        testRooms = Stream.of("407", "408", "409", "410", "411", "412", "413", "414", "415")
                .map(roomName -> new Room(roomName, testFloor, true)).collect(Collectors.toList());
//        roomRepository.saveAll(testRooms);
    }

    public void setUpTasks() {
        Random r = new Random(0);

        testTasks = Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                .map(taskName -> {
                    Task task = new Task();
                    task.setTaskName(taskName);
                    task.setFloor(testFloor);
                    int roomToAssign = r.nextInt(testRooms.size());
                    task.setAssignedRoom(testRooms.get(roomToAssign));
                    testRooms.get(roomToAssign).addToAssignedTasks(task);
//                    roomService.save(testRooms.get(roomToAssign));
                    return task;
                }).collect(Collectors.toList());
//        taskRepository.saveAll(testTasks);
    }
}
