import readers.ExelReader;
import readers.JavaReader;

public class Main {

    public static void main(String[] args) {
        new JavaReader("/home/main/IdeaProjects/ES-2Sem-2021-Grupo24/src/main/java/Main.java").read();
        new ExelReader("/home/main/Downloads/testeES.xlsx").read();
    }

}

