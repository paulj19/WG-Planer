package com.wg_planner.views.register.admission;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NewResidentAdmissionPresenter {
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

    public void waitForAdmissionStatusChange(AdmissionCode admissionCode) {
        synchronized (admissionHandler.getAdmissionDetails(admissionCode)) {
            while (admissionHandler.getAdmissionDetails(admissionCode) != null &&
                    (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.PENDING ||
                            admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.WORKING)) {
                try {
                    admissionHandler.getAdmissionDetails(admissionCode).wait(admissionHandler.getAdmissionCodeTimeoutInterval());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (admissionHandler.getAdmissionDetails(admissionCode) == null) { //removed after idle
                newResidentAdmissionView.printErrorOccurred();
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.ADMITTED) {
                newResidentAdmissionView.onAccept();
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.REJECTED) {
                newResidentAdmissionView.onReject();
            } else if (admissionHandler.getAdmissionStatus(admissionCode) == AdmissionDetails.AdmissionStatus.TIME_OUT) {
                newResidentAdmissionView.onTimeOut();
                admissionHandler.getAdmissionDetails(admissionCode).notify();
            }
        }
    }
}
