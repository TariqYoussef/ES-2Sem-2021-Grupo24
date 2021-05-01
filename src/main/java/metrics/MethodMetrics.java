package metrics;

import rules.Metric;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import rules.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 */
public class MethodMetrics {
    private final int id;
    private PackageDeclaration packageOfMethod;
    private ClassOrInterfaceDeclaration classOfMethod;
    private MethodDeclaration method;

/*
 TODO maybe alterar comportamento das metricas para usarem herança, haver uma class abstrata Metric que estas implementavam
        MethodMetrics armazenaria uma lista de Metrics que dps poderia ser iterada
        problema: como temos a certeza que cada objeto tem todas as metyricas calculadas?
*/
    private int nom_class;
    private int loc_class;
    private int wmc_class;

    private int loc_method;
    private int cyclo_method;

    /**
     * @param id
     * @param packageOfMethod
     * @param classOfMethod
     * @param method
     */
    public MethodMetrics(int id, PackageDeclaration packageOfMethod, ClassOrInterfaceDeclaration classOfMethod, MethodDeclaration method) {
        this.id = id;
        this.packageOfMethod = packageOfMethod;
        this.classOfMethod = classOfMethod;
        this.method = method;
    }

    /**
     * @return
     */
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

    /**
     * @param metric
     * @param value
     * @throws NoSuchElementException
     */
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

    /**
     * @param metric
     * @return
     * @throws NoSuchElementException
     */
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

    /**
     * <p>Recieves a {@link List<Rule>} and checks if if is an empty list first (returning "NA"), then if the object fails any of the rules (returning "False").
     * If the object doesn't fail any rule, then "True" is returned.</p>
     *
     * @param rules {@link List<Rule>} Set of rules to be verified
     * @return string
     *
     */
    public String verifyRuleset(List<Rule> rules){
        if(rules.size()==0){
            return "NA";
        }
        for(Rule r: rules) {
            if (!r.passesRule(this)){
                return "False";
            }
        }
        return "True";
    }

    /**
     * <p>Returns {@link int} corresponding to the nom_class method</p>
     *
     * @return int
     *
     */
    public int getNom_class() {
        return nom_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the nom_class method</p>
     *
     */
    public void setNom_class(int nom_class) {
        this.nom_class = nom_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the loc_class method</p>
     *
     * @return int
     *
     */
    public int getLoc_class() {
        return loc_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the loc_class method</p>
     *
     */
    public void setLoc_class(int loc_class) {
        this.loc_class = loc_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the wmc_class method</p>
     *
     * @return int
     *
     */
    public int getWmc_class() {
        return wmc_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the wmc_class method</p>
     *
     */
    public void setWmc_class(int wmc_class) {
        this.wmc_class = wmc_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the loc_method method</p>
     *
     * @return int
     *
     */
    public int getLoc_method() {
        return loc_method;
    }

    /**
     * <p>Sets {@link int} corresponding to the loc_method method</p>
     *
     */
    public void setLoc_method(int loc_method) {
        this.loc_method = loc_method;
    }

    /**
     * <p>Returns {@link int} corresponding to the cyclo_method method</p>
     *
     * @return int
     *
     */
    public int getCyclo_method() {
        return cyclo_method;
    }

    /**
     * <p>Sets {@link int} corresponding to the cyclo_method method</p>
     *
     */
    public void setCyclo_method(int cyclo_method) {
        this.cyclo_method = cyclo_method;
    }
}
