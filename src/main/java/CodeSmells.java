
import rules.Metric;
import javafx.scene.control.Alert;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CodeSmells {

    private MetricExtractor metrics;
    private String name;

    public CodeSmells(MetricExtractor metrics, String name){

        this.metrics = metrics;
        this.name = name + "_metrics.xlsx";

    }

    public void createCodeSmellsXlsxFile(File dir) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(dir.getAbsolutePath()+"/"+name);
        fileOut.close();
    }

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

    private void addRowCodeSmellsXlsx(XSSFRow row, MethodMetrics method){
        row.createCell(0).setCellValue(method.getId());
        row.createCell(1).setCellValue(method.getPackageOfMethod().getNameAsString());
        row.createCell(2).setCellValue(method.getClassOfMethod().getNameAsString());
        row.createCell(3).setCellValue(method.getMethod().getNameAsString());
        row.createCell(4).setCellValue(method.getMetric(Metric.NOM_class));//getNom_class());
        row.createCell(5).setCellValue(method.getMetric(Metric.LOC_class));//getLoc_class());
        row.createCell(6).setCellValue(method.getMetric(Metric.WMC_class));//getWmc_class());
        row.createCell(7).setCellValue(isGodClass(method));
        row.createCell(8).setCellValue(method.getMetric(Metric.LOC_method));//getLoc_method());
        row.createCell(9).setCellValue(method.getMetric(Metric.CYCLO_method));//getCyclo_method());
        row.createCell(10).setCellValue(isLongMethod(method));
    }

    public String isLongMethod(MethodMetrics method) {
        ArrayList<Rule> rules = Rule.DeserializedRule();

        if(rules.size()==0){
            return "NA";
        }

        Predicate<Rule> pr = (Rule r) -> (r.getSmell().equals(Rule.Smell.Long_Method));
        rules.removeIf(pr.negate());

        for(Rule r: rules) {
            if (!r.passesRule(method)){
                return "False";
            }
        }
        return "True";
    }

    public String isGodClass(MethodMetrics method) {
        ArrayList<Rule> rules = Rule.DeserializedRule();
        int count= 0;
        for (Rule r : rules) {
            if (r.getSmell().equals(Rule.Smell.God_Class)){
                if (!r.passesRule(method)) {
                    return "False";
                }else{
                    count++;
                }
            }
        }
        if(count >0){
            return "True";
        }else{
            return " - - - ";
        }
    }

}
