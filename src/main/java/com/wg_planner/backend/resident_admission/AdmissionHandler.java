package com.wg_planner.backend.resident_admission;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

@Controller
public class AdmissionHandler {
    private FloorService floorService;
    private AdmissionCodeStore admissionCodeStore;

    @Autowired
    public AdmissionHandler(FloorService floorService, AdmissionCodeStore admissionCodeStore) {
        this.floorService = floorService;
        this.admissionCodeStore = admissionCodeStore;
    }

    public List<Room> verifyFloorCodeAndGetVacantRoomsInFloor(String floorCode) {
        Floor floor = floorService.getFloorByFloorCode(floorCode);
        if (floor == null) {
            return null;
        }
        return floorService.getAllNonOccupiedRoomsInFloor(floor);
    }

    public synchronized AdmissionCode generateAndSaveAdmissionCode(Room roomToAdmit) {//synced to ensure unique admission codes
        Assert.notNull(roomToAdmit, "room to admit must not be null");
        Assert.isTrue(roomToAdmit.isOccupied() == false, "selected room isOccupied value must be false");
        Assert.isTrue(roomToAdmit.getResidentAccount() == null, "selected room residentAccount value must be null");

        AdmissionDetails admissionDetails = new AdmissionDetails(roomToAdmit);
        AdmissionCode admissionCode;
        do {
            admissionCode = new AdmissionCode(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE));
        } while (admissionCodeStore.containsAdmissionCode(admissionCode));
        if (admissionCodeStore.saveAdmissionCode(admissionCode, admissionDetails) == null) {//no previous mapping for the key
            return admissionCode;
        }
        return null;
    }

    public synchronized AdmissionDetails verifyAdmissionCodeAndGetAdmissionDetails(AdmissionCode admissionCode) {
        Assert.notNull(admissionCode, "admission code to verify must not be null");
        return admissionCodeStore.getAdmissionDetails(admissionCode);
    }
}
