package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Disabled
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FloorTest extends AbstractTransactionalJUnit4SpringContextTests {
    private List<Room> testRooms;
    private List<Task> testTasks;
    @Autowired
    FloorService floorService;
    @Autowired
    TaskService taskService;


    @Before
    public void setup() {
        createTasks();
    }

    @Test
    public void Floor_ValidParamsWithoutRoomsAndTasks_FloorCreatedSavedAndReturned() {
        Floor floor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
        Assert.assertEquals("3A", floor.getFloorName());
//        Assert.assertEquals("9", floor.getNumberOfRooms());
        floorService.save(floor);
        Floor floorSavedAndRetrieved = floorService.getFloorByName("3A");
        Assert.assertEquals(floor, floorSavedAndRetrieved);
    }

    @Test
    public void Floor_CreateFloorAndSetTask_FloorCreatedSetTaskSuccessful() {
        Floor floor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
        Assert.assertEquals("3A", floor.getFloorName());
//        Assert.assertEquals("9", floor.getNumberOfRooms());
        floorService.save(floor);
        Floor floorSavedAndRetrieved = floorService.getFloorByName("3A");
        Assert.assertEquals(floor, floorSavedAndRetrieved);
        for(Task task: testTasks) {
            task.setFloor(floorSavedAndRetrieved);
        }
        floorSavedAndRetrieved.setTasks(testTasks);
        taskService.saveAll(testTasks);
        floorService.save(floorSavedAndRetrieved);
        Floor floorSavedAndRetrievedWithtasks = floorService.getFloorByName("3A");
        Assert.assertEquals(floorSavedAndRetrieved, floorSavedAndRetrievedWithtasks);
    }

    @Test
    public void Floor_CreateFloorAndAddTask_FloorCreatedAddTaskSuccessful() {
        Floor floor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
        Assert.assertEquals("3A", floor.getFloorName());
//        Assert.assertEquals("9", floor.getNumberOfRooms());
        floorService.save(floor);
        Floor floorSavedAndRetrieved = floorService.getFloorByName("3A");
        Assert.assertEquals(floor, floorSavedAndRetrieved);
        for(Task task: testTasks) {
            task.setFloor(floorSavedAndRetrieved);
        }
        floorSavedAndRetrieved.setTasks(testTasks);
        taskService.saveAll(testTasks);
        floorService.save(floorSavedAndRetrieved);
        Floor floorSavedAndRetrievedWithtasks = floorService.getFloorByName("3A");
        Assert.assertEquals(floorSavedAndRetrieved, floorSavedAndRetrievedWithtasks);
    }

    @Test
    public void Floor_CreateFloorAndSetRoom_FloorCreatedSetRoomSuccessful() {
        Floor floor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
        Assert.assertEquals("3A", floor.getFloorName());
//        Assert.assertEquals("9", floor.getNumberOfRooms());
        floorService.save(floor);
        Floor floorSavedAndRetrieved = floorService.getFloorByName("3A");
        Assert.assertEquals(floor, floorSavedAndRetrieved);
        for(Task task: testTasks) {
            task.setFloor(floorSavedAndRetrieved);
        }
        floorSavedAndRetrieved.setTasks(testTasks);
        taskService.saveAll(testTasks);
        floorService.save(floorSavedAndRetrieved);
        Floor floorSavedAndRetrievedWithtasks = floorService.getFloorByName("3A");
        Assert.assertEquals(floorSavedAndRetrieved, floorSavedAndRetrievedWithtasks);
    }

    @Test
    public void Floor_CreateFloorAndSetRoomFloorNullAndEmpty_RuntimeExceptionThrown() {
        Floor floor = new Floor.FloorBuilder("3A", CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE)).build();
        Assert.assertEquals("3A", floor.getFloorName());
//        Assert.assertEquals("9", floor.getNumberOfRooms());
        Assert.assertThrows(RuntimeException.class, () -> floor.setTasks(null));
        Assert.assertThrows(RuntimeException.class, () -> floor.setRooms(null));
        Assert.assertThrows(RuntimeException.class, () -> floor.addRoom(null));
        Assert.assertThrows(RuntimeException.class, () -> floor.addTask(null));
    }


    public void createRooms(Floor floor) {
        testRooms = Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                .map(roomName -> {
                    Room room = new Room(roomName, floor);
                    return room;
                }).collect(Collectors.toList());
    }

    public void createTasks() {
        testTasks = Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                .map(taskName -> {
                    Task task = new Task();
                    task.setTaskName(taskName);
                    return task;
                }).collect(Collectors.toList());
    }
}
