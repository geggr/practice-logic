package br.com.university.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SecondDayTest {

    private final PuzzleResolver resolver = new SecondDay();

    @Test
    @DisplayName("Should answer correctly the default problem")
    void first(){
        assertEquals("2476", resolver.solveDefaultProblem());
    }

    @Test
    @DisplayName("Should answer correctly the complete problem")
    void second(){
        assertEquals("54911", resolver.solveCompleteProblem());
    }
}
