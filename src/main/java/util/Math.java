package util;

public class Math {

    /**
     * @return return the total of the quality metrics
     * @param trueP
     * @param trueN
     * @param falseP
     * @param falseN
     */
    public static double calculateTotal(double trueP, double trueN, double falseP, double falseN){
        double total = trueP + trueN + falseN + falseP;
        return total;
    }


    /**
     * @return return the precision of the positive predictions
     * @param trueP
     * @param falseP
     */
    public static Double calculatePrecision(double trueP, double falseP){
        if(trueP == 0){
            return 0.0;
        }
        double precision = trueP / (trueP + falseP);
        return precision;
    }


    /**
     * @return return the percentage of true positives among all positives
     * @param trueP
     * @param falseN
     */
    public static Double calculateRecall(double trueP, double falseN){
        if(trueP == 0){
            return 0.0;
        }
        double recall = trueP / (trueP + falseN);
        return recall;
    }

    /**
     * @return return the percentage of wrong predictions
     * @param trueP
     * @param trueN
     * @param falseP
     * @param falseN
     */
    public static Double calculateError(double trueP, double trueN, double falseP, double falseN){
        if (falseN == 0 && falseP == 0){
            return 0.0;
        }
        double total = calculateTotal(trueP, trueN, falseP, falseN);
        double error = (falseN + falseP) / total;

        return error;
    }

    /**
     * @return return the accuracy of the predictions
     * @param trueP
     * @param trueN
     * @param falseP
     * @param falseN
     */
    public static Double calculateAccuracy(double trueP, double trueN, double falseP, double falseN){
        if(trueP == 0 && trueN == 0){
            return 0.0;
        }
        double total = calculateTotal(trueP, trueN, falseP, falseN);
        double accuracy = (trueN + trueP) / total;

        return accuracy;
    }

    /**
     * @return return the quality of the measure
     * @param measure
     */
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
