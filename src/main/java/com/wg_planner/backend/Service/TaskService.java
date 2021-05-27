package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.Repository.TaskRepository;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class TaskService {
    private static final Logger LOGGER = Logger.getLogger(TaskService.class
            .getName());
    private final TaskRepository taskRepository;
    private final RoomRepository roomRepository;
    private final FloorService floorService;

    @Autowired
    public TaskService(TaskRepository taskRepository, RoomRepository roomRepository,
                       FloorService floorService) {
        this.taskRepository = taskRepository;
        this.roomRepository = roomRepository;
        this.floorService = floorService;
    }

    public Task getTaskById(Long taskId) {
        Validate.notNull(taskId, "parameter taskId must not be %s", null);
        return taskRepository.findTaskByTaskId(taskId);
    }

    @Transactional
    public void transferTask(Task task) {
        assignTask(task, floorService.getNextAvailableRoom(task.getAssignedRoom()));
    }

    @Transactional
    public void assignTask(Task taskToAssign, Room selectedRoom) {
        Validate.notNull(taskToAssign, "parameter taskToAssign must not be %s", null);
        Validate.notNull(selectedRoom, "parameter selectedRoom must not be %s", null);

        taskToAssign.getAssignedRoom().removeAssignedTask(taskToAssign);
        taskToAssign.setAssignedRoom(selectedRoom);
        selectedRoom.addToAssignedTasks(taskToAssign);
        save(taskToAssign);
    }

//    public List<Task> findAll() {
//        return taskRepository.findAll();
//    }

    public long count() {
        return taskRepository.count();
    }

    public void save(Task task) {
        Validate.notNull(task, "parameter task to save must not be %s", null);
        taskRepository.save(task);
    }

    public void saveAll(List<Task> tasks) {
        Validate.notNull(tasks, "parameter task to save must not be %s", null);
        taskRepository.saveAll(tasks);
    }

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