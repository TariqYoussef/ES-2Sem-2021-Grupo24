package metrics;

import util.Pair;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MetricExtractor {
    Path srcpath;

    public MetricExtractor(Path filepath) throws IOException {

        Optional<Path> optionalPath = Files.find(filepath,Integer.MAX_VALUE,(path, attr)->attr.isDirectory() && path.endsWith("src") ).findFirst();

        if(optionalPath.isPresent()){
            srcpath = optionalPath.get();
        }else{
            throw new IOException("src folder not found!!");
        }

    }

     //TODO Decide when/where to handle excepttion
    public void ExtractMetrics() throws IOException {
        List<CompilationUnit> cuList = CreateCompilationUnits();

        //TODO Compile all metric calls here and merge the outputs into one to be written in the xlsx file
        List<Pair<ClassOrInterfaceDeclaration, Integer>> nom_class = NOM_class(cuList);
        List<Pair<MethodDeclaration, Integer>> cyclo_method = CYCLO_method(cuList);
    }
    /*
    Creates all compilation units required to extract all metrics
    metrics should use these compilation units as their arguments (List<CompilationUnit>)
    */
    private List<CompilationUnit> CreateCompilationUnits() throws IOException {
        List<CompilationUnit> compilationUnits = new LinkedList<>();
        Files.find(srcpath, Integer.MAX_VALUE,(path, attr)->attr.isRegularFile() && path.toString().endsWith(".java")).forEach(n-> {
            try {
                compilationUnits.add(StaticJavaParser.parse(n.toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return compilationUnits;
    }
    //Extract Number of Methods each class
    private List<Pair<ClassOrInterfaceDeclaration, Integer>> NOM_class(List<CompilationUnit> compilationUnits){
        List<Pair<ClassOrInterfaceDeclaration, Integer>>pairs = new LinkedList<>();

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

    //extract Cyclomatic complexity of each method
    private List<Pair<MethodDeclaration, Integer>> CYCLO_method(List<CompilationUnit> compilationUnits) {
        List<Pair<MethodDeclaration, Integer>>pairs = new LinkedList<>();

        //Block to traverse the compilation Units and count all cycle methods in each method
        for (CompilationUnit cu:compilationUnits) {
            for (ClassOrInterfaceDeclaration cla: cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for(MethodDeclaration md:cla.getMethods()) {
                    int complexity=0;
                    for(IfStmt ifStmt: md.getChildNodesByType(IfStmt.class)) {
                        //if found
                        complexity++;
                        //System.out.println(ifStmt);
                        if(ifStmt.getElseStmt().isPresent()) {
                            //has an else
                            Statement elseStmt = ifStmt.getElseStmt().get();
                            if(elseStmt  instanceof IfStmt) {
                                //its an else-if. its already counted by counting the if above
                            } else {
                                //its an else, so its added
                                complexity++;
                                //System.out.println(elseStmt);
                            }
                        }
                    }
                    for(ForStmt forStmt: md.getChildNodesByType(ForStmt.class)) {
                        //for found
                        complexity++;
                        //System.out.println(forStmt);
                    }
                    for(ForEachStmt forEachStmt: md.getChildNodesByType(ForEachStmt.class)) {
                        //for each found
                        complexity++;
                        //System.out.println(forEachStmt);
                    }
                    for(WhileStmt whileStmt: md.getChildNodesByType(WhileStmt.class)) {
                        //while found
                        complexity++;
                        //System.out.println(whileStmt);
                    }
                    for(SwitchEntry switchEntry: md.getChildNodesByType(SwitchEntry.class)) {
                        //switch case found
                        complexity++;
                        //System.out.println(switchEntry);
                    }
                    Pair<MethodDeclaration, Integer> p = new Pair<>(md, complexity);
                    pairs.add(p);
                }
            }
        }

        //Print block
        for (Pair<MethodDeclaration, Integer> pair: pairs){
            System.out.println("Number of cycles in "+pair.getA().getNameAsString() +"="+ pair.getB());
            System.out.println("=================");
        }

        return pairs;
    }

}
