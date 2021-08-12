package com.wg_planner.views.UnauthorizedPages.register.admission;

import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;

public class AdmitNewResidentPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdmitNewResidentPresenter.class);

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
                admitNewResidentView.printMessage("New Resident request to room " + admissionHandler.getAdmissionDetails(admissionCode).getRoomToAdmit().getRoomName() + " " + admissionStatus.toString());
                LOGGER.info(LogHandler.getTestRun(), "admission code {} status set to {}", admissionCode, admissionStatus.toString());
            }
            admissionHandler.getAdmissionDetails(admissionCode).notify();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Admission code {} notified waiting thread",
                    SessionHandler.getLoggedInResidentAccount().getId(), admissionCode);
        }
    }

    private boolean isAlreadyAcceptedOrRejected(AdmissionDetails admissionDetails) {
        if (admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.ADMITTED ||
                admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.REJECTED) {
            admitNewResidentView.printAlreadyDoneMessage(admissionDetails.getRoomToAdmit().getRoomName(),
                    admissionDetails.getAdmissionStatus().toString());
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Room {} already accepted or rejected",
                    SessionHandler.getLoggedInResidentAccount().getId(), admissionDetails.getRoomToAdmit().toString());
            return true;
        }
        return false;
    }

    public boolean isAdmissionCodeInvalid(AdmissionCode admissionCode) {
        return admissionHandler.getAdmissionDetails(admissionCode) == null;
    }

    public void onSubmitAdmissionCode(String admissionCodeSubmitted) {
        AdmissionCode admissionCode = new AdmissionCode(admissionCodeSubmitted);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. admission code submitted {}",
                SessionHandler.getLoggedInResidentAccount().getId(),
                admissionCodeSubmitted);

        if (admissionHandler.getAdmissionDetails(admissionCode) == null) { //not present in map
            admitNewResidentView.setInvalidCodeMessage();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. AdmissionHandler.getAdmissionDetails(admissionCode) = null, " +
                            "admissionCode {}", SessionHandler.getLoggedInResidentAccount().getId(),
                    admissionCodeSubmitted);
            return;
        }
        synchronized (admissionHandler.getAdmissionDetails(admissionCode)) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Acquired lock for admission code {}",
                    SessionHandler.getLoggedInResidentAccount().getId(), admissionCode);
            AdmissionDetails admissionDetails = admissionHandler.getAdmissionDetails(admissionCode);
            if (admissionDetails.getAdmissionStatus() == AdmissionDetails.AdmissionStatus.PENDING) {
                admissionHandler.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.WORKING);
                LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Admission status for admission code {} was AdmissionStatus" +
                        ".PENDING set to, AdmissionStatus.WORKING", SessionHandler.getLoggedInResidentAccount().getId(), admissionCode);
            }
            sanityCheckAssertions(admissionDetails);
            if (!admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor())) {
                //submitted room not in current loggedIn floor
                LOGGER.error("Resident Account id {}. Room does not belong to this floor, must not happen. Admission Code {}. " +
                                "LoggedInResidentAccount {}. " +
                                "RoomToAdmit {}. ", SessionHandler.getLoggedInResidentAccount().getId(), admissionCode.toString(),
                        SessionHandler.getLoggedInResidentAccount().toString(),
                        admissionDetails.getRoomToAdmit().toString());
                admitNewResidentView.setInvalidCodeMessage();
            } else {
                admitNewResidentView.addAcceptRejectButtons(admissionCode, admissionDetails.getRoomToAdmit().getRoomName());
                LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding accept reject buttons for admission code {}",
                        SessionHandler.getLoggedInResidentAccount().getId(),
                        admissionCode);
            }
        }
    }

    private void sanityCheckAssertions(AdmissionDetails admissionDetails) {
        assert admissionDetails.getRoomToAdmit() != null;
        assert !admissionDetails.getRoomToAdmit().isOccupied();
        assert admissionDetails.getRoomToAdmit().getResidentAccount() == null;
        assert admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
    }
}
