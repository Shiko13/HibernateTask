package model;

import org.epam.model.TrainingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingTypeTest {

    @Test
    public void testConstructorAndGetters() {
        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setName("Type A");

        assertEquals(1L, trainingType.getId());
        assertEquals("Type A", trainingType.getName());
    }
}

