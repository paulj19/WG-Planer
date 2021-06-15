package com.wg_planner.backend.entity;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class AdmissionTest {
    @Autowired
    AdmissionHandler admissionHandler;
    @Autowired
    private FloorService floorService;

    Floor testFloor;

    @Test
    public void AdmissionHandler_generateAndSaveAdmissionCode_generatesAndSavesAdmissionCodeSuccessfully() {
        setUpFloors("1");
        IntStream.range(0, 1000).forEach(value -> {
            AdmissionCode admissionCode = admissionHandler.generateAndSaveAdmissionCode(testFloor.getRooms().get(0));
            Assert.assertEquals(admissionHandler.verifyAdmissionCodeAndGetAdmissionDetails(admissionCode).getRoomToAdmit(), testFloor.getRooms().get(0));
        });
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


    public void setUpFloors(String floorName) {
        testFloor = new Floor(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE));
        testFloor.setFloorName(floorName);
        testFloor.setRooms(createRooms(testFloor));
        testFloor.setTasks(createTasks(testFloor));
        floorService.save(testFloor);
    }
}
