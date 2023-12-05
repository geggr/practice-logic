package br.com.university.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThirdDayTest {
    
    private final PuzzleResolver resolver = new ThirdDay();

    @Test
    @DisplayName("Should answer correctly the default problem")
    void first(){
        assertEquals("532331", resolver.solveDefaultProblem());
    }

    @Test
    @DisplayName("Should answer correctly the complete problem")
    void second(){
        assertEquals("82301120", resolver.solveCompleteProblem());
    }
}
