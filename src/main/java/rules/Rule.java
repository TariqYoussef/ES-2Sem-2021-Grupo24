package rules;

import metrics.MethodMetrics;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * <p>Rule is a class used to represent two conditions that together determine wether a specific MethodMetrics object is constitutes some kind of Smell</p>
 * <p>In order to achieve this we have at our disposal various enum types:</p>
 * <ul>
 * <li>{@link Operation}</li>
 * <li>{@link LogicOp}</li>
 * <li>{@link Smell}</li>
 * </ul>
 * <p>The inner class {@link SubRule} is also used to represent Rules</p>
 * <p>Each Rule contains two {@link SubRule}, </p>
 * <p>
 *  Example SE (LOC_method > 50 E CYCLO_method >10) ENTÃO A regra identificou o code smell Long Method neste método SENÃO A regra indica que o code smell Long Method não está presente neste método.LOC_class
 * </p>
 * @see MethodMetrics
 * @see SubRule
 * @see Metric
 * @see LogicOp
 * @see Operation
 * @see Smell
 *
 */
public class Rule implements java.io.Serializable {
    public static final String pathSeries = "rules/rulehistory.ser";
    private static final long serialVersionUID = 41L;


    /**
     * <p>Operation is an enum used to categorize mathematical logical operations to compare two diferent integer values</p>
     * <p>Operation contains theese values </p>
     * <ul>
     *     <li>BiggerThan - ">" </li>
     *     <li>SmallerThan - "<" </li>
     *     <li>Equal - "=="</li>
     *     <li>Different - "!="</li>
     *     <li>BiggerThanEqual - ">="</li>
     *     <li>SmallerThanEqual - "<="</li>
     * </ul>
     */
    public enum Operation{
        BiggerThan,SmallerThan,Equal,Different,
        BiggerThanEqual,SmallerThanEqual,
    }

    /**
     *<p>LogicOp is an enum containing theese values </p>
     * <ul>
     *     <li>AND - And logic Operation, only if both inputs are true the result is also true</li>
     *     <li>OR - Or logic Operation, only one input needs to be true for the result to also be true</li>
     *     <li>XOR - Xor logic Operation, only if both inputs are different from each other the result is also true</li>
     * </ul>
     */
    public enum LogicOp {
        AND,OR,XOR,
    }

    /**
     *<p>A Smell represents a kind of codeSmell,  </p>
     *<p>Smell contains the following values </p>
     * <ul>
     *     <li>Long_Method - for a method that is determined to be too Long, it should be  </li>
     *     <li>God_Class - for a class that is determined to execute too much, with no specific identity</li>
     * </ul>
     */
    public enum Smell {
        Long_Method, God_Class
    }


    /**<p>A SubRule is a simple rule</p>
     *<p>Each contains one {@link Metric}, one {@link Operation} and an Integer value </p>
     *
     * @see Metric
     * @see Operation
     *
     */
    private static class SubRule implements java.io.Serializable {
        private static final long serialVersionUID = 40L;

        private final Metric metric;
        private final Operation operation;
        private final Integer value;

        /**
         * @param metric {@link Metric}
         * @param operation {@link Operation}
         * @param value integer
         */
        public SubRule(Metric metric, Operation operation, Integer value) {
            this.metric = metric;
            this.operation = operation;
            this.value = value;
        }

        /**
         * @return the {@link Metric} of the SubRule
         */
        public Metric getMetric() {
            return metric;
        }

        /**
         * @return the {@link Operation} of the SubRule
         */
        public Operation getOperation() {
            return operation;
        }

        /**
         * @return the integer value of the SubRule
         */
        public Integer getValue() {
            return value;
        }

        /**
         * <p>Recieves a {@link MethodMetrics} and checks if its corresponding {@link Metric} is bigger or smaller or etc
         * (see {@link LogicOp} for more options) than the extracted value for that {@link Metric} </p>
         *
         * @param method {@link MethodMetrics}
         * @return boolean
         *
         * @see MethodMetrics
         * @see LogicOp
         */
        private boolean passesSubRule(MethodMetrics method) {
            int metricValue = method.getMetric(metric);
            return Operation(metricValue, operation, value);
        }
    }

