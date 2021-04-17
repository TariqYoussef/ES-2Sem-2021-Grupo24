package metrics;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetricExtractorTest {

    static MetricExtractor extractor;

    /**
     * @throws java.io.IOException
     */
    @BeforeAll
    static void setUpBeforeClass() throws IOException {

        File testclass = new File("src/test/java/resources/QuickSort.java");
        extractor = new MetricExtractor();
        extractor.setSrcpath(testclass.toPath());

    }

    @BeforeEach
    void setUp() {

    }

    /**
     * Test method for {@link MetricExtractor#NOM_class(List)} ()}.
     * @throws java.io.IOException
     */
    @Test
    void testNom_Class() throws IOException {

        List<CompilationUnit> compilationUnits = extractor.CreateCompilationUnits();

        //List<Pair<ClassOrInterfaceDeclaration, Integer>> response = new LinkedList<Pair<ClassOrInterfaceDeclaration, Integer>>();
        //response.add(new Pair<>(compilationUnits.get(0).getLocalDeclarationFromClassname("QuickSort").get(0),3));

        int actual = extractor.NOM_class(compilationUnits).get(0).getD();
        assertEquals( 3 , actual);

    }

    /**
     * Test method for {@link MetricExtractor#CYCLO_method(List)} ()}.
     * @throws java.io.IOException
     */
    @Test
    void testCYCLO_method() throws IOException {
        List<CompilationUnit> compilationUnits = extractor.CreateCompilationUnits();
        int actual1 = extractor.CYCLO_method(compilationUnits).get(0).getD();
        assertEquals(2,actual1);
        int actual2 = extractor.CYCLO_method(compilationUnits).get(1).getD();
        assertEquals(1,actual2);
        int actual3 = extractor.CYCLO_method(compilationUnits).get(2).getD();
        assertEquals(0,actual3);

    }

    /**
     * Test method for {@link MetricExtractor#LOC_class(List)} ()}.
     * @throws java.io.IOException
     */
    @Test
    void testLOC_class() throws IOException {
        List<CompilationUnit> compilationUnits = extractor.CreateCompilationUnits();
        int actual = extractor.LOC_class(compilationUnits).get(1).getD();
        assertEquals(59,actual);
    }

    /**
     * Test method for {@link MetricExtractor#LOC_method(List)} ()}.
     * @throws java.io.IOException
     */
    @Test
    void testLOC_method() throws IOException {
        List<CompilationUnit> compilationUnits = extractor.CreateCompilationUnits();
        int actual = extractor.LOC_class(compilationUnits).get(12).getD();
        assertEquals(8,actual);
    }

}