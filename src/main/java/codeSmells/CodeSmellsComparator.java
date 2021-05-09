package codeSmells;


import readers.ExelReader;
import util.Quadruple;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class CodeSmellsComparator {
    private MetricComparissonStats godClassStats = new MetricComparissonStats();
    private MetricComparissonStats longMethodStats = new MetricComparissonStats();
    private final File excelOriginal;
    private final File excelToCompare;


    private static final int indexOfIsGodClass = 7;
    private static final int indexOfIsLongMethod = 10;


    /**
     * <p>Object used to represent the comparison between two codeSmells xlsx files</p>
     *
     * @param excelOriginal  excel file generated from the Code Smells Creator class
     * @param excelToCompare excel file to compare to get the confusion matrix
     */
    public CodeSmellsComparator(File excelOriginal, File excelToCompare) {
        this.excelOriginal = excelOriginal;
        this.excelToCompare = excelToCompare;

        setConfusionTableValues();
    }

    /**
     * <p>Method used to extract the excel values from the columns of both given files in the constructor.</p>
     * <p>After this it will call set values function to assign the values to the following class variables.</p>
     * <ul>
     *     <li>truePositiveNumber</li>
     *     <li>trueNegativeNumber</li>
     *     <li>falsePositiveNumber</li>
     *     <li>falseNegativeNumber</li>
     *     <li>invalidComparisonsNumber</li>
     * </ul>
     */
    private void setConfusionTableValues() {
        ArrayList<Quadruple<String, String, String, String>> values = new ArrayList<>();

        ArrayList<String> toCompareLines = getToCompareLines();
        ArrayList<String> originalLines = getOriginalLines();
        toCompareLines.remove(0);
        originalLines.remove(0);

        for (int toCompareLinePos = 0; toCompareLinePos < toCompareLines.size(); toCompareLinePos++) {
            String toCompareLine = toCompareLines.get(toCompareLinePos);

            for (int originalLinePos = 0; originalLinePos < originalLines.size(); originalLinePos++) {
                String originalLine = originalLines.get(originalLinePos);

                String[] originalSplit = originalLine.toLowerCase(Locale.ROOT).split(";");
                String[] toCompareSplit = toCompareLine.toLowerCase(Locale.ROOT).split(";");

                if (originalSplit[1].equals(toCompareSplit[1])
                        && originalSplit[2].equals(toCompareSplit[2])
                        && originalSplit[3].equals(toCompareSplit[3])
                ) {

                    //For efficiency
                    //toCompareLines.remove(toCompareLine);

                    try {

                        values.add(new Quadruple<>(
                                  originalSplit[    indexOfIsGodClass   ]
                                , originalSplit[    indexOfIsLongMethod ]
                                , toCompareSplit[   indexOfIsGodClass   ]
                                , toCompareSplit[   indexOfIsLongMethod ]
                        ));
                    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        /*
                        System.out.println("Original" + originalLine);
                        System.out.println("ToCompare:" + toCompareLine);
                        arrayIndexOutOfBoundsException.printStackTrace();
                        */
                    }

                    break;
                }
            }
        }

        //  System.out.println(i);

        setValues(values);
    }

    /**
     * <p>Method called by setConfusionTableValues() to set the values of the following parameters:</p>
     * <ul>
     *     <li>[0] = truePositiveNumber</li>
     *     <li>[1] = trueNegativeNumber</li>
     *     <li>[2] = falsePositiveNumber</li>
     *     <li>[3] = falseNegativeNumber</li>
     *     <li>[4] = invalidComparisonsNumber</li>
     * </ul>
     *
     * @param values ArrayList of Quadruple with the values of the columns isGodClass and IsLongMethod of both excel files given at the constructor.
     */
    private void setValues(ArrayList<Quadruple<String, String, String, String>> values) {

        for (Quadruple<String, String, String, String> value : values) {

            compareValues(value.getA(), value.getC(), godClassStats.getComparisons());
            compareValues(value.getB(), value.getD(), longMethodStats.getComparisons());

        }

    }

    /**
     * Method used to compare the 2 values (original and toCompare) to conclude de veracity of the prediction.
     * <ul>
     *     <li>[0] = truePositiveNumber</li>
     *     <li>[1] = trueNegativeNumber</li>
     *     <li>[2] = falsePositiveNumber</li>
     *     <li>[3] = falseNegativeNumber</li>
     *     <li>[4] = invalidComparisonsNumber</li>
     * </ul>
     * @param originalValue  original value to compare
     * @param toCompareValue to compare value to compare
     */
    private void compareValues(String originalValue, String toCompareValue, int[] comparisons) {
        if (originalValue.equals("true")) {
            if (toCompareValue.equals("true") || toCompareValue.equals("1")) {
                comparisons[0]++; //truePositiveNumber
            } else if (toCompareValue.equals("false") || toCompareValue.equals("0")) {
                comparisons[2]++; //falsePositiveNumber
            } else {
                comparisons[4]++; //invalidComparisonsNumber
            }
        } else if (originalValue.equals("false")) {
            if (toCompareValue.equals("true") || toCompareValue.equals("1")) {
                comparisons[3]++; //falseNegativeNumber
            } else if (toCompareValue.equals("false") || toCompareValue.equals("0")) {
                comparisons[1]++; //trueNegativeNumber
            } else {
                comparisons[4]++; //invalidComparisonsNumber
            }
        } else if (originalValue.equals("na")) {
            comparisons[4]++; //invalidComparisonsNumber
        }
    }

    /**
     * <p>Method use to return the original excel file.</p>
     *
     * @return Original Excel file
     */
    public File getExcelOriginal() {

        return excelOriginal;
    }

    /**
     * <p>Method use to return the excel file used to compare with the original.</p>
     *
     * @return To compare Excel file
     */
    public File getExcelToCompare() {

        return excelToCompare;
    }

    /**
     * <p>Method used to get the true positive number parameter encapsulating the parameter of the class corresponding to the IsGodClass metric.</p>
     *
     * @return true positive number int.
     */
    public int getTruePositiveNumberIsGodClass() {
        return godClassStats.getTruePositiveNumber();
    }

    /**
     * <p>Method used to get the true positive number parameter encapsulating the parameter of the class corresponding to the IsLongMethod metric.</p>
     *
     * @return true positive number int.
     */
    public int getTruePositiveNumberIsLongMethod() {
        return longMethodStats.getTruePositiveNumber();
    }

    /**
     * <p>Method used to get the true negative number parameter encapsulating the parameter of the class corresponding to the IsGodClass metric.</p>
     *
     * @return true negative number int.
     */
    public int getTrueNegativeNumberIsGodClass() {
        return godClassStats.getTrueNegativeNumber();
    }

    /**
     * <p>Method used to get the true negative number parameter encapsulating the parameter of the class corresponding to the IsLongMethod metric.</p>
     *
     * @return true negative number int.
     */
    public int getTrueNegativeNumberIsLongMethod() {
        return longMethodStats.getTrueNegativeNumber();
    }

    /**
     * <p>Method used to get the false positive number parameter encapsulating the parameter of the class corresponding to the IsGodClass metric.</p>
     *
     * @return false positive number int.
     */
    public int getFalsePositiveNumberIsGodClass() {
        return godClassStats.getFalsePositiveNumber();
    }

    /**
     * <p>Method used to get the false positive number parameter encapsulating the parameter of the class corresponding to the IsLongMethod metric.</p>
     *
     * @return false positive number int.
     */
    public int getFalsePositiveNumberIsLongMethod() {
        return longMethodStats.getFalsePositiveNumber();
    }

    /**
     * <p>Method used to get the false negative number parameter encapsulating the parameter of the class corresponding to the IsGodClass metric.</p>
     *
     * @return false negative number int.
     */
    public int getFalseNegativeNumberIsGodClass() {
        return godClassStats.getFalseNegativeNumber();
    }

    /**
     * <p>Method used to get the false negative number parameter encapsulating the parameter of the class corresponding to the IsLongMethod metric.</p>
     *
     * @return false negative number int.
     */
    public int getFalseNegativeNumberIsLongMethod() {
        return longMethodStats.getFalseNegativeNumber();
    }

    /**
     * <p>Method used to get the number of comparisons where at least one of the values in nonexistent or invalid corresponding to the IsGodClass metric.</p>
     *
     * @return false negative number int.
     */
    public int getInvalidComparisonsNumberIsGodClass() {
        return godClassStats.getInvalidComparisonsNumber();
    }

    /**
     * <p>Method used to get the number of comparisons where at least one of the values in nonexistent or invalid corresponding to the IsLongMethod metric.</p>
     *
     * @return false negative number int.
     */
    public int getInvalidComparisonsNumberIsLongMethod() {
        return longMethodStats.getInvalidComparisonsNumber();
    }


    /**
     * <p>Method used to get the lines of the original excel file as a ArrayList of string.</p>
     *
     * @return ArrayList of String of the lines of the original excel file
     */
    private ArrayList<String> getOriginalLines() {

        return ExelReader.read(excelOriginal);
    }

    /**
     * <p>Method used to get the lines of the excel file to compare as a ArrayList of string.</p>
     *
     * @return ArrayList of String of the lines of the excel file to compare
     */
    private ArrayList<String> getToCompareLines() {

        return ExelReader.read(excelToCompare);
    }
}