package com.wg_planner.backend.resident_admission;

import com.wg_planner.backend.entity.Room;

public class AdmissionDetails {
    public enum AdmissionStatus {
        PENDING("pending"), WORKING("working"), ADMITTED("admitted"), REJECTED("rejected"), TIME_OUT("time out");
        private String statusAsString;

        AdmissionStatus(String statusAsString) {
            this.statusAsString = statusAsString;
        }

        public String toString() {
            return statusAsString;
        }
    }

    private Room roomToAdmit;
    private AdmissionStatus admissionStatus = AdmissionStatus.PENDING;

    public synchronized void setAdmissionStatus(AdmissionStatus admissionStatus) {
        this.admissionStatus = admissionStatus;
        //todo if status=ADMITTED invalidate other operations
    }

    public AdmissionDetails(Room roomToAdmit) {
        this.roomToAdmit = roomToAdmit;
    }

    public Room getRoomToAdmit() {
        return roomToAdmit;
    }

    public void setRoomToAdmit(Room roomToAdmit) {
        this.roomToAdmit = roomToAdmit;
    }

    public AdmissionStatus getAdmissionStatus() {
        return admissionStatus;
    }
}
