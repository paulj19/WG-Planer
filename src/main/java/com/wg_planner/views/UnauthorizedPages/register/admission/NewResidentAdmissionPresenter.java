package com.wg_planner.views.UnauthorizedPages.register.admission;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NewResidentAdmissionPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewResidentAdmissionPresenter.class);
    private NewResidentAdmissionView newResidentAdmissionView;

    @Autowired
    AdmissionHandler admissionHandler;

    public NewResidentAdmissionPresenter() {
    }

    public void init(NewResidentAdmissionView NewResidentAdmissionView) {
        this.newResidentAdmissionView = NewResidentAdmissionView;
    }

    public List<Room> verifyFloorCodeAndGetNonOccupiedRooms(String floorCode) {
        return admissionHandler.verifyFloorCodeAndGetVacantRoomsInFloor(floorCode);
    }

    public synchronized AdmissionCode generateAndSaveAdmissionCode(Room selectedRoom) {
        return admissionHandler.generateAndSaveAdmissionCode(selectedRoom);
    }

    long getValidityOfAdmissionCodeInMinutes() {
        return admissionHandler.getAdmissionCodeTimeoutInterval() / 60000;
    }

    public void waitForAdmissionStatusChange(AdmissionCode admissionCode) {
        synchronized (admissionHandler.getAdmissionDetails(admissionCode)) {
            LOGGER.info(LogHandler.getTestRun(), "waitForAdmissionStatusChange lock for admission details acquired");
            while (admissionHandler.getAdmissionDetails(admissionCode) != null &&
                    (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.PENDING ||
                            admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.WORKING)) {
                try {
                    admissionHandler.getAdmissionDetails(admissionCode).wait(admissionHandler.getAdmissionCodeTimeoutInterval());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LOGGER.info(LogHandler.getTestRun(), "waitForAdmissionStatusChange woke up from wait");
            if (admissionHandler.getAdmissionDetails(admissionCode) == null) { //removed after idle
                LOGGER.debug("admissionHandler.getAdmissionDetails(admissionCode) returned null, Room to admit " +
                        "{}, admission code {}", admissionHandler.getAdmissionDetails(admissionCode).getRoomToAdmit().toString(),
                        admissionCode.toString());
                newResidentAdmissionView.printErrorOccurred();
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.ADMITTED) {
                newResidentAdmissionView.onAccept();
                LOGGER.info(LogHandler.getTestRun(), "admissionHandler.getAdmissionStatus(admissionCode) = AdmissionDetails" +
                                ".AdmissionStatus.ADMITTED, Room to admit " + "{}, admission code {}",
                        admissionHandler.getAdmissionDetails(admissionCode).getRoomToAdmit().toString(),
                        admissionCode.toString());
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.REJECTED) {
                newResidentAdmissionView.onReject();
                LOGGER.info(LogHandler.getTestRun(), "admissionHandler.getAdmissionStatus(admissionCode) = AdmissionDetails" +
                                ".AdmissionStatus.REJECTED, Room to admit " + "{}, admission code {}",
                        admissionHandler.getAdmissionDetails(admissionCode).getRoomToAdmit().toString(),
                        admissionCode.toString());
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.TIME_OUT) {
                newResidentAdmissionView.onTimeOut();
                admissionHandler.getAdmissionDetails(admissionCode).notify();
                LOGGER.debug("admissionHandler.getAdmissionStatus(admissionCode) = AdmissionDetails" +
                                ".AdmissionStatus.TIME_OUT. Room to admit " + "{}, admission code {}",
                        admissionHandler.getAdmissionDetails(admissionCode).getRoomToAdmit().toString(),
                        admissionCode.toString());
                LOGGER.info(LogHandler.getTestRun(),"notify waiting thread done");
            }
        }
    }
}
