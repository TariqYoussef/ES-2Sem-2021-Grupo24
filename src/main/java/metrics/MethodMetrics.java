package metrics;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodMetrics {
    private int id;
    private PackageDeclaration packageOfMethod;
    private ClassOrInterfaceDeclaration classOfMethod;
    private MethodDeclaration method;


    private int nom_class;
    private int loc_class;
    private int wmc_class;

    private int loc_method;
    private int cyclo_method;

    public MethodMetrics(int id, PackageDeclaration packageOfMethod, ClassOrInterfaceDeclaration classOfMethod, MethodDeclaration method) {
        this.id = id;
        this.packageOfMethod = packageOfMethod;
        this.classOfMethod = classOfMethod;
        this.method = method;
    }

    public ClassOrInterfaceDeclaration getClassOfMethod() {
        return classOfMethod;
    }

    public int getId() {
        return id;
    }

    public PackageDeclaration getPackageOfMethod() {
        return packageOfMethod;
    }

    public MethodDeclaration getMethod() {
        return method;
    }

    public void setMetric(String name, int metric){
        switch(name)
        {
            case "nom_class":
                setNom_class(metric);
                break;
            case "loc_class":
                setLoc_class(metric);
                break;
            case "wmc_class":
                setWmc_class(metric);
                break;
            case "loc_method":
                setLoc_method(metric);
                break;
            case "cyclo_method":
                setCyclo_method(metric);
                break;
            default:
                System.out.println("no match");
        }
    }



    public int getNom_class() {
        return nom_class;
    }

    private void setNom_class(int nom_class) {
        this.nom_class = nom_class;
    }

    public int getLoc_class() {
        return loc_class;
    }

    private void setLoc_class(int loc_class) {
        this.loc_class = loc_class;
    }

    public int getWmc_class() {
        return wmc_class;
    }

    private void setWmc_class(int wmc_class) {
        this.wmc_class = wmc_class;
    }

    public int getLoc_method() {
        return loc_method;
    }

    private void setLoc_method(int loc_method) {
        this.loc_method = loc_method;
    }

    public int getCyclo_method() {
        return cyclo_method;
    }

    private void setCyclo_method(int cyclo_method) {
        this.cyclo_method = cyclo_method;
    }
}
