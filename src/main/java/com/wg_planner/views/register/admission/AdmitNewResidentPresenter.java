package com.wg_planner.views.register.admission;

import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.resident_admission.AdmissionHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class AdmitNewResidentPresenter {
    private AdmitNewResidentView admitNewResidentView;
    @Autowired
    AdmissionHandler admissionHandler;

    public AdmitNewResidentPresenter(AdmitNewResidentView admitNewResidentView) {
        this.admitNewResidentView = admitNewResidentView;
    }

    public AdmissionDetails verifyAdmissionCodeAndGetAdmissionDetails(String admissionCode) {
        return admissionHandler.verifyAdmissionCodeAndGetAdmissionDetails(new AdmissionCode(admissionCode));
    }
}
