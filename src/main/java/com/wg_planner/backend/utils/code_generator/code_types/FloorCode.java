package com.wg_planner.backend.utils.code_generator.code_types;

public class FloorCode {
    String floorCode;
    final int FLOOR_CODE_LENGTH = 4;

    public FloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        this.floorCode = floorCode;
    }

    public int getFloorCodeLength() {
        return FLOOR_CODE_LENGTH;
    }
}
