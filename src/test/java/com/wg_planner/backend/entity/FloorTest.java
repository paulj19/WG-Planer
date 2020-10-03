package com.wg_planner.backend.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FloorTest {
    private List<Room> rooms;
    private List<Task> tasks;

    @Before
    public void setupTasks() {
        Random r = new Random(0);
        tasks = Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
                .map(taskName -> {
                    Task task = new Task();
                    task.setTaskName(taskName);
                    return task;
                }).collect(Collectors.toList());
    }

    @Test
    public void createFloor() {
        Floor floor = new Floor.FloorBuilder("2A", "9", "300").setTasks(tasks).build();
        Assert.assertEquals("2A", floor.getFloorNumber());
        Assert.assertEquals("9", floor.getNumberOfRooms());
        Assert.assertEquals("300", floor.getRoomStartIndex());
        Assert.assertEquals(tasks, floor.getTasks());

        createRooms(floor);
        floor.addRooms(rooms);
    }

    public void createRooms(Floor floor) {
        rooms = Stream.of("307", "308", "309", "310", "311", "312", "313", "314", "315")
                .map(roomName -> {
                    Room room = new Room(roomName, floor);
//                        room.setRoomNumber(roomName);
                    return room;
                }).collect(Collectors.toList());
    }
}
