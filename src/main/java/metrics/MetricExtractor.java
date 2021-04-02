package metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.*;
import util.Pair;
import util.Quadruple;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MetricExtractor {
    Path srcpath;



    public MetricExtractor(Path filepath) throws IOException {

        Optional<Path> optionalPath = Files.find(filepath, Integer.MAX_VALUE, (path, attr) -> attr.isDirectory() && path.endsWith("src")).findFirst();

        if (optionalPath.isPresent()) {
            srcpath = optionalPath.get();
        } else {
            throw new IOException("src folder not found!!");
        }

    }
    /**
     * Used for junit testing
     * */
    public MetricExtractor() {}

    public static List<CompilationUnit> CreateCompilationUnits(Path dirPath) throws IOException {
        List<CompilationUnit> compilationUnits = new LinkedList<>();
        Files.find(dirPath, Integer.MAX_VALUE, (path, attr) -> attr.isRegularFile() && path.toString().endsWith(".java")).forEach(n -> {
            try {
                compilationUnits.add(StaticJavaParser.parse(n.toAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return compilationUnits;
    }

    public Path getSrcpath() {
        return srcpath;
    }

    public void setSrcpath(Path srcpath) {
        this.srcpath = srcpath;
    }

    //TODO Decide when/where to handle excepttion
    public void ExtractMetrics() throws IOException {
        List<CompilationUnit> cuList = CreateCompilationUnits(srcpath);

        //TODO Compile all metric calls here and merge the outputs into one to be written in the xlsx file
        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer>> nom_class = NOM_class(cuList);
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> cyclo_method = CYCLO_method(cuList);
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> loc_method = LOC_method(cuList);
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> loc_class = LOC_method(cuList);
    }

    /*
    Creates all compilation units required to extract all metrics
    metrics should use these compilation units as their arguments (List<CompilationUnit>)
    */
    public List<CompilationUnit> CreateCompilationUnits() throws IOException {
        return CreateCompilationUnits(srcpath);
    }


    private PackageDeclaration ConvertOptionalToActual(Optional<PackageDeclaration> packageD){
        PackageDeclaration pack ;
        if(packageD.isPresent()){
            return packageD.get();
        }else{
           return new PackageDeclaration(new Name("none"));
        }
    }

    //Extract Number of Methods each class
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> NOM_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> quadruples = new LinkedList<>();

        //Block to traverse the compilation Units and count all the methods in each class
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (MethodDeclaration md : cla.getMethods()) {
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> q =
                            new Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>(
                                    ConvertOptionalToActual(cu.getPackageDeclaration()),
                                    cla,
                                    md,
                                    cla.getMethods().size());
                    quadruples.add(q);
               }
            }
        }

        //Print block
        for (Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> quadruple : quadruples) {
            System.out.println("Package:"+quadruple.getA().getNameAsString()+";Class:"+quadruple.getB().getNameAsString()+";Method:"+quadruple.getC().getNameAsString()+";NOM_class:"+quadruple.getD());
            System.out.println("=================");
        }

        return quadruples;
    }

    //TODO change Pair<MethodDeclaration, Integer> to Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>
    //extract Cyclomatic complexity of each method
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> CYCLO_method(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> quads = new LinkedList<>();

        //Block to traverse the compilation Units and count all cycle methods in each method
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (MethodDeclaration md : cla.getMethods()) {
                    int complexity = 0;
                    for (IfStmt ifStmt : md.getChildNodesByType(IfStmt.class)) {
                        //if found
                        complexity++;
                        //System.out.println(ifStmt);
                        if (ifStmt.getElseStmt().isPresent()) {
                            //has an else
                            Statement elseStmt = ifStmt.getElseStmt().get();
                            if (elseStmt instanceof IfStmt) {
                                //its an else-if. its already counted by counting the if above
                            } else {
                                //its an else, so its added
                                complexity++;
                                //System.out.println(elseStmt);
                            }
                        }
                    }
                    for (ForStmt forStmt : md.findAll(ForStmt.class)) {
                        //for found
                        complexity++;
                        //System.out.println(forStmt);
                    }
                    for (ForEachStmt forEachStmt : md.findAll(ForEachStmt.class)) {
                        //for each found
                        complexity++;
                        //System.out.println(forEachStmt);
                    }
                    for (WhileStmt whileStmt : md.findAll(WhileStmt.class)) {
                        //while found
                        complexity++;
                        //System.out.println(whileStmt);
                    }
                    for (SwitchEntry switchEntry : md.findAll(SwitchEntry.class)) {
                        //switch case found
                        complexity++;
                        //System.out.println(switchEntry);
                    }
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> p = new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()),cla,md, complexity);
                    quads.add(p);
                }
            }
        }

        //Print block
        for (Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> quad : quads) {
            System.out.println("Package:"+quad.getA().getNameAsString()+";Class:"+quad.getB().getNameAsString()+";Method:"+quad.getC().getNameAsString()+";CYCLO_method:"+quad.getD());
            System.out.println("=================");
        }

        return quads;
    }

    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> WMC_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> quads = new LinkedList<>();

        //Block to traverse the compilation Units and count all cycle methods in each method
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                int classComplexity = 0;
                for (MethodDeclaration md : cla.getMethods()) {
                    for (IfStmt ifStmt : md.getChildNodesByType(IfStmt.class)) {
                        //if found
                        classComplexity++;
                        if (ifStmt.getElseStmt().isPresent()) {
                            //has an else
                            Statement elseStmt = ifStmt.getElseStmt().get();
                            if (elseStmt instanceof IfStmt) {
                                //its an else-if. its already counted by counting the if above
                            } else {
                                //its an else, so its added
                                classComplexity++;
                            }
                        }
                    }
                    for (ForStmt forStmt : md.findAll(ForStmt.class)) {
                        //for found
                        classComplexity++;
                    }
                    for (ForEachStmt forEachStmt : md.findAll(ForEachStmt.class)) {
                        //for each found
                        classComplexity++;
                    }
                    for (WhileStmt whileStmt : md.findAll(WhileStmt.class)) {
                        //while found
                        classComplexity++;
                    }
                    for (SwitchEntry switchEntry : md.findAll(SwitchEntry.class)) {
                        //switch case found
                        classComplexity++;
                    }

                }
                for (MethodDeclaration md : cla.getMethods()) {
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> p = new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()),cla,md, classComplexity);
                    quads.add(p);
                }
            }
        }

        //Print block
        for (Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> quad : quads) {
            System.out.println("Package:"+quad.getA().getNameAsString()+";Class:"+quad.getB().getNameAsString()+";Method:"+quad.getC().getNameAsString()+";WMC_class:"+quad.getD());
            System.out.println("=================");
        }

        return quads;
    }

    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> LOC_method(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> quadruples = new LinkedList<>();
        int lines = 0;
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (MethodDeclaration md : cla.getMethods()) {
                    String body = md.getBody().toString();
                    String comments = md.getAllContainedComments().toString();
//                    System.out.println(code);
//                    System.out.println(comments);
                    lines = (1+countLines(body)-countLines(comments));
                    quadruples.add(new Quadruple(cu.getPackageDeclaration(), cla, md, lines));
                    System.out.println("Lines of "+md.getNameAsString()+" in "+cla.getNameAsString()+": "+lines);
                    System.out.println("=================");
                }
            }
        }



        return quadruples;
    }

    private int countLines(String s){
        int n = 0;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '\n')
                n++;
        }
        return n;
    }


    //LOC_Class
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> LOC_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> quadruples = new LinkedList<>();
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                int classLength = cla.getRange().map(range -> range.end.line - range.begin.line).orElse(0);

                for (MethodDeclaration md : cla.getMethods()) {
                    quadruples.add(new Quadruple(cu.getPackageDeclaration(), cla, md, classLength));
                }

                System.out.println("Lines in "+cla.getNameAsString()+": "+classLength);
                System.out.println("=================");
            }
        }
        return quadruples;
    }

    /*

        //TODO preencham com todos os metodos da classe especifica para o excel poder criar uma linha do tipo:
        methodid ; package; class; method; NOM_class; LOC_class; WMC_class; is_God_Class; LOC_method; CYCLO_method; is_long_method
            ou seja, exemplo uma metrica que corresponda a uma classe, metam o resultado da metrica para todos os metodos da classe
            tipo classe A tem 100 LOC então para cada metodo existente nessa classe criem um Quadruple para cada metodo, todos com o 100 no fim
            espero que percebam o que eu quero dizer, qualquer coisa perguntem

        //TODO
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> WMC_class(List<CompilationUnit> compilationUnits) {
    }

        //TODO
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer>> LOC_method(List<CompilationUnit> compilationUnits) {
    }

    */

}
