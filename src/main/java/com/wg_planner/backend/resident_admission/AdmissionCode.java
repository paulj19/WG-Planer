package com.wg_planner.backend.resident_admission;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AdmissionCode {
    private String admissionCode;

    private AdmissionCode() {//so that no object generated without the code
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

    public String toString() {
        return admissionCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof AdmissionCode))
            return false;
        AdmissionCode otherAdmissionCode = (AdmissionCode) other;
        return new EqualsBuilder()
                .append(getAdmissionCode(), otherAdmissionCode.getAdmissionCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAdmissionCode())
                .toHashCode();
    }

}
