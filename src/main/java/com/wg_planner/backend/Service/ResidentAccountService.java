package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ResidentAccountService {
    private static final Logger LOGGER = Logger.getLogger(ResidentAccountService.class
            .getName());
    private final ResidentAccountRepository residentAccountRepository;
    private final RoomRepository roomRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ResidentAccountService(ResidentAccountRepository residentAccountRepository, RoomRepository roomRepository, AccountRepository accountRepository) {
        Validate.notNull(residentAccountRepository);
        Validate.notNull(roomRepository);
        Validate.notNull(accountRepository);
        this.residentAccountRepository = residentAccountRepository;
        this.roomRepository = roomRepository;
        this.accountRepository = accountRepository;
    }

    public ResidentAccount getResidentAccount(Long accountId) {
        Validate.notNull(accountId, "parameter account id must not be %s", null);
        return residentAccountRepository.getResidentAccount(accountId);
    }

    public ResidentAccount getResidentAccountByRoom(Room room) {
        Validate.notNull(room, "parameter room must not be %s", null);
        return residentAccountRepository.getResidentAccountByRoom(room.getId());
    }

    public Room getRoomByResidentAccount(ResidentAccount residentAccount) {
        Validate.notNull(residentAccount, "parameter resident account must not be %s", null);
        return roomRepository.findRoomByResidentId(residentAccount.getId());
    }

    public ResidentAccount getResidentAccountByUsername(String username) {
        Validate.notNull(username, "parameter username must not be %s", null);
        Validate.notEmpty(username, "parameter username must not be empty");
        return (ResidentAccount) accountRepository.findAccountByUsername(username);
    }

    public void save(ResidentAccount residentAccount) {
        Validate.notNull(residentAccount, "parameter resident account must not be %s", null);
        residentAccountRepository.save(residentAccount);
    }

    @Transactional
    public void removeResidentAccount(ResidentAccount residentAccountToRemove,
                                      FloorService floorService, TaskService taskService) {
        transferTasksOfResidentToNext(residentAccountToRemove, floorService, taskService);
        Room roomToDeactivate = residentAccountToRemove.getRoom();
        roomToDeactivate.setResidentAccount(null);
        roomToDeactivate.setOccupied(false);
        residentAccountToRemove.getResidentDevices().forEach(residentDevice -> {
            residentDevice.getDeviceNotificationChannels().forEach(notificationChannel -> notificationChannel.setActive(false));
            residentDevice.setActive(false);
        });
        residentAccountToRemove.setActive(false);
        residentAccountToRemove.setEnabled(false);
        //TODO high pri-- fix setRoom to null
        residentAccountToRemove.setRoom(residentAccountToRemove.getRoom().getFloor().getTasks().get(0).getAssignedRoom());
//        residentAccountToRemove.setRoom(null);
        roomRepository.save(roomToDeactivate);//todo see why room cascade residentaccount does not work
        residentAccountRepository.save(residentAccountToRemove);
    }

    public void transferTasksOfResidentToNext(ResidentAccount currentResidentAccount,
                                              FloorService floorService, TaskService taskService) {
        List<Task> assignedTasks = new ArrayList<>(currentResidentAccount.getRoom().getAssignedTasks());
        assignedTasks.forEach(task -> taskService.transferTask(task, floorService));
        Assert.isTrue(currentResidentAccount.getRoom().getAssignedTasks().isEmpty(), "assigned " +
                "tasks to the room after transfer task of resident must be empty");
    }
}
