package util;

public class Math {

    public static double calculateTotal(double trueP, double trueN, double falseP, double falseN){
        double total = trueP + trueN + falseN + falseP;
        return total;
    }

    public static Double calculatePrecision(double trueP, double falseP){
        if(trueP == 0){
            return 0.0;
        }
        double precision = trueP / (trueP + falseP);
        return precision;
    }

    public static Double calculateRecall(double trueP, double falseN){
        if(trueP == 0){
            return 0.0;
        }
        double recall = trueP / (trueP + falseN);
        return recall;
    }

    public static Double calculateError(double trueP, double trueN, double falseP, double falseN){
        if (falseN == 0 && falseP == 0){
            return 0.0;
        }
        double total = calculateTotal(trueP, trueN, falseP, falseN);
        double error = (falseN + falseP) / total;

        return error;
    }

    public static Double calculateAccuracy(double trueP, double trueN, double falseP, double falseN){
        if(trueP == 0 && trueN == 0){
            return 0.0;
        }
        double total = calculateTotal(trueP, trueN, falseP, falseN);
        double accuracy = (trueN + trueP) / total;

        return accuracy;
    }

    public static String evaluateMeasure(Double measure) {
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
