package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairTest {
    static Pair<String, String> testPairObject;

    @BeforeEach
    void setUp() {
        testPairObject = new Pair<>("a", "b");
    }

    /**
     * Test method for {@link Pair#getA()}.
     */
    @Test
    void getA() {
        assertEquals("a", testPairObject.getA());
    }

    /**
     * Test method for {@link Pair#getB()}.
     */
    @Test
    void getB() {
        assertEquals("b", testPairObject.getB());

    }

    /**
     * Test method for {@link Pair#setA(Object)}.
     */
    @Test
    void setA() {
        String changed = "changed";
        testPairObject.setA(changed);
        assertEquals(changed, testPairObject.getA());
    }

    /**
     * Test method for {@link Pair#setB(Object)}.
     */
    @Test
    void setB() {
        String changed = "changed";
        testPairObject.setB(changed);
        assertEquals(changed, testPairObject.getB());
    }
}