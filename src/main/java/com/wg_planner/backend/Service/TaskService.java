package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, RoomRepository roomRepository) {
        this.taskRepository = taskRepository;
        this.roomRepository = roomRepository;
    }
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public long count() { return taskRepository.count(); }

//    @PostConstruct
//    public void populateTestData() {
//        if(taskRepository.count() == 0) {
//            Random r = new Random(0);
//
//            List<Room> allRooms = roomRepository.findAll();
//            taskRepository.saveAll(Stream.of("Biomüll", "Restmüll", "Gelbersack", "Ofen Reinigen", "Mikrowelle Reinigen")
//                    .map(taskName -> {
//                        Task task = new Task();
//                        task.setTaskName(taskName);
//                        task.setAssignedRoom(allRooms.get(r.nextInt(allRooms.size())));
//                        return task;
//                    }).collect(Collectors.toList()));
//        }
//    }
}