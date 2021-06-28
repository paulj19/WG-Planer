package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
@Transactional
public class FloorCodeGenerationTest {
    @Autowired
    private FloorService floorService;

    HashMap<String, Floor> floorCodeMap = new HashMap<>();

    Floor testFloor;

    public void setUpFloors(String floorName) {
        testFloor = new Floor(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE));
        testFloor.setFloorName(floorName);
        testFloor.setRooms(createRooms(testFloor));
        testFloor.setTasks(createTasks(testFloor));
        floorCodeMap.put(testFloor.getFloorCode(), testFloor);
        floorService.save(testFloor);
    }

    public List<Room> createRooms(Floor floor) {
        return Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                .map(roomName -> {
                    Room room = new Room(roomName, floor);
                    return room;
                }).collect(Collectors.toList());
    }

    public List<Task> createTasks(Floor floor) {
        return Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                .map(taskName -> {
                    Task task = new Task();
                    task.setTaskName(taskName);
                    task.setFloor(floor);
                    return task;
                }).collect(Collectors.toList());
    }

    @Test
    public void FloorCodeGenerationTest_createFloorWithFloorCode_createsFloorWithUniqueNonNullNonEmptyFloorCode() {
        for (int i = 0; i < 10; i++) {
            setUpFloors(String.valueOf(i));
        }
        floorService.getAllFloors().forEach(floor -> {
            Assert.assertNotNull(floor.getFloorCode());
            Assert.assertTrue(!floor.getFloorCode().isEmpty());
        });
        floorCodeMap.forEach((floorCode, savedFloorCorrespondingToFloorCode) -> Assert.assertEquals(floorService.getFloorByFloorCode(floorCode), savedFloorCorrespondingToFloorCode));
    }
}
