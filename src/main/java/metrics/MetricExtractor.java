package metrics;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import gui.GuiController;
import javafx.scene.control.Alert;
import jdk.nashorn.internal.codegen.CompilerConstants;
import rules.Metric;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.*;
import util.Quadruple;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetricExtractor {

    private Path srcpath;
//Constructors----------
    /**
     * Used for junit testing
     * */
    public MetricExtractor() {    }

    public MetricExtractor(Path filepath) throws IOException {

        Optional<Path> optionalPath = Files.find(filepath, Integer.MAX_VALUE, (path, attr) -> attr.isDirectory() && path.endsWith("src")).findFirst();

        if (optionalPath.isPresent()) {
            srcpath = optionalPath.get();
        } else {
            GuiController.showInformationMessage("Erro","Não existe um src folder", Alert.AlertType.ERROR);
        }

    }

//Getter-Setter----------
    public Path getSrcpath() {
        return srcpath;
    }

    public void setSrcpath(Path srcpath) {
        this.srcpath = srcpath;
    }

    /**
     * @return {@link List} of MethodMetrics extracted from the project
     * @throws IOException if there exists any problem with the file
     */
//main.Main execution method----------
    public List<MethodMetrics> ExtractMetrics() throws IOException {
        List<CompilationUnit> cuList = CreateCompilationUnits(srcpath);

        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> nom_class = NOM_class(cuList);
        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> cyclo_method = CYCLO_method(cuList);
        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> loc_method = LOC_method(cuList);
        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> loc_class = LOC_class(cuList);
        List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> wmc_class = WMC_class(cuList);

        List<MethodMetrics> methodMetrics = new ArrayList<>();

        joinMetrics(methodMetrics, nom_class,Metric.NOM_class);
        joinMetrics(methodMetrics, cyclo_method, Metric.CYCLO_method);
        joinMetrics(methodMetrics, loc_method, Metric.LOC_method);
        joinMetrics(methodMetrics, loc_class, Metric.LOC_class);
        joinMetrics(methodMetrics, wmc_class, Metric.WMC_class);

        return methodMetrics;

    }

    /**
     * @param methodMetrics {@link List} of the {@link Quadruple} made from {@link PackageDeclaration}, {@link ClassOrInterfaceDeclaration}, {@link CallableDeclaration} and {@link Integer}
     * @param metrics {@link List} of the {@link MethodMetrics}
     * @param metricEnum {@link Metric}
     */
    // joins the metrics for xlsx file
    public void joinMetrics(List<MethodMetrics> methodMetrics, List<Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> metrics, Metric metricEnum){
        int id=1;
        for(Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer> metric : metrics){
            boolean exists = joinMetricsAUX(methodMetrics, metricEnum, metric);
            if(!exists){
                MethodMetrics method = new MethodMetrics(id, metric.getA(), metric.getB(), metric.getC());
                method.setMetric(metricEnum,metric.getD());
                methodMetrics.add(method);
                id++;
            }
        }
    }

    private boolean joinMetricsAUX(List<MethodMetrics> methodMetrics, Metric metricEnum, Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer> metric) {
        boolean exists= false;
        for(MethodMetrics method:methodMetrics){
            // If the method already exists set the metric in its line
            if(method.getPackageOfMethod().equals(metric.getA()) && method.getClassOfMethod().equals(metric.getB())
            && method.getMethod().equals(metric.getC())){
                method.setMetric(metricEnum,metric.getD());
                exists= true;
                break;
            }
        }
        return exists;
    }

//main.Main execution method----------


    /**
     * @param dirPath {@link Path} of the project to analyze
     * @return {@link List} {@link CompilationUnit} created from analyzing the java project
     * @throws IOException if there is any problem with the file
     */
    /*
    Creates all compilation units required to extract all metrics
    metrics should use these compilation units as their arguments (List<CompilationUnit>)
    */
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


    /**
     * @return {@link List} {@link CompilationUnit} created from analyzing the java project
     * @throws IOException if there is any problem with the file
     */
    public List<CompilationUnit> CreateCompilationUnits() throws IOException {
        return CreateCompilationUnits(srcpath);
    }


    /**
     * <p>Converts a Optional Package into a Package object</p>
     * @param packageD {@link Optional} {@link PackageDeclaration}
     * @return {@link PackageDeclaration}
     */
    private static PackageDeclaration ConvertOptionalToActual(Optional<PackageDeclaration> packageD){
        PackageDeclaration pack ;
        return packageD.orElseGet(() -> new PackageDeclaration(new Name("none")));
    }

    /**
     * PreetyPrints an  Iterable<? extends Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer>>
     *
     * @param quadruples - Iterable<? extends Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer>>
     * @param metric {@link String}
     */
    private static void PrettyPrintQuad(Iterable<? extends Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> quadruples, String metric) {
        for (Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer> quadruple : quadruples) {
            PrettyPrintQuad(quadruple, metric);
        }
    }
    /**
     * PreetyPrints a specific Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer>
     *
     * example:
     *
     * @param quadruple - Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, MethodDeclaration, Integer>
     * @param metric
     */
    private static void PrettyPrintQuad(Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer> quadruple, final String metric) {
        System.out.println(
                "Package:"              + quadruple.getA().getNameAsString() +
                ";Class:"               + quadruple.getB().getNameAsString() +
                ";Method:"              + quadruple.getC().getNameAsString() +
                ";" + metric + ":"      + quadruple.getD());
        System.out.println("=================");
    }


    /**
     *  Extracts the Number of Methods inside each class, the number is stored in the D attribute of the returned Quadruple
     *
     * @param compilationUnits - The compilationUnits are going to be analyzed and scanned for each method in a class
     * @return a list of Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> which is
     * an object frequently used in the project, in order to associate the specific method with its corresponding extracted metric
     *
     * @see Quadruple
     */
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration, CallableDeclaration, Integer>> NOM_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> quadruples = new LinkedList<>();

        //Block to traverse the compilation Units and count all the methods in each class
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                int numberMethodInCLA = cla.getMethods().size()+cla.getConstructors().size();
                for (MethodDeclaration md : cla.getMethods()) {
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> q =
                            new Quadruple<>(
                                    ConvertOptionalToActual(cu.getPackageDeclaration()), cla, md, numberMethodInCLA);
                    quadruples.add(q);
               }
                for(ConstructorDeclaration cd : cla.getConstructors()){
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> q =
                            new Quadruple<>(
                                    ConvertOptionalToActual(cu.getPackageDeclaration()), cla, cd, numberMethodInCLA);
                    quadruples.add(q);
                }
            }
        }

        //PrettyPrintQuad(quadruples, "NOM_class");

        return quadruples;
    }

    /**
     * @param compilationUnits {@link CompilationUnit}
     * @return list of methods with their respective CYCLO_method count
     */
    //extract Cyclomatic complexity of each method
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> CYCLO_method(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> quads = new LinkedList<>();

        //Block to traverse the compilation Units and count all cycle methods in each method
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (MethodDeclaration md : cla.getMethods()) {
                    quads.add(calculateCycloMethodAUX(cu, cla, md));
                }
                for (ConstructorDeclaration cd : cla.getConstructors()) {
                    quads.add(calculateCycloMethodAUX(cu, cla, cd));
                }
            }
        }

        //PrettyPrintQuad(quads,"CYCLO_method");
        return quads;
    }

    private Quadruple<PackageDeclaration, ClassOrInterfaceDeclaration, CallableDeclaration, Integer> calculateCycloMethodAUX(CompilationUnit cu, ClassOrInterfaceDeclaration cla, CallableDeclaration cd) {
        int complexity = 0;
        for (IfStmt ifStmt : cd.getChildNodesByType(IfStmt.class)) {
            //if found
            complexity++;
            if (ifStmt.getElseStmt().isPresent()) {
                //has an else
                Statement elseStmt = ifStmt.getElseStmt().get();
                if (elseStmt instanceof IfStmt) {
                    //its an else-if. its already counted by counting the if above
                } else {
                    //its an else, so its added
                    complexity++;
                }
            }
        }
        complexity = getComplexity(cd, complexity, ForStmt.class);
        complexity = getComplexity(cd, complexity, ForEachStmt.class);
        complexity = getComplexity(cd, complexity, WhileStmt.class);
        complexity = getComplexity(cd, complexity, SwitchEntry.class);

        Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> quad =
                new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()), cla, cd, complexity);
        return quad;
    }

    private < T extends Node> int getComplexity(CallableDeclaration cd, int complexity, Class<T> nodeType) {
        for (T iterator : cd.findAll(nodeType)) {
            //for found
            complexity++;
        }
        return complexity;
    }

    /**
     * @param compilationUnits {@link CompilationUnit}
     * @return list of methods with their respective WMC_class count
     */
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> WMC_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> quads = new LinkedList<>();

        //Block to traverse the compilation Units and count all cycle methods in class
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                int classComplexity = getWMCClassComplexity(cla);
                for (MethodDeclaration md : cla.getMethods()) {
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> quad =
                            new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()),cla,md, classComplexity);
                    quads.add(quad);
                    //PrettyPrintQuad(quad,"WMC_class");
                }
            }
        }

        return quads;
    }

    private int getWMCClassComplexity(ClassOrInterfaceDeclaration cla) {
        int classComplexity = 0;
        for (MethodDeclaration md : cla.getMethods()) {
            classComplexity = calculateClassCOplecityAUX(classComplexity, md);

        }
        for (ConstructorDeclaration cd : cla.getConstructors()) {
            classComplexity = calculateClassCOplecityAUX(classComplexity, cd);

        }
        return classComplexity;
    }

    private int calculateClassCOplecityAUX(int classComplexity, CallableDeclaration md) {
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
        classComplexity = getComplexity(md, classComplexity, ForStmt.class);
        classComplexity = getComplexity(md, classComplexity, ForEachStmt.class);
        classComplexity = getComplexity(md, classComplexity, WhileStmt.class);
        classComplexity = getComplexity(md, classComplexity, SwitchEntry.class);
        return classComplexity;
    }

    /**
     * @param compilationUnits {@link CompilationUnit}
     * @return list of methods with their respective LOC_method count
     */
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> LOC_method(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> quadruples = new LinkedList<>();
        int lines = 0;
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                for (MethodDeclaration md : cla.getMethods()) {
                    lines = getMethodLines(md);
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> quad =
                            new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()), cla, md, lines);
                    quadruples.add(quad);
                    //PrettyPrintQuad(quad,"LOC_method");

                }
            }
        }
        return quadruples;
    }

    private int getMethodLines(MethodDeclaration md) {
        int lines;
        String body = md.getBody().toString();
        String comments = md.getAllContainedComments().toString();

        lines = (1+countLines(body)-countLines(comments));
        return lines;
    }

    /**
     *  <p>
     *      Method used to count the number of lines in a class, by adding the number of lines in each method with the number of class attributes
     *  </p>
     *
     * @param compilationUnits - The {@link CompilationUnit} are going to be analyzed and scanned for each method in a class
     * @return a list of Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,MethodDeclaration, Integer> which is
     * an object frequently used in the project, in order to associate the specific method with its corresponding extracted metric
     *
     * @see Quadruple
     */
    public List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> LOC_class(List<CompilationUnit> compilationUnits) {
        List<Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer>> quadruples = new LinkedList<>();
        for (CompilationUnit cu : compilationUnits) {
            for (ClassOrInterfaceDeclaration cla : cu.findAll(ClassOrInterfaceDeclaration.class)) {
                List<MethodDeclaration> methods = cla.getMethods();
                List<ConstructorDeclaration> constructors = cla.getConstructors();
                int classLength= countLines(cla.getFields().toString());

                classLength = countMethodsANDConstructors(methods, constructors, classLength);

                for(MethodDeclaration methodDeclaration: methods){
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> quad =
                            new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()), cla, methodDeclaration, classLength);
                    quadruples.add(quad);
                    //PrettyPrintQuad(quad,"LOC_class");
                }

                for(ConstructorDeclaration constructorDeclaration: constructors){
                    Quadruple<PackageDeclaration,ClassOrInterfaceDeclaration,CallableDeclaration, Integer> quad =
                            new Quadruple<>(ConvertOptionalToActual(cu.getPackageDeclaration()), cla, constructorDeclaration, classLength);
                    quadruples.add(quad);
                    //PrettyPrintQuad(quad,"LOC_class");
                }
            }
        }
        return quadruples;
    }

    private int countMethodsANDConstructors(List<MethodDeclaration> methods, List<ConstructorDeclaration> constructors, int classLength) {
        for (MethodDeclaration methodDeclaration : methods) {
            classLength = calculateLOC_CLASSAUX(classLength, methodDeclaration);
        }
        for (ConstructorDeclaration constructorDeclaration : constructors) {
            classLength = calculateLOC_CLASSAUX(classLength, constructorDeclaration);
        }
        return classLength;
    }

    private int calculateLOC_CLASSAUX(int classLength, CallableDeclaration declaration) {
        if(declaration.isConstructorDeclaration()){
            ConstructorDeclaration constructorDeclaration =declaration.asConstructorDeclaration();
            String body = constructorDeclaration.getBody().toString();
            String comments = constructorDeclaration.getAllContainedComments().toString();

            classLength += ((1 + countLines(body)) - countLines(comments));
            return classLength;
        }else if (declaration.isMethodDeclaration()){
            MethodDeclaration methodDeclarationd = declaration.asMethodDeclaration();
            String body = methodDeclarationd.getBody().toString();
            String comments = methodDeclarationd.getAllContainedComments().toString();

            classLength += ((1 + countLines(body)) - countLines(comments));
            return classLength;
        }else{
            return classLength;
        }
    }

    /**
     *
     * @param s {@link String}
     * @return
     */
    private static int countLines(String s){
        int n = 0;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '\n')
                n++;
        }
        return n;
    }

}
