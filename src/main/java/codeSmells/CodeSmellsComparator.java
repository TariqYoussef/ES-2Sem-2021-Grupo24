package codeSmells;


import readers.ExelReader;
import util.Quadruple;

import java.io.File;
import java.util.*;

public class CodeSmellsComparator {
    private final File excelOriginal;
    private final File excelToCompare;

    /**
     *
     * <p>The comparisons array stores all the values resulting from the execution of the methods in this class. Their positions are:</p>
     * <ul>
     *     <li>[0] = truePositiveNumber</li>
     *     <li>[1] = trueNegativeNumber</li>
     *     <li>[2] = falsePositiveNumber</li>
     *     <li>[3] = falseNegativeNumber</li>
     *     <li>[4] = invalidComparisonsNumber</li>
     * </ul>
     *
     */
    private int[] comparisons = {0,0,0,0,0};

//    private int truePositiveNumber = 0;
//    private int trueNegativeNumber = 0;
//    private int falsePositiveNumber = 0;
//    private int falseNegativeNumber = 0;
//
//    private int invalidComparisons = 0;

    /**
     * <p>Object used to represent the comparison between two codeSmells xlsx files</p>
     * <p></p>
     *
     * @param excelOriginal excel file generated from the Code Smells Creator class
     * @param excelToCompare excel file to compare to get the confusion matrix
     */
    public CodeSmellsComparator(File excelOriginal, File excelToCompare) {
        this.excelOriginal = excelOriginal;
        this.excelToCompare = excelToCompare;

        setConfusionTableValues();
    }

