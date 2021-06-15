package com.wg_planner.backend.resident_admission;

import com.wg_planner.backend.entity.Room;

public class AdmissionDetails {
    public enum NewResidentAdmissionStatus {
        ADMITTED, PENDING, REJECTED
    }

    private Room roomToAdmit;
    private NewResidentAdmissionStatus newResidentAdmissionStatus = NewResidentAdmissionStatus.PENDING;

    public synchronized void setNewResidentAdmissionStatus(NewResidentAdmissionStatus newResidentAdmissionStatus) {
        this.newResidentAdmissionStatus = newResidentAdmissionStatus;
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

    public NewResidentAdmissionStatus getNewResidentAdmissionStatus() {
        return newResidentAdmissionStatus;
    }
}
