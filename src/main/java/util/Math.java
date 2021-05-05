package util;

public class Math {

    /**
     * <p>Method used to calculate the total of all the quality metrics</p>
     *
     * @return return the total of the quality metrics
     * @param trueP number of true positives
     * @param trueN number of true negatives
     * @param falseP number of false positives
     * @param falseN number of false negatives
     */
    public static int calculateTotal(double trueP, double trueN, double falseP, double falseN){
        double total = trueP + trueN + falseN + falseP;
        return (int) total;
    }


    /**
     * <p>Method used to calculate the precision of the predictions</p>
     *
     * @return return the precision of the predictions
     * @param trueP number of true positives
     * @param falseP number of false positives
     */
    public static Double calculatePrecision(double trueP, double falseP){
        if(trueP == 0){
            return 0.0;
        }
        double precision = trueP / (trueP + falseP);
        return precision;
    }


    /**
     * <p>Method used to calculate the percentage of the correct true predictions</p>
     *
     * @return return the percentage of true positives
     * @param trueP number of true positives
     * @param falseN number of false negatives
     */
    public static Double calculateRecall(double trueP, double falseN){
        if(trueP == 0){
            return 0.0;
        }
        double recall = trueP / (trueP + falseN);
        return recall;
    }

    /**
     * <p>Method used to calculate the the percentage of wrong predictions among all metrics</p>
     *
     * @return return the percentage of wrong predictions
     * @param trueP number of true positives
     * @param trueN number of true negatives
     * @param falseP number of false positives
     * @param falseN number of false negatives
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
     * <p>Method used to calculate the accuracy of the starting predictions</p>
     *
     * @return return the accuracy of the predictions
     * @param trueP number of true positives
     * @param trueN number of true negatives
     * @param falseP number of false positives
     * @param falseN number of false negatives
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
     * <p>Method used evaluate the quality of each measure</p>
     *
     * @return return the quality of the measure
     * @param measure quality measure
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
