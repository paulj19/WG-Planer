package com.wg_planner.backend.utils.code_generator;

import com.wg_planner.backend.utils.ApplicationContextHolder;
import org.springframework.stereotype.Service;

public class CreateApacheRandomStringGenerator extends CreateRandomStringGenerator {

    @Override
    public MyRandomStringGenerator createRandomStringGenerator() {
            return ApplicationContextHolder.getContext().getBean(ApacheRandomStringGenerator.class);
    }
}
