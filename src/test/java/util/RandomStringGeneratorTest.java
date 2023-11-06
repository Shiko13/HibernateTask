package util;

import org.epam.util.RandomStringGenerator;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomStringGeneratorTest {

    @Test
    public void testGenerateRandomString() {
        int length = 10;
        String randomString = RandomStringGenerator.generateRandomString(length);

        assertEquals(length, randomString.length());

        assertTrue(randomString.matches("[A-Za-z0-9]+"));
    }

    @Test
    public void testGenerateUniqueRandomStrings() {
        int numberOfStrings = 100;
        int length = 8;
        Set<String> generatedStrings = new HashSet<>();

        for (int i = 0; i < numberOfStrings; i++) {
            String randomString = RandomStringGenerator.generateRandomString(length);
            generatedStrings.add(randomString);
        }

        assertEquals(numberOfStrings, generatedStrings.size());
    }
}