    /*
    public Rule(SubRule rule1, SubRule rule2, LogicOp logicoperation, Smell smell) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.operation = logicoperation;
        this.smell = smell;
    }
    */
    /**
     * <p>Constructor for a Rule, itnerlnaly creates two {@link SubRule} and stores them inside itself together with the {@link LogicOp} and a {@link Smell} </p>
     *
     * @param metric1 {@link Metric} used to create the first {@link SubRule}
     * @param operation1 {@link Operation} used to create the first {@link SubRule}
     * @param value1 integer value used to create the first {@link SubRule}
     * @param metric2 {@link Metric} used to create the second {@link SubRule}
     * @param operation2 {@link Operation} used to create the second {@link SubRule}
     * @param value2 integer value used to create the second {@link SubRule}
     * @param logicoperation a {@link LogicOp} used to link the two inner {@link SubRule}
     * @param smell a {@link Smell} used to determine the codeSmell that the rule is
     */
    public Rule(Metric metric1, Operation operation1, Integer value1, Metric metric2,
                Operation operation2, Integer value2, LogicOp logicoperation, Smell smell) {
        this.rule1 = new SubRule(metric1, operation1, value1);
        this.rule2 = new SubRule(metric2, operation2, value2);
        this.operation = logicoperation;
        this.smell = smell;
    }
    private final SubRule rule1;
    private final SubRule rule2;
    private final LogicOp operation;
    private final Smell smell;

    /**
     * @return {@link SubRule}
     */
    public SubRule getRule1() {
        return rule1;
    }

    /**
     * @return {@link SubRule}
     */
    public SubRule getRule2() {
        return rule2;
    }

    /**
     * @return {@link LogicOp}
     */
    public LogicOp getOperation() {
        return operation;
    }

    /**
     * @return {@link Smell}
     */
    public Smell getSmell() {
        return smell;
    }

    /**
     * @param sr {@link SubRule} given
     * @return the {@link Metric} of the {@link SubRule}
     */
    public Metric getMetric(SubRule sr) {
        return sr.metric;
    }

    /**
     * @param sr {@link SubRule} given
     * @return the {@link Metric} operation of the subrule
     */
    public Operation getMetricOperation(SubRule sr) {
        return sr.operation;
    }

    /**
     * @param sr {@link SubRule} given
     * @return the {@link Metric} value of the subrule
     */
    public Integer getMetricValue(SubRule sr) {
        return sr.value;
    }

    /**
     * <p>Receives a  {@link MethodMetrics} and checks all of its stored {@link Metric} and </p>
     *
     * @param method {@link MethodMetrics}
     * @return boolean
     * @throws NoSuchElementException
     *
     * @see Metric
     * @see MethodMetrics
     */
    public boolean passesRule(MethodMetrics method) throws NoSuchElementException {

        boolean passesrule1 = rule1.passesSubRule(method);
        boolean passesrule2 = rule2.passesSubRule(method);

        switch (operation) {
            case AND:
                return passesrule1 && passesrule2;
            case OR:
                return passesrule1 || passesrule2;
            case XOR:
                //(car.isDiesel() && !car.isManual()) || (!car.isDiesel() && car.isManual())
                return ((passesrule1 && !passesrule2) || (!passesrule1 && passesrule2));
            default:
                throw new NoSuchElementException("Non existant Logical Operation for rule passing, passesRule function");
        }
    }

    /**
     * <p>Method that compares two input integer values and based on the {@link Operation} parameter returns a boolean</p>
     * <p>Example if {@link Operation} is a BiggerThan and value1 is 1 and value2 is 2 then the method will return value1 > value2 that is TRUE in this example</p>
     *
     * @param value1 integer
     * @param op {@link Operation}
     * @param value2 integer
     * @return boolean
     * @throws NoSuchElementException for when the received Operation is not programmed in this method
     *
     *
     */
    private static boolean Operation(int value1, Operation op, int value2) throws NoSuchElementException {
        switch (op) {
            case BiggerThan:
                return value1 > value2;
            case BiggerThanEqual:
                return value1 >= value2;
            case Equal:
                return value1 == value2;
            case Different:
                return value1 != value2;
            case SmallerThan:
                return value1 < value2;
            case SmallerThanEqual:
                return value1 <= value2;
            default:
                throw new NoSuchElementException("Non existant Operation for rule passing, Operation function");

        }
    }
//TODO fazer função pa escrever apenas uma rule no file
    /**
     * <p>Method to store an ArrayList of rules to rules/rulehistory.ser, replacing the old set of rules</p>
     * @param rules ArrayList of rules to be stored
     * @throws IOException IO operation failed
     */
    public static void serializeRule(ArrayList<Rule> rules) throws IOException {
            FileOutputStream fileOut = new FileOutputStream(pathSeries);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(rules);
            out.close();
            fileOut.close();
    }

