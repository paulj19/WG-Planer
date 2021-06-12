package com.wg_planner.backend.utils.code_generator;


import com.wg_planner.backend.Service.FloorService;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ApacheRandomStringGenerator extends MyRandomStringGenerator {

    private RandomStringGenerator randomStringGenerator;
    private FloorService floorService;

    @Autowired
    public ApacheRandomStringGenerator(FloorService floorService) {
        this.floorService = floorService;
        randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'Z')
                        .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
                        .build();
    }

    @Override
    public String generateRandomString(int length) {
        String generatedString;
        do {
            generatedString = randomStringGenerator.generate(length);
        } while (!floorService.isFloorCodeUnique(generatedString));

        return generatedString;
    }
}
