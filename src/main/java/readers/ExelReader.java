package readers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

            for(Row row: sheet)
            {
                for(Cell cell: row)
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
