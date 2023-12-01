package br.com.university.days;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FirstDayTest {

    final PuzzleResolver resolver = new FirstDay();

    @Test
    @DisplayName("Should answer correclty the default problem")
    void first(){
        assertEquals("54630", resolver.solveDefaultProblem());
    }

    @Test
    @DisplayName("Should answer correctly the complete problem")
    void second(){
        assertEquals("54770", resolver.solveCompleteProblem());
    }
}
