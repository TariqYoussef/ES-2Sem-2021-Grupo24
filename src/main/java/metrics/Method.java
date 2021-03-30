package metrics;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Method {
    private int id;
    private ClassOrInterfaceDeclaration classOfMethod;
    private PackageDeclaration packageOfMethod;
    private MethodDeclaration method;


    private int nom_class;
    private int loc_class;
    private int wmc_class;

    private int loc_method;
    private int cyclo_method;

    public Method(int id, ClassOrInterfaceDeclaration classOfMethod, PackageDeclaration packageOfMethod, MethodDeclaration method) {
        this.id = id;
        this.classOfMethod = classOfMethod;
        this.packageOfMethod = packageOfMethod;
        this.method = method;
    }

    public int getNom_class() {
        return nom_class;
    }

    public void setNom_class(int nom_class) {
        this.nom_class = nom_class;
    }

    public int getLoc_class() {
        return loc_class;
    }

    public void setLoc_class(int loc_class) {
        this.loc_class = loc_class;
    }

    public int getWmc_class() {
        return wmc_class;
    }

    public void setWmc_class(int wmc_class) {
        this.wmc_class = wmc_class;
    }

    public int getLoc_method() {
        return loc_method;
    }

    public void setLoc_method(int loc_method) {
        this.loc_method = loc_method;
    }

    public int getCyclo_method() {
        return cyclo_method;
    }

    public void setCyclo_method(int cyclo_method) {
        this.cyclo_method = cyclo_method;
    }
}
