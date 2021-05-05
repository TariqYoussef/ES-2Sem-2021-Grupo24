package rules;

import metrics.MethodMetrics;
import metrics.MetricExtractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleTest {
    
    static MetricExtractor extractor;
    static Rule rule1;
    static Rule rule2;
    static Rule rule3;
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
     * Test method for {@link Rule.SubRule#getMetric()}.
     *
     */
    @Test
    void subRuleGetMetricTest() {
        assertEquals(Metric.LOC_method,rule1.getRule1().getMetric());
    }

    /**
     * Test method for {@link Rule.SubRule#getOperation()}.
     *
     */
    @Test
    void subRuleGetOperationTest() {
        assertEquals(Rule.Operation.BiggerThan,rule1.getRule1().getOperation());
    }

    /**
     * Test method for {@link Rule.SubRule#getMetric()}.
     *
     */
    @Test
    void subRuleGetValueTest() {
        assertEquals(10,rule1.getRule1().getValue());
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
    /**
     * Test method for {@link Rule#serializeRule(ArrayList)}.
     * Test method for {@link Rule#deserializedRule()} .
     *
     */
    @Test
    void serializationRuleTest() throws IOException, ClassNotFoundException {
        String testRuleSer = "src/test/java/resources/ruleTest.ser";
        ArrayList<Rule> rulesToWrite = new ArrayList<>();
        rulesToWrite.add(rule1);
        rulesToWrite.add(rule2);
        rulesToWrite.add(rule3);
        Rule.serializeRule(rulesToWrite,testRuleSer);

        ArrayList<Rule> rulesRead = Rule.deserializedRule(testRuleSer) ;
        for (int i = 0 ; i<rulesRead.size()-1;i++) {

            assertEquals(rulesToWrite.get(i).getRule1().getMetric()     ,rulesRead.get(i).getRule1().getMetric() );
            assertEquals(rulesToWrite.get(i).getRule1().getOperation()  ,rulesRead.get(i).getRule1().getOperation());
            assertEquals(rulesToWrite.get(i).getRule1().getValue()      ,rulesRead.get(i).getRule1().getValue() );
            assertEquals(rulesToWrite.get(i).getRule2().getMetric()     ,rulesRead.get(i).getRule2().getMetric() );
            assertEquals(rulesToWrite.get(i).getRule2().getOperation()  ,rulesRead.get(i).getRule2().getOperation());
            assertEquals(rulesToWrite.get(i).getRule2().getValue()      ,rulesRead.get(i).getRule2().getValue() );

            assertEquals(rulesToWrite.get(i).getOperation(),rulesRead.get(i).getOperation() );
            assertEquals(rulesToWrite.get(i).getSmell(),rulesRead.get(i).getSmell() );
            assertEquals(rulesToWrite.get(i).getSmell(),rulesRead.get(i).getSmell() );
        }



    }

}