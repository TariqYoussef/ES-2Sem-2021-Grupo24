package codeSmells;

import metrics.MetricExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CodeSmellsComparatorTest {

    static CodeSmellsComparator codeSmellsComparator;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        File excelOriginal = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_metrics.xlsx");
        File excelToCompare = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_metrics_-_Copy.xlsx");
        codeSmellsComparator = new CodeSmellsComparator(excelOriginal,excelToCompare);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getExcelOriginal()}.
     *
     */
    @Test
    void getExcelOriginal() {
        File actual = codeSmellsComparator.getExcelOriginal();
        File expected = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_metrics.xlsx");
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getExcelToCompare()}.
     *
     */
    @Test
    void getExcelToCompare() {
        File actual = codeSmellsComparator.getExcelToCompare();
        File expected = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_metrics_-_Copy.xlsx");
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getTruePositiveNumberIsGodClass()}.
     *
     */
    @Test
    void getTruePositiveNumberIsGodClass() {
        int actual = codeSmellsComparator.getTruePositiveNumberIsGodClass();
        assertEquals(22, actual);
    }
    /**
     * Test method for {@link CodeSmellsComparator#getTruePositiveNumberIsLongMethod()} ()}.
     *
     */
    @Test
    void getTruePositiveNumberIsLongMethod() {
        int actual = codeSmellsComparator.getTruePositiveNumberIsLongMethod();
        assertEquals(4, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getTrueNegativeNumberIsGodClass()}.
     *
     */
    @Test
    void getTrueNegativeNumberIsGodClass() {
        int actual = codeSmellsComparator.getTrueNegativeNumberIsGodClass();
        assertEquals(34, actual);
    }
    /**
     * Test method for {@link CodeSmellsComparator#getTrueNegativeNumberIsLongMethod()}.
     *
     */
    @Test
    void getTrueNegativeNumberIsLongMethod() {
        int actual = codeSmellsComparator.getTrueNegativeNumberIsLongMethod();
        assertEquals(71, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getFalsePositiveNumberIsGodClass()}.
     *
     */
    @Test
    void getFalsePositiveNumberIsGodClass() {
        int actual = codeSmellsComparator.getFalsePositiveNumberIsGodClass();
        assertEquals(1, actual);
    }
    /**
     * Test method for {@link CodeSmellsComparator#getFalsePositiveNumberIsGodClass()}.
     *
     */
    @Test
    void getFalsePositiveNumberIsLongMethod() {
        int actual = codeSmellsComparator.getFalsePositiveNumberIsLongMethod();
        assertEquals(0, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getFalseNegativeNumberIsGodClass()}.
     *
     */
    @Test
    void getFalseNegativeNumberIsGodClass() {
        int actual = codeSmellsComparator.getFalseNegativeNumberIsGodClass();
        assertEquals(37, actual);
    }
    /**
     * Test method for {@link CodeSmellsComparator#getFalseNegativeNumberIsLongMethod()}.
     *
     */
    @Test
    void getFalseNegativeNumberIsLongMethod() {
        int actual = codeSmellsComparator.getFalseNegativeNumberIsLongMethod();
        assertEquals(19, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getInvalidComparisonsNumberIsGodClass()}.
     *
     */
    @Test
    void getInvalidComparisonsNumberIsGodClass() {
        int actual = codeSmellsComparator.getInvalidComparisonsNumberIsGodClass();
        assertEquals(0, actual);
    }
    /**
     * Test method for {@link CodeSmellsComparator#getInvalidComparisonsNumberIsLongMethod()}.
     *
     */
    @Test
    void getInvalidComparisonsNumberIsLongMethod() {
        int actual = codeSmellsComparator.getInvalidComparisonsNumberIsLongMethod();
        assertEquals(0, actual);
    }
}