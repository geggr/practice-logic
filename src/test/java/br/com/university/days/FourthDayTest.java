package br.com.university.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FourthDayTest {
     private final PuzzleResolver resolver = new FourthDay();

    @Test
    @DisplayName("Should answer correctly the default problem")
    void first(){
        assertEquals("23235", resolver.solveDefaultProblem());
    }

    @Test
    @DisplayName("Should answer correctly the complete problem")
    void second(){
        assertEquals("5920640", resolver.solveCompleteProblem());
    }
}
