package rules;

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
    static Rule rule1, rule2, rule3,rule4;
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




}