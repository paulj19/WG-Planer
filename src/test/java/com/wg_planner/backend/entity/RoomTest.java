package com.wg_planner.backend.entity;

import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RoomTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    FloorService floorService;
    @Autowired
    RoomService roomService;
    @Autowired
    TaskRepository taskRepository;
    @Test
    public void Room_CreateInValidParams_RuntimeExceptionThrown() {
        Assert.assertThrows(RuntimeException.class, () -> new Room(null, createAndReturnFloor()));
        Assert.assertThrows(RuntimeException.class, () -> new Room(null, null));
        Assert.assertThrows(RuntimeException.class, () -> new Room("123", null));
        Assert.assertThrows(RuntimeException.class, () -> new Room("$", createAndReturnFloor()));
        Assert.assertThrows(RuntimeException.class, () -> new Room(StringUtils.repeat("a", 251), createAndReturnFloor()));
        Assert.assertThrows(RuntimeException.class, () -> new Room(null, null));
    }

    @Test
    public void Room_CreateSaveValidParams_RoomCreatedSavedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        floorService.save(testFloor);
        Room testRoom = new Room("222", testFloor);
        roomService.save(testRoom);
        //room number not unique but within floor room number is unique, testing the floor should return only the currently created room not any previously created ones
        Room createdRoom = roomService.getRoomByNumber("222");
        Assert.assertEquals(testRoom.getRoomNumber(), createdRoom.getRoomNumber());
        Assert.assertEquals(testRoom.getFloor(), createdRoom.getFloor());
    }

    @Test
    public void Room_SetResidentAccount_RoomWithResidentAccountCreatedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        floorService.save(testFloor);
        Room testRoom = new Room("222", testFloor);
        roomService.save(testRoom);
        ResidentAccount residentAccount = createAndReturnResidentAccount(testRoom);
        testRoom.setResidentAccount(residentAccount);
        roomService.save(testRoom);
        Room room = roomService.getRoomByNumber("222");
        Assert.assertEquals(testRoom.getRoomNumber(), room.getRoomNumber());
        Assert.assertEquals(testRoom.getFloor(), room.getFloor());
        Assert.assertEquals(residentAccount, room.getResidentAccount());
    }

    @Test
    public void Room_SetTasks_RoomWithTaskSavedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        floorService.save(testFloor);
        Room testRoom = new Room("222", testFloor);
        roomService.save(testRoom);
        Task task = new Task("Biomüll", testFloor, testRoom);
        List<Task> tasks = Stream.of("Restmüll", "Gelbersack", "ofen")
                .map(taskname -> {
                    Task task1 = new Task();
                    task1.setTaskName(taskname);
                    return task1;
                }).collect(Collectors.toList());
        List<Task> tasks1 = Stream.of("Gelbersack", "ofen")
                .map(taskname -> {
                    Task task1 = new Task();
                    task1.setTaskName(taskname);
                    return task1;
                }).collect(Collectors.toList());
        testRoom.setAssignedTasks(tasks);
        roomService.save(testRoom);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222").getAssignedTasks());
        Assert.assertNotEquals(tasks1, roomService.getRoomByNumber("222").getAssignedTasks());
    }
    @Test
    public void Room_AddTasks_RoomWithTaskAddedSavedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        floorService.save(testFloor);
        Room testRoom = new Room("222", testFloor);
        roomService.save(testRoom);
        Task task = new Task("Biomüll", testFloor, testRoom);
        taskRepository.save(task);
        List<Task> tasks = Stream.of("Restmüll", "Gelbersack", "ofen")
                .map(taskname -> {
                    Task taskx = new Task();
                    taskx.setFloor(testFloor);
                    taskx.setTaskName(taskname);
                    return taskx;
                }).collect(Collectors.toList());
        List<Task> tasks1 = Stream.of("Gelbersack", "ofen")
                .map(taskname -> {
                    Task task1 = new Task();
                    task1.setTaskName(taskname);
                    return task1;
                }).collect(Collectors.toList());
        taskRepository.saveAll(tasks);
        List<Task> tasksCopy = new ArrayList<>(tasks);
        testRoom.setAssignedTasks(tasks);
        roomService.save(testRoom);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222").getAssignedTasks());
        Assert.assertNotEquals(tasks1, roomService.getRoomByNumber("222").getAssignedTasks());
        testRoom.addAssignedTasks(task);
        roomService.save(testRoom);
        tasks.add(task);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222").getAssignedTasks());
        Assert.assertNotEquals(tasksCopy, roomService.getRoomByNumber("222").getAssignedTasks());
    }
    public Floor createAndReturnFloor() {
        return new Floor.FloorBuilder("3A", "9", "300").build();
    }
    public ResidentAccount createAndReturnResidentAccount(Room testRoom) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new ResidentAccount("testValid_first_name",
                "testValid_last_name",
                "testvalid@testcreate.com", "testValid_username_redundant", encoder.encode(
                "testValid_password"), testRoom,
                false, authorities);
    }

}
