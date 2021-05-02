package util;

public class Math {

    public static int calculateTotal(int trueP, int trueN, int falseP, int falseN){
        int total = trueP + trueN + falseN + falseP;
        return total;
    }

    public static Double calculatePrecision(int trueP, int falseP){
        double precision = trueP / (trueP + falseP);
        return precision;
    }

    public static Double calculateRecall(int trueP, int falseN){
        double recall = trueP / (trueP + falseN);
        return recall;
    }

    public static Double calculateError(int trueP, int trueN, int falseP, int falseN){
        int total = calculateTotal(trueP, trueN, falseP, falseN);
        double error = (falseN + falseP) / total;

        return error;
    }

    public static Double calculateAccuracy(int trueP, int trueN, int falseP, int falseN){
        int total = calculateTotal(trueP, trueN, falseP, falseN);
        double accuracy = (trueN + trueP) / total;

        return accuracy;
    }

    public String evaluateMeasure(Double measure) {
        if (measure == 0) {
            return "Worst";
        } else if (measure > 0 && measure < 0.5) {
            return "Bad";
        } else if (measure >= 0.5 && measure < 0.9) {
            return "Decent";
        } else if (measure > 0.9 && measure <= 1) {
            return "Great";
        } else {
            return "Quality measure incorrectly calculated";
        }
    }

}