    /**
     * <p>Method do get the current set of rules stored in rules/rulehistory.ser</p>
     * <p>In case the file rulehistory.ser is not found, it's created and added an empty ArrayList of rules</p>
     * @return An ArrayList with a set of rules
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist
     */
    public static ArrayList<Rule> deserializedRule() throws IOException, ClassNotFoundException {
        ArrayList<Rule> result;
        try {
            FileInputStream fileIn = new FileInputStream(pathSeries);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Rule> rules = (ArrayList<Rule>) in.readObject();

            in.close();
            fileIn.close();
            result = rules;
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectory(Paths.get("rules"));
            } catch (FileAlreadyExistsException fileAlreadyExistsException) {

            }
            FileOutputStream fileOut = new FileOutputStream(pathSeries);
            serializeRule(new ArrayList<>());
            fileOut.close();
            result = new ArrayList<>();
        }
        return result;
    }

    /**
     * <p>A new ArrayList of Rule is created, the current rules are obtained from the deserializeRule method.
     * All the rules from the current rule list are passed to the new ArrayList,
     * unless the rule is equal to the rule we want to delete, in this case the for each loop skips it,
     * finally the new ArrayList is serialized and it replaces the current set of rules.</p>
     * @param r Rule to be deleted
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist.
     */
    public static void deleteRule(Rule r) throws IOException, ClassNotFoundException {
        ArrayList<Rule> newRules = new ArrayList<>();
        for (Rule rule:deserializedRule()){
            if(!rule.toString().equals(r.toString()))
                newRules.add(rule);
        }
        serializeRule(newRules);
    }

    /**
     *  <p>A new ArrayList of Rule is created, the current rules are obtained from the deserializeRule method.
     *  All the rules from the current rule list are passed to the new ArrayList,
     *  unless the rule is equal to the rule we want to update, in this case that rule is swapped with the new Rule.
     *  finally the new ArrayList is serialized and it replaces the current set of rules.</p>
     * @param oldRule Rule to be changed
     * @param newRule Rule updated
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist.
     */
    public static void changeRule(Rule oldRule, Rule newRule) throws IOException, ClassNotFoundException {
        ArrayList<Rule> newRules = new ArrayList<>();
        for (Rule rule:deserializedRule()){
            if(!rule.toString().equals(oldRule.toString()))
                newRules.add(rule);
            else {
                newRules.add(newRule);
            }
        }
        serializeRule(newRules);
    }

    /**
     * <p>The rule given enters a for each loop and is compared to all the rules in the rulehistory.ser file</p>
     * @param rule rule to be checked
     * @return true if rule already exists and false if not
     * @throws IOException IO operation failed
     * @throws ClassNotFoundException Class Rule doesn't exist
     */
    public static boolean doesRuleExist(Rule rule) throws IOException, ClassNotFoundException {
        ArrayList<Rule> rules2add = Rule.deserializedRule();
        for (Rule rule2Compare:rules2add) {
            System.out.println(rule2Compare);
            if(rule2Compare.toString().equals(rule.toString())){
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Returns a {@link Rule} as a String, as if it was a standart English phrase</p>
     * @return A detailed way to see the rule as a String.
     */
    @Override
    public String toString() {
        /*
        return "Metric 1: " + rule1.metric + ", Operation: " + rule1.operation + ", Value: " + rule1.value +
                "\nMetric 2: " + rule2.metric + ", Operation: " + rule2.operation + ", Value: " + rule2.value +
                "\nOperation: " + operation +
                "\nSmell: " + smell;
        */
        return "If "+ rule1.metric +" is "+ rule1.operation + ": " + rule1.value +" "+ operation +" If "+ rule2.metric +" is "+ rule2.operation + ": " + rule2.value +" Then is "+ smell;
    }

}