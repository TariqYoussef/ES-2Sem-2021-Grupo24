package util;

import metrics.MetricExtractor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuadrupleTest {
    static Quadruple<String,String,String,String> testQuad;

    @BeforeEach
    void setUp() {
        testQuad = new Quadruple<>("a","b","c","d");
    }

    /**
     * Test method for {@link Quadruple#getA()}.
     */
    @org.junit.jupiter.api.Test
    void getA() {
        assertEquals("a",testQuad.getA());
    }
    /**
     * Test method for {@link Quadruple#setA(Object)}.
     */
    @org.junit.jupiter.api.Test
    void setA() {
        String changed = "changed";
        testQuad.setA(changed);
        assertEquals(changed,testQuad.getA());
    }
    /**
     * Test method for {@link Quadruple#getB()}.
     */
    @org.junit.jupiter.api.Test
    void getB() {
        assertEquals("b",testQuad.getB());
    }
    /**
     * Test method for {@link Quadruple#setB(Object)}.
     */
    @org.junit.jupiter.api.Test
    void setB() {
        String changed = "changed";
        testQuad.setB(changed);
        assertEquals(changed,testQuad.getB());
    }
    /**
     * Test method for {@link Quadruple#getC()}.
     */
    @org.junit.jupiter.api.Test
    void getC() {
        assertEquals("c",testQuad.getC());
    }
    /**
     * Test method for {@link Quadruple#setC(Object)} .
     */
    @org.junit.jupiter.api.Test
    void setC() {
        String changed = "changed";
        testQuad.setC(changed);
        assertEquals(changed,testQuad.getC());
    }
    /**
     * Test method for {@link Quadruple#getD()}.
     */
    @org.junit.jupiter.api.Test
    void getD() {
        assertEquals("d",testQuad.getD());
    }
    /**
     * Test method for {@link Quadruple#setD(Object)}.
     */
    @org.junit.jupiter.api.Test
    void setD() {
        String changed = "changed";
        testQuad.setD(changed);
        assertEquals(changed,testQuad.getD());
    }

    @Test
    void testToString() {
        String toString="Quadruple{java.lang.String=a, java.lang.String=b, java.lang.String=c, java.lang.String=d, }";
        assertEquals( toString, testQuad.toString());

    }
}