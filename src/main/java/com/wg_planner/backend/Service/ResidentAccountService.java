package com.wg_planner.backend.Service;

import com.wg_planner.backend.Repository.AccountRepository;
import com.wg_planner.backend.Repository.ResidentAccountRepository;
import com.wg_planner.backend.Repository.RoomRepository;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.sub_menu.account_details.ResidentAvailabilityView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.backend.utils.locking.LockRegisterHandler;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResidentAccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResidentAccountService.class);
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

    public ResidentAccount getResidentAccountById(Long accountId) {
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
        roomRepository.save(roomToDeactivate);
        residentAccountRepository.delete(residentAccountToRemove);
    }

    public void transferTasksOfResidentToNext(ResidentAccount currentResidentAccount,
                                              FloorService floorService, TaskService taskService) {
        List<Task> assignedTasks = new ArrayList<>(currentResidentAccount.getRoom().getAssignedTasks());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. transferTasksOfResidentToNext. Tasks transferred: ",
                SessionHandler.getLoggedInResidentAccount().getId());
        assignedTasks.forEach(task -> {
            try {
                Object taskLock = LockRegisterHandler.getInstance().registerLock(task.getId());
                synchronized (taskLock) {
                    LOGGER.info(LogHandler.getTestRun(), ", {}", task.getId());
                    taskService.transferTask(task, floorService);
                }
            } finally {
                LockRegisterHandler.getInstance().unregisterLock(task.getId());
            }
        });
        Assert.isTrue(currentResidentAccount.getRoom().getAssignedTasks().isEmpty(), "assigned " +
                "tasks to the room after transfer task of resident must be empty");
    }
}
