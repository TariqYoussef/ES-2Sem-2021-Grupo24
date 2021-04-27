package codeSmells;

import rules.Metric;
import metrics.MethodMetrics;
import metrics.MetricExtractor;
import rules.Rule;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public class CodeSmellsCreator {

    private MetricExtractor metrics;
    private String name;

    private List<Rule> godRules;
    private List<Rule> longRules;

    /**
     * @param metrics MetricExtractor object that will be used to get the metrics
     * @param name name of the excel file
     * @param godRules //TODO david
     * @param longRules //TODO david
     */
    public CodeSmellsCreator(MetricExtractor metrics, String name, List<Rule> godRules, List<Rule> longRules){

        this.metrics = metrics;
        this.name = name + "_metrics.xlsx";
        this.godRules = godRules;
        this.longRules = longRules;
    }

    /**
     * This method creates an empty excel file in which will be written the code smells and metrics.
     * This method should only be executed whenever the file doesn't exist.
     * @param dir dir where the excel file will be placed
     * @throws IOException
     */
    public void createCodeSmellsXlsxFile(File dir) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(dir.getAbsolutePath()+"/"+name);
        fileOut.close();
    }

    /**
     * This method is used to add/replace the content of the code smells excel file.
     * @param dir dir where the excel file is placed
     * @throws IOException
     */
    public void addCodeSmellsToXlsx(File dir) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet= wb.createSheet();
        XSSFRow rowhead = sheet.createRow((short)0);

        createHeaderCodeSmellsXlsx(rowhead);

        List<MethodMetrics> methodMetrics = metrics.ExtractMetrics();

        int rowNumber = 1;
        for (MethodMetrics method: methodMetrics) {
            XSSFRow row = sheet.createRow((short)rowNumber);
            addRowCodeSmellsXlsx(row, method);
            rowNumber++;
        }

        FileOutputStream fileOut = new FileOutputStream(dir.getAbsolutePath()+"/"+name);
        wb.write(fileOut);
        wb.close();
        fileOut.close();

        System.out.println("Code smells escritos");

    }

    /**
     * This method is used to create the header of the table in the excel file.
     * @param rowHead object that specifies in which row of the excel file the header will be written
     */
    private void createHeaderCodeSmellsXlsx(XSSFRow rowHead){
        rowHead.createCell(0).setCellValue("MethodID");
        rowHead.createCell(1).setCellValue("package");
        rowHead.createCell(2).setCellValue("class");
        rowHead.createCell(3).setCellValue("method");
        rowHead.createCell(4).setCellValue("NOM_class");
        rowHead.createCell(5).setCellValue("LOC_class");
        rowHead.createCell(6).setCellValue("WMC_class");
        rowHead.createCell(7).setCellValue("is_God_Class");
        rowHead.createCell(8).setCellValue("LOC_method");
        rowHead.createCell(9).setCellValue("CYCLO_method");
        rowHead.createCell(10).setCellValue("is_Long_Method");
    }

    /**
     * This method is used to write in the excel file the metrics and code smells of a given method.
     * @param row object that specifies in which row of the excel file the line will be written
     * @param method object that will be used to get the values of the cells
     */
    private void addRowCodeSmellsXlsx(XSSFRow row, MethodMetrics method){
        row.createCell(0).setCellValue(method.getId());
        row.createCell(1).setCellValue(method.getPackageOfMethod().getNameAsString());
        row.createCell(2).setCellValue(method.getClassOfMethod().getNameAsString());
        row.createCell(3).setCellValue(method.getMethod().getDeclarationAsString(false,false,false));
        row.createCell(4).setCellValue(method.getMetric(Metric.NOM_class));//getNom_class());
        row.createCell(5).setCellValue(method.getMetric(Metric.LOC_class));//getLoc_class());
        row.createCell(6).setCellValue(method.getMetric(Metric.WMC_class));//getWmc_class());
        row.createCell(7).setCellValue(method.verifyRuleset(godRules));
        row.createCell(8).setCellValue(method.getMetric(Metric.LOC_method));//getLoc_method());
        row.createCell(9).setCellValue(method.getMetric(Metric.CYCLO_method));//getCyclo_method());
        row.createCell(10).setCellValue(method.verifyRuleset(longRules));
    }

    /**
     * @param rules
     * @param method
     * @return
     */
//    public String isLongMethod(List<Rule> rules, MethodMetrics method) {
//        //ArrayList<Rule> rules = null;
//        /*
//        try {
//            rules = Rule.deserializedRule();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        */
////        if(rules.size()==0){
////            return "NA";
////        }
//        Predicate<Rule> pr = (Rule r) -> (r.getSmell().equals(Rule.Smell.Long_Method));
//        rules.removeIf(pr.negate());
////
////        for(Rule r: rules) {
////            if (!r.passesRule(method)){
////                return "False";
////            }
////        }
////        return "True";
//        return method.verifyRuleset(rules);
//    }

//    public String isGodClass(List<Rule> rules, MethodMetrics method) {
//        Predicate<Rule> pr = (Rule r) -> (r.getSmell().equals(Rule.Smell.God_Class));
//        rules.removeIf(pr.negate());
//        return method.verifyRuleset(rules);
//    }

//    /**
//     * @param method
//     * @return
//     */
//    public String isGodClass(MethodMetrics method) {
//        ArrayList<Rule> rules = null;
//        try {
//            rules = Rule.deserializedRule();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        int count= 0;
//        for (Rule r : rules) {
//            if (r.getSmell().equals(Rule.Smell.God_Class)){
//                if (!r.passesRule(method)) {
//                    return "False";
//                }else{
//                    count++;
//                }
//            }
//        }
//        if(count >0){
//            return "True";
//        }else{
//            return " - - - ";
//        }
//    }

}
