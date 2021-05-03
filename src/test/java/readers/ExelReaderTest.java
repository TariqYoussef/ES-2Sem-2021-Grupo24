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
        lines.add("1;com.jasml.compiler;GrammerException;GrammerException(int,String);4;18;4;3;1");
        lines.add("2;com.jasml.compiler;GrammerException;GrammerException(int,int,int,String);4;18;4;3;1");
        lines.add("3;com.jasml.compiler;GrammerException;GrammerException(String,Exception);4;18;4;3;1");
        lines.add("4;com.jasml.compiler;GrammerException;GrammerException(int,int,String);4;18;4;3;1");
        lines.add("5;com.jasml.compiler;ParsingException;ParsingException(int,int,int,String);6;50;13;6;1");
        lines.add("6;com.jasml.compiler;ParsingException;ParsingException(int,int,String);6;50;13;5;1");
        lines.add("7;com.jasml.compiler;ParsingException;ParsingException(int,String);6;50;13;4;1");
        lines.add("8;com.jasml.compiler;ParsingException;ParsingException(String,Exception);6;50;13;3;1");
        lines.add("9;com.jasml.compiler;ParsingException;ParsingException(String);6;50;13;3;1");
        lines.add("10;com.jasml.compiler;ParsingException;getMessage();6;50;13;21;8");
        assertEquals(lines,ExelReader.read(file).stream().limit(11).collect(Collectors.toList()));
    }
}