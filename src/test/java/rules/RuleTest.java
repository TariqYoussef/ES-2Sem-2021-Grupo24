package rules;

import codeSmells.CodeSmellsComparator;
import metrics.MethodMetrics;
import metrics.MetricExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleTest {
    
    static MetricExtractor extractor;
    static Rule rule1, rule2, rule3;
    static List<MethodMetrics> methods;

    @BeforeAll
    static void setUpBeforeClass() throws IOException {
        File testclass = new File("src/test/java/resources/QuickSort.java");
        extractor = new MetricExtractor();
        extractor.setSrcpath(testclass.toPath());
        methods = extractor.ExtractMetrics();
        //RUle é LOC_method > 10 AND CYCLO_METHOD > 5 then is lONG_Method
        rule1 = new Rule(Metric.LOC_method, Rule.Operation.BiggerThan, 10,
                Metric.CYCLO_method, Rule.Operation.BiggerThanEqual, 2,
                Rule.LogicOp.AND, Rule.Smell.Long_Method );

        //RUle é LOC_method == 10 OR CYCLO_METHOD != 5 then is lONG_Method
        rule2 = new Rule(Metric.LOC_method, Rule.Operation.Equal, 10,
                Metric.CYCLO_method, Rule.Operation.Different, 0,
                Rule.LogicOp.OR, Rule.Smell.Long_Method );

        //RUle é NOM_class < 10 XOR LOC_class <= 100 then is God_Class
        rule3 = new Rule(Metric.NOM_class, Rule.Operation.SmallerThan, 20,
                Metric.LOC_class, Rule.Operation.SmallerThanEqual, 100,
                Rule.LogicOp.XOR, Rule.Smell.God_Class );

    }

    /**
     * Test method for {@link Rule#passesRule(MethodMetrics)} (List)} ()}.
     * @throws java.io.IOException
     */
    @Test
    void passesRuleTest() throws IllegalStateException  {


        for (MethodMetrics method : methods) {
            switch (method.getMethod().getNameAsString()){
                case "partition":
                    assertEquals( true , rule1.passesRule(method));
                    assertEquals( true , rule2.passesRule(method));
                    assertEquals( false , rule3.passesRule(method));
                    break;
                case "quickSort":
                    assertEquals( false , rule1.passesRule(method));
                    assertEquals( true , rule2.passesRule(method));
                    assertEquals( false , rule3.passesRule(method));
                    break;
                case "main":
                    assertEquals( false , rule1.passesRule(method));
                    assertEquals( false , rule2.passesRule(method));
                    assertEquals( false , rule3.passesRule(method));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + method.getMethod().getNameAsString());
            }
        }

    }

    /**
     * Test method for {@link Rule#getOperation()}.
     *
     */
    @Test
    void getOperationTest() {
        assertEquals(Rule.LogicOp.AND,rule1.getOperation());
    }

    /**
     * Test method for {@link Rule#getSmell()}.
     *
     */
    @Test
    void getSmellTest() {
        assertEquals(Rule.Smell.Long_Method,rule1.getSmell());
    }

    /**
     * Test method for {@link Rule#getMetric(Rule.SubRule)}.
     *
     */
    @Test
    void getMetricTest() {
        assertEquals(Metric.LOC_method,rule1.getMetric(rule1.getRule1()));
    }

    /**
     * Test method for {@link Rule#getMetricOperation(Rule.SubRule)}.
     *
     */
    @Test
    void getMetricOperationTest() {
        assertEquals(Rule.Operation.BiggerThan,rule1.getMetricOperation(rule1.getRule1()));
    }

    /**
     * Test method for {@link Rule#getMetricValue(Rule.SubRule)}.
     *
     */
    @Test
    void getMetricValueTest() {
        assertEquals(10,rule1.getMetricValue(rule1.getRule1()));
    }

    /**
     * Test method for {@link Rule#toString()}.
     *
     */
    @Test
    void toStringTest() {

        String string = "If "+ Metric.LOC_method +" is "+ Rule.Operation.BiggerThan + ": " + 10 +
                " "+ Rule.LogicOp.AND +" If "+ Metric.CYCLO_method +" is "+ Rule.Operation.BiggerThanEqual +
                ": " + 2 +" Then is "+ Rule.Smell.Long_Method;

        assertEquals(string,rule1.toString());
    }

}