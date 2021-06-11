package com.wg_planner.backend.utils.code_generator.floor_code_generator;

import com.wg_planner.backend.utils.code_generator.CreateApacheRandomStringGenerator;
import com.wg_planner.backend.utils.code_generator.CreateRandomStringGenerator;
import com.wg_planner.backend.utils.code_generator.RandomStringGenerator;

public class FloorCodeGenerator {
    CreateRandomStringGenerator createRandomStringGenerator = new CreateApacheRandomStringGenerator();
    RandomStringGenerator randomStringGenerator = createRandomStringGenerator.createRandomStringGenerator();
    private static FloorCodeGenerator floorCodeGenerator;
    final int FLOOR_CODE_LENGTH = 4;

    static {
        floorCodeGenerator = new FloorCodeGenerator();
    }

    private FloorCodeGenerator() {
    }

    public static FloorCodeGenerator getInstance() {
        return floorCodeGenerator;
    }

    public String getFloorCode() {
        return randomStringGenerator.generateRandomString(FLOOR_CODE_LENGTH);
    }
}
