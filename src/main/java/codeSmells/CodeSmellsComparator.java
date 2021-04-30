package codeSmells;


import readers.ExelReader;
import util.Quadruple;

import java.io.File;
import java.util.*;

public class CodeSmellsComparator {
    private final File excelOriginal;
    private final File excelToCompare;

    public CodeSmellsComparator(File excelOriginal, File excelToCompare) {
        this.excelOriginal = excelOriginal;
        this.excelToCompare = excelToCompare;
    }

    //duas listas de Pair ou ent Boolean[2]
    //uma lista para God_Class outra pa Long_method
    //esquerda são do Original os da direita sao do Compare

    private ArrayList<String> getOriginalLines(){
        return ExelReader.read(excelOriginal);
    }
    private ArrayList<String> getToCompareLines(){
        return ExelReader.read(excelToCompare);
    }

    public ArrayList<Quadruple<String,String,String,String>> getValuesToCompare(){
        ArrayList<Quadruple<String,String,String,String>> values = new ArrayList<>();

        ArrayList<String> toCompareLines=  getToCompareLines();
        ArrayList<String> originalLines = getOriginalLines();
        toCompareLines.remove(0);
        originalLines.remove(0);

        //debugging purposes must be the name of methods in the project
        int i = 0;

        for(int originalLinePos = 0; originalLinePos < originalLines.size(); originalLinePos++){
            String originalLine = originalLines.get(originalLinePos);

            for(int toCompareLinePos = 0; toCompareLinePos < toCompareLines.size(); toCompareLinePos++){

                String toCompareLine = toCompareLines.get(toCompareLinePos);

                String[] originalSplit=originalLine.toLowerCase(Locale.ROOT).split(";");
                String[] toCompareSplit = toCompareLine.toLowerCase(Locale.ROOT).split(";");

                if(originalSplit[1].equals(toCompareSplit[1])
                        && originalSplit[2].equals(toCompareSplit[2])
                        && originalSplit[3].equals(toCompareSplit[3])){
                    System.out.println("Original: "+originalLine+"\ntoCompare: " + toCompareLine);

                    //For efficiency
                    toCompareLines.remove(toCompareLine);

                    Quadruple<String,String,String,String> valuesLine = new Quadruple<>(originalSplit[7], originalSplit[10],toCompareSplit[7],toCompareSplit[10]);

                    values.add(valuesLine);
                    System.out.println(valuesLine);
                    i++;
                    break;
                }
            }
        }

        System.out.println(i);
        return values;
    }

    public static void main(String[] args) {
        File excelOriginal = new File("ES-2Sem-2021-Grupo24_metrics/ES-2Sem-2021-Grupo24_metrics.xlsx");
        File excelToCompare = new File("metrics_to_compare/ES-2Sem-2021-Grupo24_compare_metrics.xlsx");
        CodeSmellsComparator codeSmellsComparator = new CodeSmellsComparator(excelOriginal, excelToCompare);
        codeSmellsComparator.getValuesToCompare();
    }

}