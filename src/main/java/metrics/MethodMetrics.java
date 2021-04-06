package metrics;

import rules.Metric;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.NoSuchElementException;

public class MethodMetrics {
    private final int id;
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

    public void setMetric(Metric metric, int value)throws NoSuchElementException{
        switch(metric)
        {
            case NOM_class:
                setNom_class(value);
                break;
            case LOC_class:
                setLoc_class(value);
                break;
            case WMC_class:
                setWmc_class(value);
                break;
            case LOC_method:
                setLoc_method(value);
                break;
            case CYCLO_method:
                setCyclo_method(value);
                break;
            default:
                throw new NoSuchElementException("Non existant metric enum");
        }
    }

    public int getMetric(Metric metric) throws NoSuchElementException{
        switch(metric)
        {
            case NOM_class:
                return getNom_class();
            case LOC_class:
                return getLoc_class();
            case WMC_class:
                return getWmc_class();
            case LOC_method:
                return getLoc_method();
            case CYCLO_method:
                return getCyclo_method();
            default:
                throw new NoSuchElementException("Non existant metric enum");
        }
    }



    private int getNom_class() {
        return nom_class;
    }

    private void setNom_class(int nom_class) {
        this.nom_class = nom_class;
    }

    private int getLoc_class() {
        return loc_class;
    }

    private void setLoc_class(int loc_class) {
        this.loc_class = loc_class;
    }

    private int getWmc_class() {
        return wmc_class;
    }

    private void setWmc_class(int wmc_class) {
        this.wmc_class = wmc_class;
    }

    private int getLoc_method() {
        return loc_method;
    }

    private void setLoc_method(int loc_method) {
        this.loc_method = loc_method;
    }

    private int getCyclo_method() {
        return cyclo_method;
    }

    private void setCyclo_method(int cyclo_method) {
        this.cyclo_method = cyclo_method;
    }
}
