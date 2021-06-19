package com.wg_planner.views.register.admission;

import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdmitNewResidentPresenter {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(AdmitNewResidentPresenter.class
            .getName());
    private AdmitNewResidentView admitNewResidentView;
    @Autowired
    AdmissionHandler admissionHandler;

    public AdmitNewResidentPresenter(AdmitNewResidentView admitNewResidentView) {
        this.admitNewResidentView = admitNewResidentView;
    }

    public void setAdmissionStatus(AdmissionCode admissionCode, AdmissionDetails.AdmissionStatus admissionStatus) {
        synchronized (admissionHandler.getAdmissionDetails(admissionCode)) {
            if (!isAlreadyAcceptedOrRejected(admissionHandler.getAdmissionDetails(admissionCode))) {
                admissionHandler.setAdmissionStatus(admissionCode, admissionStatus);
                admitNewResidentView.printMessage("Request has been " + admissionStatus.toString());
            }
            admissionHandler.getAdmissionDetails(admissionCode).notify();
        }
    }

    private boolean isAlreadyAcceptedOrRejected(AdmissionDetails admissionDetails) {
        if (admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.ADMITTED ||
                admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.REJECTED) {
            admitNewResidentView.printAlreadyDoneMessage(admissionDetails.getRoomToAdmit().getRoomName(),
                    admissionDetails.getAdmissionStatus().toString());
            return true;
        }
        return false;
    }

    public boolean isAdmissionCodeInvalid(AdmissionCode admissionCode) {
        return admissionHandler.getAdmissionDetails(admissionCode) == null;
    }

    public void onSubmitAdmissionCode(String admissionCodeSubmitted) {
        AdmissionCode admissionCode = new AdmissionCode(admissionCodeSubmitted);
        synchronized (admissionHandler.getAdmissionDetails(admissionCode)) {
            AdmissionDetails admissionDetails = admissionHandler.getAdmissionDetails(admissionCode);
            if (admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.PENDING)
                admissionHandler.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.WORKING);
            sanityCheckAssertions(admissionDetails);
            if (admissionDetails == null) { //not present in map
                admitNewResidentView.setInvalidCodeMessage();
            } else if (!admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor())) {//submitted room not in current loggedIn floor
                LOGGER.log(Level.SEVERE,
                        "room does not belong to this floor, must not happen. Admission Code: " + admissionCode.toString() +
                                "LoggedInResidentAccount: " + SessionHandler.getLoggedInResidentAccount().toString() +
                                "RoomToAdmit: " + admissionDetails.getRoomToAdmit().toString());
                admitNewResidentView.setInvalidCodeMessage();
            } else {
                admitNewResidentView.addAcceptRejectButtons(admissionCode, admissionDetails.getRoomToAdmit().getRoomName());
            }
        }
    }

    private void sanityCheckAssertions(AdmissionDetails admissionDetails) {
        assert admissionDetails.getRoomToAdmit() != null;
        assert admissionDetails.getRoomToAdmit().isOccupied() == false;
        assert admissionDetails.getRoomToAdmit().getResidentAccount() == null;
        assert admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
    }
}
