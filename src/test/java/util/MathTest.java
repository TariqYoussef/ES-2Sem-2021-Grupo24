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
     * Test method for {@link Math#calculatePrecision(double, double)} .
     */
    @Test
    void testCalculatePrecisionWith0(){
        assertEquals(0.0, Math.calculatePrecision(0,1));
    }

    /**
     * Test method for {@link Math#calculateRecall(double, double)} .
     */
    @Test
    void testCalculateRecall(){
        assertEquals(0.5, Math.calculateRecall(1,1));
    }

    /**
     * Test method for {@link Math#calculateRecall(double, double)} .
     */
    @Test
    void testCalculateRecallnWith0(){
        assertEquals(0.0, Math.calculateRecall(0,1));
    }

    /**
     * Test method for {@link Math#calculateError(double, double, double, double)} .
     */
    @Test
    void testCalculateError(){
        assertEquals(0.5, Math.calculateError(1,1,1,1));
    }

    /**
     * Test method for {@link Math#calculateError(double, double, double, double)} .
     */
    @Test
    void testCalculateErrornWith0(){
        assertEquals(0.0, Math.calculateError(1,1,0,0));
    }

    /**
     * Test method for {@link Math#calculateAccuracy(double, double, double, double)} .
     */
    @Test
    void testCalculateAccuracy(){
        assertEquals(0.5, Math.calculateAccuracy(1,1,1,1));
    }

    /**
     * Test method for {@link Math#calculateAccuracy(double, double, double, double)} .
     */
    @Test
    void testCalculateAccuracynWith0(){
        assertEquals(0.0, Math.calculateAccuracy(0,0,1,1));
    }

    /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasure(){
        assertEquals("Decent", Math.evaluateMeasure(0.7));
    }

    /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasureWORST(){
        assertEquals("Worst", Math.evaluateMeasure(0.0));
    }
    /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasureBAD(){
        assertEquals("Bad", Math.evaluateMeasure(0.2));
    }
    /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasureDECENT(){
        assertEquals("Great", Math.evaluateMeasure(0.92));
    }
        /**
     * Test method for {@link Math#evaluateMeasure(Double)} .
     */
    @Test
    void testEvaluateMeasureINCORRECTLYCALC(){
        assertEquals("Quality measure incorrectly calculated", Math.evaluateMeasure(2.0));
    }



}
