package com.wg_planner.backend.utils.code_generator.custom_code_generator;

import com.wg_planner.backend.utils.code_generator.CreateApacheRandomStringGenerator;
import com.wg_planner.backend.utils.code_generator.CreateRandomStringGenerator;
import com.wg_planner.backend.utils.code_generator.MyRandomStringGenerator;

public class CustomCodeCreator {
    public enum CodeGenerationPurposes {
        FLOOR_CODE(4), ADMISSION_CODE(4);
        private final int codeLength;

        CodeGenerationPurposes(int codeLength) {
            this.codeLength = codeLength;
        }

        public int getCodeLength() {
            return codeLength;
        }
    }

    private CreateRandomStringGenerator createRandomStringGenerator = new CreateApacheRandomStringGenerator();
    private MyRandomStringGenerator randomStringGenerator = createRandomStringGenerator.createRandomStringGenerator();//todo maybe codeGenerationManager or something
    private static CustomCodeCreator customCodeCreator;

    static {
        customCodeCreator = new CustomCodeCreator();
    }

    private CustomCodeCreator() {
    }

    public static CustomCodeCreator getInstance() {
        return customCodeCreator;
    }

    public String generateCode(CodeGenerationPurposes codeGenerationPurposes) {
        return randomStringGenerator.generateRandomString(codeGenerationPurposes.getCodeLength());
    }
}
