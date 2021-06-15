package com.wg_planner.backend.entity;

import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
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
public class RoomTest  {
    @Autowired
    FloorService floorService;
    @Autowired
    RoomService roomService;
    @Autowired
    TaskService taskService;
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
        Room createdRoom = roomService.getRoomByNumber("222", testFloor);
        Assert.assertEquals(testRoom.getRoomName(), createdRoom.getRoomName());
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
        Room room = roomService.getRoomByNumber("222", testFloor);
        Assert.assertEquals(testRoom.getRoomName(), room.getRoomName());
        Assert.assertEquals(testRoom.getFloor(), room.getFloor());
        Assert.assertEquals(residentAccount.getUsername(), room.getResidentAccount().getUsername());
    }

    @Test
    public void Room_SetTasks_RoomWithTaskSavedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        Room testRoom = new Room("222", testFloor);
        List<Room> testRoomList = new ArrayList<>();
        testRoomList.add(testRoom);
        testFloor.setRooms(testRoomList);
        floorService.save(testFloor);
        roomService.save(testRoom);
        Task task = new Task("Biomüll", testFloor, testRoom);
        List<Task> tasks = Stream.of("Restmüll", "Gelbersack", "ofen")
                .map(taskname -> new Task(taskname, testFloor)).collect(Collectors.toList());
        List<Task> tasks1 = Stream.of("Gelbersack", "ofen")
                .map(taskname -> new Task(taskname, testFloor)).collect(Collectors.toList());
        testRoom.setAssignedTasks(tasks);
        tasks.forEach(task1 -> task1.setAssignedRoom(testRoom));
        roomService.save(testRoom);
        List<String> tasksNames = tasks.stream()
                .map(Task::getTaskName)
                .collect(Collectors.toList());
        List<String> tasks1Names = tasks1.stream()
                .map(Task::getTaskName)
                .collect(Collectors.toList());
        List<String> roomSavedTasks = tasks.stream()
                .map(Task::getTaskName)
                .collect(Collectors.toList());
        Assert.assertEquals(tasksNames, roomSavedTasks);
        Assert.assertNotEquals(tasks1Names, roomSavedTasks);
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
        testRoom.setAssignedTasks(tasks);
        roomService.save(testRoom);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
        Assert.assertNotEquals(tasks1, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
        testRoom.addToAssignedTasks(task);
        roomService.save(testRoom);
        List<Task> tasksCopy = new ArrayList<>(tasks);
        tasks.add(task);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
        Assert.assertNotEquals(tasksCopy, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
    }
    @Test
    public void Room_RemoveTasks_RoomWithTaskRemovedSavedAndReturned() {
        Floor testFloor = createAndReturnFloor();
        floorService.save(testFloor);
        Room testRoom = new Room("222", testFloor);
        roomService.save(testRoom);
        Task task = new Task("Biomüll", testFloor, testRoom);
        List<Task> tasks = Stream.of("Restmüll", "Gelbersack", "ofen")
                .map(taskname -> {
                    Task taskx = new Task();
                    taskx.setFloor(testFloor);
                    taskx.setTaskName(taskname);
                    return taskx;
                }).collect(Collectors.toList());
        taskRepository.save(task);
        tasks.add(task);
        taskRepository.saveAll(tasks);
        testRoom.setAssignedTasks(tasks);
        roomService.save(testRoom);
        Assert.assertEquals(tasks, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
        testRoom.removeAssignedTask(task);
        roomService.save(testRoom);
        Assert.assertNotEquals(tasks, roomService.getRoomByNumber("222", testFloor).getAssignedTasks());
        Assert.assertFalse(roomService.getRoomByNumber("222", testFloor).getAssignedTasks().contains(task));
    }

    public Floor createAndReturnFloor() {
        return new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
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
