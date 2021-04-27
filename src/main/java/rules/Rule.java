package rules;

import metrics.MethodMetrics;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 */
public class Rule implements java.io.Serializable {
    public static final String pathSeries = "rules/rulehistory.ser";
    /*Example
    SE (LOC_method > 50 E CYCLO_method >10) ENTÃO A regra identificou o code smell Long Method
    neste método SENÃO A regra indica que o code smell Long Method não está presente neste método.LOC_class
        */

    public enum Operation{
        BiggerThan,SmallerThan,Equal,Different,
        BiggerThanEqual,SmallerThanEqual,
    }
    public enum LogicOp {
        AND,OR,XOR,
    }

    public enum Smell {
        Long_Method, God_Class
    }

    /**
     *
     */
    private static class SubRule implements java.io.Serializable {
        private static final long serialVersionUID = 6529385098262757690L;

        private final Metric metric;
        private final Operation operation;
        private final Integer value;
        public SubRule(Metric metric, Operation operation, Integer value) {
            this.metric = metric;
            this.operation = operation;
            this.value = value;
        }
        public Metric getMetric() {
            return metric;
        }
        public Operation getOperation() {
            return operation;
        }
        public Integer getValue() {
            return value;
        }

        private boolean passesSubRule(MethodMetrics method) {
            int metricValue = method.getMetric(metric);
            return Operation(metricValue, operation, value);
        }
    }

    /**
     * @param metric1
     * @param operation1
     * @param value1
     * @param metric2
     * @param operation2
     * @param value2
     * @param logicoperation
     * @param smell
     */
    /*
    public Rule(SubRule rule1, SubRule rule2, LogicOp logicoperation, Smell smell) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.operation = logicoperation;
        this.smell = smell;
    }
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
    public SubRule getRule1() {
        return rule1;
    }
    public SubRule getRule2() {
        return rule2;
    }
    public LogicOp getOperation() {
        return operation;
    }
    public Smell getSmell() {
        return smell;
    }

    /**
     * @param sr SubRule given
     * @return returns the metric of the subrule
     */
    public Metric getMetric(SubRule sr) {
        return sr.metric;
    }

    /**
     * @param sr SubRule given
     * @return returns the metric operation of the subrule
     */
    public Operation getMetricOperation(SubRule sr) {
        return sr.operation;
    }

    /**
     * @param sr SubRule given
     * @return returns the metric value of the subrule
     */
    public Integer getMetricValue(SubRule sr) {
        return sr.value;
    }

    /**
     * @param method
     * @return
     * @throws NoSuchElementException
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
     * @param value1
     * @param op
     * @param value2
     * @return
     * @throws NoSuchElementException
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

    /**
     * Method to store an ArrayList of rules to rules/rulehistory.ser, replacing the old set of rules
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
     * Method do get the current set of rules stored in rules/rulehistory.ser
     * In case the file rulehistory.ser is not found, it's created and added an empty ArrayList of rules
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
     * A new ArrayList of Rule is created, the current rules are obtained from the deserializeRule method.
     * All the rules from the current rule list are passed to the new ArrayList,
     * unless the rule is equal to the rule we want to delete, in this case the for each loop skips it,
     * finally the new ArrayList is serialized and it replaces the current set of rules.
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
     *  A new ArrayList of Rule is created, the current rules are obtained from the deserializeRule method.
     *  All the rules from the current rule list are passed to the new ArrayList,
     *  unless the rule is equal to the rule we want to update, in this case that rule is swapped with the new Rule.
     *  finally the new ArrayList is serialized and it replaces the current set of rules.
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
     * The rule given enters a for each loop and is compared to all the rules in the rulehistory.ser file
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
     * @return A detailed way to see the rule selected.
     */
    @Override
    public String toString() {
        return "Metric 1: " + rule1.metric + ", Operation: " + rule1.operation + ", Value: " + rule1.value +
                "\nMetric 2: " + rule2.metric + ", Operation: " + rule2.operation + ", Value: " + rule2.value +
                "\nOperation: " + operation +
                "\nSmell: " + smell;
    }

}