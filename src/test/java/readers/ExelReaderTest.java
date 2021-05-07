package readers;

import codeSmells.CodeSmellsComparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ExelReaderTest {

    File file = new File("src/test/java/resources/Code_Smells.xlsx");

    /**
     * Test method for {@link ExelReader#read(File)}.
     *  Only Checking the first 11 lines because excel file has a lot of lines.
     */
    @Test
    void readTest() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add("MethodID;package;class;method;NOM_class;LOC_class;WMC_class;is_God_Class;LOC_method;CYCLO_method;is_Long_Method");
        lines.add("1;com.jasml.compiler;GrammerException;GrammerException(int,String);4;18;4;false;3;1;false");
        lines.add("2;com.jasml.compiler;GrammerException;GrammerException(int,int,int,String);4;18;4;false;3;1;false");
        lines.add("3;com.jasml.compiler;GrammerException;GrammerException(String,Exception);4;18;4;false;3;1;false");
        lines.add("4;com.jasml.compiler;GrammerException;GrammerException(int,int,String);4;18;4;false;3;1;false");
        lines.add("5;com.jasml.compiler;ParsingException;ParsingException(int,int,int,String);6;50;13;false;6;1;false");
        lines.add("6;com.jasml.compiler;ParsingException;ParsingException(int,int,String);6;50;13;false;5;1;false");
        lines.add("7;com.jasml.compiler;ParsingException;ParsingException(int,String);6;50;13;false;4;1;false");
        lines.add("8;com.jasml.compiler;ParsingException;ParsingException(String,Exception);6;50;13;false;3;1;false");
        lines.add("9;com.jasml.compiler;ParsingException;ParsingException(String);6;50;13;false;3;1;false");
        lines.add("10;com.jasml.compiler;ParsingException;getMessage();6;50;13;false;21;8;false");
        assertEquals(lines,ExelReader.read(file).stream().limit(11).collect(Collectors.toList()));
    }
}