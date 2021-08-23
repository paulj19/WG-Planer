package com.wg_planner.views.sub_menu.floor_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskCreate;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskDelete;
import com.wg_planner.backend.utils.locking.LockRegisterHandler;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeRequireConsensusTaskCreate;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeRequireConsensusTaskDelete;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FloorDetailsPresenter {
    @Autowired
    private FloorService floorService;
    @Autowired
    TaskService taskService;
    @Autowired
    ResidentAccountService residentAccountService;

    private FloorDetailsView floorDetailsView;

    public void init(FloorDetailsView floorDetailsView) {
        this.floorDetailsView = floorDetailsView;
        Floor floor =
                residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()).getRoom().getFloor();
        if (!floor.getFloorName().isEmpty()) {
            floorDetailsView.addFloorName(floor.getFloorName());
        }
        floorDetailsView.addFloorCode(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorCode());
        floorDetailsView.addRoomsInFloor(floorService.getAllRoomsInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
        floorDetailsView.addAdmitNewResidentView();
        floorDetailsView.addListener(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent.class, this::onTaskDelete);
        floorDetailsView.addTasksInFloor();
    }

    private synchronized void onTaskDelete(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent event) {
        if (!ConsensusHandler.getInstance().isObjectWaitingForConsensus(event.getTask())) {
            try {
                Object taskLock = LockRegisterHandler.getInstance().registerLock(event.getTask().getId());
                synchronized (taskLock) {
                    event.getTask().setAssignedRoom(null);
                    taskService.save(event.getTask());
                }
            } finally {
                LockRegisterHandler.getInstance().unregisterLock(event.getTask().getId());
            }
            List<Room> roomsToSentNotification = floorService.getAllOccupiedAndResidentNotAwayRooms(event.getTask().getFloor());
            roomsToSentNotification.removeIf(room -> room.equals(SessionHandler.getLoggedInResidentAccount().getRoom()));
            UIMessageBus.broadcast(UINotificationHandler.getInstance().createAndSaveUINotification(new UINotificationTypeRequireConsensusTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(), event.getTask()), roomsToSentNotification));
            ConsensusHandler.getInstance().add(new ConsensusObjectTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(),
                    event.getTask(), floorService));
            UINotificationMessage.notify("All other residents are notified, all the other residents should accept" +
                    " before task can be deleted");
        } else {
            UINotificationMessage.notify("Some other resident tried to delete the task and is waiting for approval by all available residents");
        }
        floorDetailsView.refreshTasksInFloor();
    }

    synchronized boolean isTaskEditable(Task taskToDelete) {
        return !ConsensusHandler.getInstance().isObjectWaitingForConsensus(taskToDelete) && !floorService.getAllOccupiedAndResidentNotAwayRooms(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()).isEmpty();
    }

    boolean isAnyResidentPresentInFloor() {
        return !floorService.getAllOccupiedAndResidentNotAwayRooms(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()).isEmpty();
    }

    synchronized void saveNewlyCreatedTask(Task taskToCreate) {
        boolean isAnotherTaskCreatedWithSameNameWaitingForConsensus =
                ConsensusHandler.getInstance().getAllConsensusObjects().stream().filter(consensusObject -> consensusObject.getRelatedObject() instanceof Task && ((Task) consensusObject.getRelatedObject()).getFloor().equals(taskToCreate.getFloor())).anyMatch(consensusObject -> ((Task) consensusObject.getRelatedObject()).getTaskName().equals(taskToCreate.getTaskName()));
        //task Name should be unique in floor
        if (!isAnotherTaskCreatedWithSameNameWaitingForConsensus) {
            List<Room> roomsToSentNotification = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToCreate.getFloor());
            roomsToSentNotification.removeIf(room -> room.equals(SessionHandler.getLoggedInResidentAccount().getRoom()));
            UIMessageBus.broadcast(UINotificationHandler.getInstance().createAndSaveUINotification(new UINotificationTypeRequireConsensusTaskCreate(SessionHandler.getLoggedInResidentAccount().getRoom(), taskToCreate), roomsToSentNotification));
            ConsensusHandler.getInstance().add(new ConsensusObjectTaskCreate(SessionHandler.getLoggedInResidentAccount().getRoom(),
                    taskToCreate, floorService, taskService));
            UINotificationMessage.notify("All other residents are notified, all the other residents should accept" +
                    " before task can be created");
        } else {
            UINotificationMessage.notify("Some other resident tried to create the task with the same name and is waiting for approval by all available " +
                    "residents");
        }
    }

    List<Task> getTasksInFloor() {
        return floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
    }

    //todo fixme: usage in view, refactor to make presenter call
    public FloorService getFloorService() {
        return floorService;
    }
}
