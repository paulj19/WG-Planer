package com.wg_planner.backend.resident_admission;

public class AdmissionCode {
    private String admissionCode;

    private AdmissionCode() {//so that no object generated with the code
    }

    public AdmissionCode(String admissionCode) {
        this.admissionCode = admissionCode;
    }

    public String getAdmissionCode() {
        return admissionCode;
    }

    public void setAdmissionCode(String admissionCode) {
        this.admissionCode = admissionCode;
    }

    public String toString(){
        return admissionCode;
    }

}
