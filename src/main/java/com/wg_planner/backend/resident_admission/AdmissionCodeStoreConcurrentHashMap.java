package com.wg_planner.backend.resident_admission;

import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;

@Controller
public class AdmissionCodeStoreConcurrentHashMap implements AdmissionCodeStore {

    private ConcurrentHashMap<AdmissionCode, AdmissionDetails> admissionCodesMap = new ConcurrentHashMap<>();

    public AdmissionCodeStoreConcurrentHashMap() {
    }

    @Override
    public AdmissionDetails saveAdmissionCode(AdmissionCode admissionCode, AdmissionDetails admissionDetails) {
        return admissionCodesMap.putIfAbsent(admissionCode, admissionDetails); //null checks taken care during the insertion into map itself
    }

    @Override
    public AdmissionDetails getAdmissionDetails(AdmissionCode admissionCode) {
        return admissionCodesMap.get(admissionCode);
    }

    @Override
    public AdmissionDetails removeAdmissionCode(AdmissionCode admissionCode) {
        return admissionCodesMap.remove(admissionCode);
    }

    @Override
    public boolean containsAdmissionCode(AdmissionCode admissionCode) {
        return admissionCodesMap.containsKey(admissionCode);
    }
}
