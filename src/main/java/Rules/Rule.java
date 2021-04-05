package Rules;

import metrics.MethodMetrics;

import java.util.NoSuchElementException;

public class Rule {
    /*Example
    SE (LOC_method > 50 E CYCLO_method >10) ENTÃO A regra identificou o code smell Long Method
    neste método SENÃO A regra indica que o code smell Long Method não está presente neste método.LOC_class
        */

    enum Operation{
        BiggerThan,SmallerThan,Equal,Different,
        BiggerThanEqual,SmallerThanEqual,
    }
    enum LogicOp {
        AND,OR,XOR,
    }
    enum Smell{
        Long_Method, God_Class
    }

    private static class SubRule {
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

        private boolean passesSubRule(MethodMetrics method){
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

    public boolean passesRule(MethodMetrics method) throws NoSuchElementException{

        boolean passesrule1= rule1.passesSubRule(method);
        boolean passesrule2= rule2.passesSubRule(method);

        switch (operation){
            case AND:
                return passesrule1 && passesrule2;
            case OR:
                return passesrule1 || passesrule2;
            case XOR:
                //(car.isDiesel() && !car.isManual()) || (!car.isDiesel() && car.isManual())
                return ( (passesrule1 && !passesrule2) || (!passesrule1 && passesrule2));
            default:
                throw new NoSuchElementException("Non existant Logical Operation for rule passing, passesRule function");
        }
    }

    private static boolean Operation(int value1, Operation op, int value2) throws NoSuchElementException{
        switch (op){
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




}