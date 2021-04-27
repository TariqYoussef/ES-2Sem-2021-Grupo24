package readers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class ExelReader {
    private File file;

    /**
     * @param path path to exel file
     */
    public ExelReader(String path){
        file= new File(path);
    }

    //TODO static
    /**
     * @return return the exel file as a list of string
     */
    public ArrayList<String> read(){
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet=wb.getSheetAt(0);

            for(Row row: sheet)
            {
                String line = "";
                for(Cell cell: row)
                {
                    if(cell.getCellType() == CellType.NUMERIC){
                        line += (int)cell.getNumericCellValue()+";";
                    }
                    if(cell.getCellType() == CellType.STRING){
                        line += cell.getStringCellValue()+";";
                    }
                }
                lines.add(line.substring(0,line.length()-1));
                System.out.println();
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
