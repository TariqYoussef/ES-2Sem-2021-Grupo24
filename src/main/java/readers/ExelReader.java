package readers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ExelReader {

    /**
     * @return return the exel file as a list of string
     * @param file {@link File} file to read from
     */
    public static ArrayList<String> read(File file){
        ArrayList<String> lines = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(file)){

            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet=wb.getSheetAt(0);


            FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
            for(Row row: sheet)
            {
                String line = "";
                for(int i=0;i<row.getLastCellNum();i++)
                {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    CellType cellType = cell.getCellType();
                    if (cell == null) {
                        line += ";";
                    } else if (cellType == CellType.NUMERIC) {
                        line += (int) cell.getNumericCellValue() + ";";
                    } else if (cellType == CellType.STRING) {
                        line += cell.getStringCellValue() + ";";
                    } else if (cellType == CellType.BOOLEAN) {
                        line += cell.getBooleanCellValue() + ";";
                    } else if (cellType == CellType.FORMULA) {
                        line += evaluator.evaluate(cell).getBooleanValue() + ";";
                    } else if (cellType == CellType.BLANK) {
                        line += "0;";
                    } else {
                        line += /*cell.getCellType() + */ ";";
                    }
                }
                lines.add(line.substring(0,line.length()-1));
            }
            wb.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  lines;
    }

}
