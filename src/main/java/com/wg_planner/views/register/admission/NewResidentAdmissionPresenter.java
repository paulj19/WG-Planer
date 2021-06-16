package com.wg_planner.views.register.admission;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.AdmissionCode;
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

    public AdmissionCode generateAndSaveAdmissionCode(Room selectedRoom) {
        return admissionHandler.generateAndSaveAdmissionCode(selectedRoom);
    }
}
