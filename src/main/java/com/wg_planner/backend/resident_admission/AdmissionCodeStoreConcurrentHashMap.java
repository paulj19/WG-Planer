package com.wg_planner.backend.resident_admission;

import java.util.concurrent.ConcurrentHashMap;

public class AdmissionCodeStoreConcurrentHashMap implements AdmissionCodeStore {

    private ConcurrentHashMap<AdmissionCode, AdmissionDetails> admissionCodes = new ConcurrentHashMap<>();

    public AdmissionCodeStoreConcurrentHashMap() {
    }

    @Override
    public AdmissionDetails saveAdmissionCode(AdmissionCode admissionCode, AdmissionDetails admissionDetails) {
        return admissionCodes.putIfAbsent(admissionCode, admissionDetails); //null checks taken care during the insertion into map itself
    }

    @Override
    public synchronized AdmissionDetails getAdmissionDetails(AdmissionCode admissionCode) {
        return admissionCodes.get(admissionCode);
    }

    @Override
    public synchronized AdmissionDetails removeAdmissionCode(AdmissionCode admissionCode) {
        return admissionCodes.remove(admissionCode);
    }

}