    /**
     *
     * <p>Method used to extract the excel values from the columns of both given files in the constructor.</p>
     * <p>After this it will call set values function to assign the values to the following class variables.</p>
     * <ul>
     *     <li>truePositiveNumber</li>
     *     <li>trueNegativeNumber</li>
     *     <li>falsePositiveNumber</li>
     *     <li>falseNegativeNumber</li>
     *     <li>invalidComparisonsNumber</li>
     * </ul>
     *
     */
    private void setConfusionTableValues(){
        ArrayList<Quadruple<String,String,String,String>> values = new ArrayList<>();

        ArrayList<String> toCompareLines=  getToCompareLines();
        ArrayList<String> originalLines = getOriginalLines();
        toCompareLines.remove(0);
        originalLines.remove(0);

        //debugging purposes must be the number of methods in the project
        int i = 0;

        for(int toCompareLinePos = 0; toCompareLinePos < toCompareLines.size(); toCompareLinePos++){
            String toCompareLine = toCompareLines.get(toCompareLinePos);

            for(int originalLinePos = 0; originalLinePos < originalLines.size(); originalLinePos++){
                String originalLine = originalLines.get(originalLinePos);


                String[] originalSplit=originalLine.toLowerCase(Locale.ROOT).split(";");
                String[] toCompareSplit = toCompareLine.toLowerCase(Locale.ROOT).split(";");

                if(originalSplit[1].equals(toCompareSplit[1])
                        && originalSplit[2].equals(toCompareSplit[2])
                        && originalSplit[3].equals(toCompareSplit[3])){
                    //System.out.println("Original: "+originalLine+"\ntoCompare: " + toCompareLine);

                    //For efficiency
                    toCompareLines.remove(toCompareLine);

                    try {

                        values.add(new Quadruple<>(originalSplit[7], originalSplit[10],toCompareSplit[7],toCompareSplit[10]));
                    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        System.out.println("Original"+originalLine);
                        System.out.println("ToCompare:"+toCompareLine);
                        arrayIndexOutOfBoundsException.printStackTrace();
                    }

                    //System.out.println(valuesLine);
                    i++;
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
     *     <li>truePositiveNumber</li>
     *     <li>trueNegativeNumber</li>
     *     <li>falsePositiveNumber</li>
     *     <li>falseNegativeNumber</li>
     * </ul>
     *
     * @param values ArrayList of Quadruple with the values of the columns isGodClass and IsLongMethod of both excel files given at the constructor.
     *
     */
    private void setValues( ArrayList<Quadruple<String,String,String,String>> values){

        for(Quadruple<String,String,String,String> value: values){

            compareValues(value.getA(), value.getC());
            compareValues(value.getB(), value.getD());

        }

    }

    /**
     * Method used to compare the 2 values (original and toCompare) to conclude de veracity of the prediction.
     *
     * @param originalValue original value to compare
     * @param toCompareValue to compare value to compare
     *
     */
    private void compareValues(String originalValue, String toCompareValue){
        if(originalValue.equals("true")){
            if(toCompareValue.equals("true")|| toCompareValue.equals("1")){
                comparisons[0]++;
            }else if(toCompareValue.equals("false")|| toCompareValue.equals("0")){
                comparisons[2]++;
            }else {
                comparisons[4]++;
            }
        }else if(originalValue.equals("false")){
            if(toCompareValue.equals("true")|| toCompareValue.equals("1")){
                comparisons[3]++;
            }else if(toCompareValue.equals("false")|| toCompareValue.equals("0")){
                comparisons[1]++;
            }else {
                comparisons[4]++;
            }
        }else if(originalValue.equals("na")){
            comparisons[4]++;
        }
    }

    /**
     * <p>Method use to return the original excel file.</p>
     *
     * @return Original Excel file
     *
     */
    public File getExcelOriginal() {

        return excelOriginal;
    }

    /**
     * <p>Method use to return the excel file used to compare with the original.</p>
     *
     * @return To compare Excel file
     *
     */
    public File getExcelToCompare() {

        return excelToCompare;
    }

    /**
     * <p>Method used to get the true positive number parameter encapsulating the parameter of the class.</p>
     *
     * @return true positive number int.
     *
     */
    public int getTruePositiveNumber() {

        return comparisons[0];
    }

    /**
     * <p>Method used to get the true negative number parameter encapsulating the parameter of the class.</p>
     *
     * @return true negative number int.
     *
     */
    public int getTrueNegativeNumber() {

        return comparisons[1];
    }

    /**
     * <p>Method used to get the false positive number parameter encapsulating the parameter of the class.</p>
     *
     * @return false positive number int.
     *
     */
    public int getFalsePositiveNumber() {

        return comparisons[2];
    }

    /**
     * <p>Method used to get the false negative number parameter encapsulating the parameter of the class.</p>
     *
     * @return false negative number int.
     *
     */
    public int getFalseNegativeNumber() {

        return comparisons[3];
    }

    /**
     * <p>Method used to get the number of comparisons where at least one of the values in nonexistent or invalid.</p>
     *
     * @return false negative number int.
     *
     */
    public int getInvalidComparisonsNumber() {
        return comparisons[4];
    }


    /**
     * <p>Method used to get the lines of the original excel file as a ArrayList of string.</p>
     *
     * @return ArrayList of String of the lines of the original excel file
     *
     */
    private ArrayList<String> getOriginalLines(){

        return ExelReader.read(excelOriginal);
    }

    /**
     * <p>Method used to get the lines of the excel file to compare as a ArrayList of string.</p>
     *
     * @return ArrayList of String of the lines of the excel file to compare
     *
     */
    private ArrayList<String> getToCompareLines(){

        return ExelReader.read(excelToCompare);
    }

    public static void main(String[] args) {
        //File excelOriginal = new File("ES-2Sem-2021-Grupo24_metrics/ES-2Sem-2021-Grupo24_metrics.xlsx");
        //File excelToCompare = new File("Metrics_to_compare/ES-2Sem-2021-Grupo24_compare_metrics.xlsx");
        File excelOriginal = new File("Example_files/Code_Smells.xlsx");
        File excelToCompare = new File("Metrics_to_compare/jasml_0.10_metrics.xlsx");
        CodeSmellsComparator codeSmellsComparator = new CodeSmellsComparator(excelOriginal, excelToCompare);
    }



}