
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
public class CodeSmells {

    private MetricExtractor metrics;
    private String name;

    private List<Rule> godRules;
    private List<Rule> longRules;

    /**
     * @param metrics
     * @param name
     */
    public CodeSmells(MetricExtractor metrics, String name, List<Rule> godRules, List<Rule> longRules){

        this.metrics = metrics;
        this.name = name + "_metrics.xlsx";
        this.godRules = godRules;
        this.longRules = longRules;
    }

    /**
     * @param dir
     * @throws IOException
     */
    public void createCodeSmellsXlsxFile(File dir) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(dir.getAbsolutePath()+"/"+name);
        fileOut.close();
    }

    /**
     * @param dir
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
     * @param rowhead
     */
    private void createHeaderCodeSmellsXlsx(XSSFRow rowhead){
        rowhead.createCell(0).setCellValue("MethodID");
        rowhead.createCell(1).setCellValue("package");
        rowhead.createCell(2).setCellValue("class");
        rowhead.createCell(3).setCellValue("method");
        rowhead.createCell(4).setCellValue("NOM_class");
        rowhead.createCell(5).setCellValue("LOC_class");
        rowhead.createCell(6).setCellValue("WMC_class");
        rowhead.createCell(7).setCellValue("is_God_Class");
        rowhead.createCell(8).setCellValue("LOC_method");
        rowhead.createCell(9).setCellValue("CYCLO_method");
        rowhead.createCell(10).setCellValue("is_Long_Method");
    }

    /**
     * @param row
     * @param method
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
