package MetricExtraction;

import Util.Pair;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class MetricExtractor {

    public List<Pair<ClassOrInterfaceDeclaration, Integer>> NOM_class(Path filePath) throws IOException {
        List<Pair<ClassOrInterfaceDeclaration, Integer>>pairs = new LinkedList<>();

        //Block to create compilationUnits and store them in a list
        List<CompilationUnit> compilationUnits = new LinkedList<>();
        Files.find(filePath, Integer.MAX_VALUE,(path, attr)->attr.isRegularFile() && path.toString().endsWith(".java")).forEach(n-> {
            try {
                compilationUnits.add(StaticJavaParser.parse(n.toAbsolutePath()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Block to traverse the compilation Units and count all the methods in each class
        for (CompilationUnit cu:compilationUnits) {
            for (ClassOrInterfaceDeclaration cla: cu.findAll(ClassOrInterfaceDeclaration.class)) {
                Pair<ClassOrInterfaceDeclaration, Integer> p = new Pair<>(cla, cla.getMethods().size());
                pairs.add(p);
            }
        }

        //Print block
        for (Pair<ClassOrInterfaceDeclaration, Integer> pair: pairs){
            System.out.println("Number of methods in "+pair.getA().getNameAsString() +"="+ pair.getB());
            System.out.println("=================");
        }

        return pairs;
    }



}
