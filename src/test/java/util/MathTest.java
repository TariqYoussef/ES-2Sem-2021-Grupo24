package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathTest {


    @Test
    void testCalculateTotal(){
        assertEquals(4, Math.calculateTotal(1,1,1,1));
    }

    @Test
    void testCalculatePrecision(){
        assertEquals(0.5, Math.calculatePrecision(1,1));
    }

    @Test
    void testCalculateRecall(){
        assertEquals(0.5, Math.calculateRecall(1,1));
    }

    @Test
    void testCalculateError(){
        assertEquals(0.5, Math.calculateError(1,1,1,1));
    }

    @Test
    void testCalculateAccuracy(){
        assertEquals(0.5, Math.calculateAccuracy(1,1,1,1));
    }

    @Test
    void testEvaluateMeasure(){
        assertEquals("Decent", Math.evaluateMeasure(0.7));
    }
}
