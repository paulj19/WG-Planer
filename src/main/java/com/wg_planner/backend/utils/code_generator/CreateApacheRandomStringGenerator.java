package com.wg_planner.backend.utils.code_generator;

public class CreateApacheRandomStringGenerator extends CreateRandomStringGenerator {
    @Override
    public RandomStringGenerator createRandomStringGenerator() {
        return ApacheRandomStringGenerator.getInstance();
    }
}
