package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathTest {
    
    /**
     * Test method for {@link Math#calculateTotal(double, double, double, double)} .
     */
    @Test
    void testCalculateTotal(){
        assertEquals(4, Math.calculateTotal(1,1,1,1));
    }

    /**
     * Test method for {@link Math#calculatePrecision(double, double)} .
     */
    @Test
    void testCalculatePrecision(){
        assertEquals(0.5, Math.calculatePrecision(1,1));
    }

    /**
     * Test method for {@link Math#calculateRecall(double, double)} .
     */
    @Test
    void testCalculateRecall(){
        assertEquals(0.5, Math.calculateRecall(1,1));
    }

    /**
     * Test method for {@link Math#calculateError(double, double, double, double)} .
     */
    @Test
    void testCalculateError(){
        assertEquals(0.5, Math.calculateError(1,1,1,1));
    }

    /**
     * Test method for {@link Math#calculateAccuracy(double, double, double, double)} .
     */
    @Test
    void testCalculateAccuracy(){
        assertEquals(0.5, Math.calculateAccuracy(1,1,1,1));
    }

    /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasure(){
        assertEquals("Decent", Math.evaluateMeasure(0.7));
    }
}
