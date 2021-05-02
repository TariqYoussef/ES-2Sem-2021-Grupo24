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
        File excelToCompare = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_compare_metrics.xlsx");
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
        File expected = new File("src/test/java/resources/ES-2Sem-2021-Grupo24_compare_metrics.xlsx");
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getTruePositiveNumber()}.
     *
     */
    @Test
    void getTruePositiveNumber() {
        int actual = codeSmellsComparator.getTruePositiveNumber();
        assertEquals(0, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getTrueNegativeNumber()}.
     *
     */
    @Test
    void getTrueNegativeNumber() {
        int actual = codeSmellsComparator.getTrueNegativeNumber();
        assertEquals(92, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getFalsePositiveNumber()}.
     *
     */
    @Test
    void getFalsePositiveNumber() {
        int actual = codeSmellsComparator.getFalsePositiveNumber();
        assertEquals(0, actual);
    }

    /**
     * Test method for {@link CodeSmellsComparator#getFalseNegativeNumber()}.
     *
     */
    @Test
    void getFalseNegativeNumber() {
        int actual = codeSmellsComparator.getFalseNegativeNumber();
        assertEquals(0, actual);
    }
}