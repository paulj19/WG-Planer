package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.*;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class RoomService {
    private static final Logger LOGGER = Logger.getLogger(Room.class
            .getName());
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;
    private final TaskRepository taskRepository;
    private final ResidentAccountRepository residentAccountRepository;
    private final AccountRepository accountRepository;
    private final TaskNotificationContentRepository taskNotificationContentRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, AccountRepository accountRepository,
                       FloorRepository floorRepository, TaskRepository taskRepository,
                       ResidentAccountRepository residentAccountRepository,
                       TaskNotificationContentRepository taskNotificationContentRepository) {
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
        this.floorRepository = floorRepository;
        this.taskRepository = taskRepository;
        this.residentAccountRepository = residentAccountRepository;
        this.taskNotificationContentRepository = taskNotificationContentRepository;
    }

    public Room getRoomByNumber(String roomNumber, Floor floorToSearch) {
        Validate.notNull(roomNumber, "parameter room number must not be %s", null);
        Validate.notEmpty(roomNumber, "parameter room number must not be empty");
        return roomRepository.findRoomByNumber(roomNumber, floorToSearch.getId());
    }

    public Room getRoomById(Long roomId) {
        Validate.notNull(roomId, "parameter room id must not be %s", null);
        return roomRepository.findRoomByRoomId(roomId);
    }

    public long count() {
        return roomRepository.count();
    }

    public void save(Room room) {
        Validate.notNull(room, "parameter room to save must not be %s", null);
        roomRepository.save(room);
    }
}
