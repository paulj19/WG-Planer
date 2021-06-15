package com.wg_planner.backend.resident_admission;

public interface AdmissionCodeStore {
    AdmissionDetails saveAdmissionCode(AdmissionCode admissionCode, AdmissionDetails admissionDetails);
    AdmissionDetails getAdmissionDetails(AdmissionCode admissionCode);
    AdmissionDetails removeAdmissionCode(AdmissionCode admissionCode);
}
