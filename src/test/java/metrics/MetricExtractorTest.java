package metrics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

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

        int actual = extractor.NOM_class(compilationUnits).get(0).getB();
        assertEquals( 3 , actual);

    }
}