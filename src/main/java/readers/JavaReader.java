package readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JavaReader {
    private File file;

    public JavaReader(String path){
        file= new File(path);
    }

    public void read() {
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
