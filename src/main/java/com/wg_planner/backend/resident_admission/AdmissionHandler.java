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
    private final long admissionCodeTimeoutInterval = 600000; //10 min
    private final long admissionCodeRemovalInterval = 180000; //3 min; to let the user know timeout instead of removing and showing invalid
    TimerRelapse setAdmissionStatusToTimeout =
            (admissionCode) -> {
                if (admissionCode instanceof AdmissionCode) {
                    synchronized (getAdmissionDetails((AdmissionCode) admissionCode)) {
                        if (getAdmissionDetails((AdmissionCode) admissionCode).getAdmissionStatus() == AdmissionDetails.AdmissionStatus.WORKING) {
                            //if the admit/reject screen is left open; remove after 20 min
                            try {
                                Thread.sleep(2 * admissionCodeTimeoutInterval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            setAdmissionStatus((AdmissionCode) admissionCode, AdmissionDetails.AdmissionStatus.TIME_OUT);
                            try {
                                getAdmissionDetails((AdmissionCode) admissionCode).wait(admissionCodeRemovalInterval);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        admissionCodeStore.removeAdmissionCode((AdmissionCode) admissionCode);
                    }
                }
            };

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

    public AdmissionCode generateAndSaveAdmissionCode(Room roomToAdmit) {//synced to ensure unique admission codes
        Assert.notNull(roomToAdmit, "room to admit must not be null");
        Assert.isTrue(!roomToAdmit.isOccupied(), "selected room isOccupied value must be false");
        Assert.isTrue(roomToAdmit.getResidentAccount() == null, "selected room residentAccount value must be null");

        AdmissionDetails admissionDetails = new AdmissionDetails(roomToAdmit);
        AdmissionCode admissionCode;
        do {
            admissionCode =
                    new AdmissionCode(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE));
        } while (admissionCodeStore.containsAdmissionCode(admissionCode));
        if (admissionCodeStore.saveAdmissionCode(admissionCode, admissionDetails) == null) {//no previous mapping for the key
            EventTimer.getInstance().setTimer(admissionCode, setAdmissionStatusToTimeout, admissionCodeTimeoutInterval - 500);
            return admissionCode;
        }
        return null;
    }

    public AdmissionDetails getAdmissionDetails(AdmissionCode admissionCode) {
        Assert.notNull(admissionCode, "admission code to verify must not be null");
        return admissionCodeStore.getAdmissionDetails(admissionCode);
    }

    public AdmissionDetails.AdmissionStatus getAdmissionStatus(AdmissionCode admissionCode) {
        return admissionCodeStore.getAdmissionDetails(admissionCode).getAdmissionStatus();
    }

    public void setAdmissionStatus(AdmissionCode admissionCode, AdmissionDetails.AdmissionStatus admissionStatus) {
        assert admissionCodeStore.getAdmissionDetails(admissionCode) != null;
        admissionCodeStore.getAdmissionDetails(admissionCode).setAdmissionStatus(admissionStatus);
    }

    public long getAdmissionCodeTimeoutInterval() {
        return admissionCodeTimeoutInterval;
    }
}
