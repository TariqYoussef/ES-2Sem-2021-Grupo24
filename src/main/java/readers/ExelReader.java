package readers;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExelReader {
    private File file;

    public ExelReader(String path){
        file= new File(path);
    }

    public void read(){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet=wb.getSheetAt(0);
            //evaluating cell type
            for(Row row: sheet)     //iteration over row using for each loop
            {
                for(Cell cell: row)    //iteration over cell using for each loop
                {
                    if(cell.getCellType() == CellType.NUMERIC){
                        System.out.println(cell.getNumericCellValue());
                    }
                    if(cell.getCellType() == CellType.STRING){
                        System.out.println(cell.getStringCellValue());
                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
