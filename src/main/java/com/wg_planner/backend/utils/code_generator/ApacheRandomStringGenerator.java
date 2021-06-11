package com.wg_planner.backend.utils.code_generator;


import org.apache.commons.text.CharacterPredicates;

public class ApacheRandomStringGenerator extends RandomStringGenerator {

    private static ApacheRandomStringGenerator apacheRandomStringGenerator;

    static {
        apacheRandomStringGenerator = new ApacheRandomStringGenerator();
    }

    private ApacheRandomStringGenerator() {
    }

    public static ApacheRandomStringGenerator getInstance() {
        return apacheRandomStringGenerator;
    }

    @Override
    public String generateRandomString(int length) {
        org.apache.commons.text.RandomStringGenerator randomStringGenerator =
                new org.apache.commons.text.RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        return randomStringGenerator.generate(length);
    }
}
