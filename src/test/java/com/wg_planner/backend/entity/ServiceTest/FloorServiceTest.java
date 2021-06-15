package com.wg_planner.backend.entity.ServiceTest;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FloorServiceTest{
    @Autowired
    private FloorService floorService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private FloorRepository floorRepository;

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

    //test FloorService.getAllFloors()
    @Test
    public void FloorService_getAllFloors_ReturnsAllCreatedFloors() {
        List<Floor> testFloors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testFloors.add(new Floor.FloorBuilder(String.valueOf(i), CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build());
        }
        floorRepository.saveAll(testFloors);
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(testFloors.get(i), floorService.getFloorByName(String.valueOf(i)));
        }
    }

    @Test
    public void FloorService_getAllNonOccupiedRoomsInFloor_ReturnsAllNonOccupiedRoomsInFloor() {
        Assert.assertThrows(RuntimeException.class, () -> {
            FloorService.getAllNonOccupiedRoomsInFloor(null);
        });
        List<Room> nonOccupiedrooms = new ArrayList<>();
        IntStream.range(2, 5).forEachOrdered(i -> {
            testRooms.get(i).setOccupied(false);
            nonOccupiedrooms.add(testRooms.get(i));
        });
//        roomRepository.saveAll(testRooms);
        FloorService.getAllNonOccupiedRoomsInFloor(testFloor);
        Assert.assertEquals(nonOccupiedrooms, FloorService.getAllNonOccupiedRoomsInFloor(testFloor));
    }

    @Test
    public void FloorServiceNextAvailable_setResidentAway_ReturnsNextAvailableInOrder() {
        Room room407 = roomService.getRoomByNumber("407", testFloor);
        Room room408 = roomService.getRoomByNumber("408", testFloor);
        Room room415 = roomService.getRoomByNumber("415", testFloor);
        Assert.assertFalse(room407.getResidentAccount().isAway());
        room407.getResidentAccount().setAway(true);
//        roomService.save(room407);
        Assert.assertEquals(room408, floorService.getNextAvailableRoom(testFloor, room415));
        room407.getResidentAccount().setAway(false);
//        roomService.save(room407);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room415));
    }

    @Test
    public void FloorServiceNextAvailable_setMultipleResidentsAway_ReturnsNextAvailableInOrder() {
        Room room407 = roomService.getRoomByNumber("407", testFloor);
        Room room413 = roomService.getRoomByNumber("413", testFloor);
        Room room414 = roomService.getRoomByNumber("414", testFloor);
        Room room415 = roomService.getRoomByNumber("415", testFloor);
        Assert.assertFalse(room407.getResidentAccount().isAway());
        room414.getResidentAccount().setAway(true);
//        roomService.save(room414);
        room415.getResidentAccount().setAway(true);
//        roomService.save(room415);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room413));
        room415.getResidentAccount().setAway(false);
//        roomService.save(room415);
        Assert.assertEquals(room415, floorService.getNextAvailableRoom(testFloor, room413));
    }

    @Test
    public void FloorServiceNextAvailable_setRoomUnOccupied_ReturnsNextAvailableInOrder() {
        Room room407 = roomService.getRoomByNumber("407", testFloor);
        Room room408 = roomService.getRoomByNumber("408", testFloor);
        Room room415 = roomService.getRoomByNumber("415", testFloor);
        Assert.assertFalse(room407.getResidentAccount().isAway());
        room407.setOccupied(false);
//        roomService.save(room407);
        Assert.assertEquals(room408, floorService.getNextAvailableRoom(testFloor, room415));
        room407.getResidentAccount().setAway(false);
//        roomService.save(room407);
        Assert.assertEquals(room408, floorService.getNextAvailableRoom(testFloor, room415));
        room407.setOccupied(true);
//        roomService.save(room407);
        room407.getResidentAccount().setAway(true);
//        roomService.save(room407);
        Assert.assertEquals(room408, floorService.getNextAvailableRoom(testFloor, room415));
        room407.getResidentAccount().setAway(false);
//        roomService.save(room407);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room415));

    }

    @Test
    public void FloorServiceNextAvailable_setMultipleRoomsUnOccupied_ReturnsNextAvailableInOrder() {
        Room room407 = roomService.getRoomByNumber("407", testFloor);
        Room room413 = roomService.getRoomByNumber("413", testFloor);
        Room room414 = roomService.getRoomByNumber("414", testFloor);
        Room room415 = roomService.getRoomByNumber("415", testFloor);
        Assert.assertFalse(room407.getResidentAccount().isAway());
        room414.setOccupied(false);
        room415.setOccupied(false);
//        roomService.save(room414);
//        roomService.save(room415);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room413));
        room415.setOccupied(true);
        room415.getResidentAccount().setAway(true);
//        roomService.save(room415);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room413));
        room415.setOccupied(false);
//        roomService.save(room415);
        Assert.assertEquals(room407, floorService.getNextAvailableRoom(testFloor, room413));
        room415.getResidentAccount().setAway(false);
        room415.setOccupied(true);
        room415.getResidentAccount().setAway(false);
//        roomService.save(room415);
        Assert.assertEquals(room415, floorService.getNextAvailableRoom(testFloor, room413));
    }

    @Test
    public void FloorServiceGetAllTasksInFloor_NullParameter_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> floorService.getAllTasksInFloor(null));
    }

    @Test
    public void FloorServiceGetAllTasksInFloor_VerifyAllSavedTasks_ReturnsAllSavedTasksCorrectly() {
        List<Task> currentTasksFromBackend = floorService.getAllTasksInFloor(testFloor);
        Assert.assertTrue(testTasks.size() == currentTasksFromBackend.size() &&
                testTasks.containsAll(currentTasksFromBackend) && currentTasksFromBackend.containsAll(testTasks));
    }

    @Test
    public void FloorServiceGetAllTasksInFloor_RemoveTasks_ReturnsFloorTasksWithoutRemovedTask() {
//        floorService.deleteTaskAndUpdateFloor(testFloor,testTasks.get(0));
        Assert.assertNotEquals(testTasks, floorService.getAllTasksInFloor(testFloor));
    }

    @Test
    public void FloorServiceGetAllTasksInFloor_AddTasks_ReturnFloorTaskWithTaskAdded() {
        Task newTaskToAdd = new Task("new Task", testFloor);
        newTaskToAdd.setAssignedRoom(roomService.getRoomByNumber("407", testFloor));
        roomService.getRoomByNumber("407", testFloor).addToAssignedTasks(newTaskToAdd);
        taskRepository.save(newTaskToAdd);

        Assert.assertNotEquals(testTasks, floorService.getAllTasksInFloor(testFloor));
        Assert.assertTrue(floorService.getAllTasksInFloor(testFloor).contains(newTaskToAdd));
        Assert.assertTrue(roomService.getRoomByNumber("407", testFloor).getAssignedTasks().contains(newTaskToAdd));
    }

    @Test
    public void FloorServiceGetFloorByNumber_NullParameter_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> floorService.getFloorByName(null));
    }

    @Test
    public void FloorServiceGetFloorByNumber_EmptyParameter_ThrowsRunTimeException() {
        Assert.assertThrows(RuntimeException.class, () -> floorService.getFloorByName(""));
    }

    public void setUpFloor() {
        testFloor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
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