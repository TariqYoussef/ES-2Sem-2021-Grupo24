package Rules;

public class Rule {
    /*Example
    SE (LOC_method > 50 E CYCLO_method >10) ENTÃO A regra identificou o code smell Long Method
    neste método SENÃO A regra indica que o code smell Long Method não está presente neste método.
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

    private class subRule{
        Metric metric;
        Operation operation;
        Integer value;

        public subRule(Metric metric1, Operation operation2, Integer value) {
            this.metric = metric1;
            this.operation = operation2;
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
    }

    public Rule(subRule rule1, subRule rule2, LogicOp operation, Smell smell) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.operation = operation;
        this.smell = smell;
    }

    public Rule(Metric metric1, Operation operation1, Integer value1, Metric metric2, Operation operation2, Integer value2, LogicOp logicoperation, Smell smell) {
        new Rule(new subRule(metric1,operation1,value1),new subRule(metric2,operation2,value2),logicoperation,smell);
    }

    private subRule rule1;
    private subRule rule2;
    private LogicOp operation;
    private Smell smell;


    public subRule getRule1() {
        return rule1;
    }

    public subRule getRule2() {
        return rule2;
    }

    public LogicOp getOperation() {
        return operation;
    }

    public Smell getSmell() {
        return smell;
    }









}