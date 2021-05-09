package metrics;

import com.github.javaparser.ast.body.CallableDeclaration;
import rules.Metric;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import rules.Rule;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *<p>MethodMetrics is a class to encapsulate all the following variables relating </p>
 * <ul>
 *     <li>Methoid              </li>
 *     <li>packageOfMethod      </li>
 *     <li>classOfMethod        </li>
 *     <li>method               </li>
 *     <li>nom_class            </li>
 *     <li>loc_class            </li>
 *     <li>wmc_class            </li>
 *     <li>loc_method           </li>
 *     <li>cyclo_method         </li>
 * </ul>
 */
public class MethodMetrics {
    private final int id;
    private final PackageDeclaration packageOfMethod;
    private final ClassOrInterfaceDeclaration classOfMethod;
    private final CallableDeclaration method;

    private int nom_class;
    private int loc_class;
    private int wmc_class;

    private int loc_method;
    private int cyclo_method;

    /**
     * @param id int
     * @param packageOfMethod PackageDeclaration
     * @param classOfMethod ClassOrInterfaceDeclaration
     * @param method CallableDeclaration
     */
    public MethodMetrics(int id, PackageDeclaration packageOfMethod, ClassOrInterfaceDeclaration classOfMethod, CallableDeclaration method) {
        this.id = id;
        this.packageOfMethod = packageOfMethod;
        this.classOfMethod = classOfMethod;
        this.method = method;
    }

    /**
     * @return ClassOrInterfaceDeclaration
     */
    public ClassOrInterfaceDeclaration getClassOfMethod() {
        return classOfMethod;
    }
    /**
     * @return int id of the MethodMetric
     */
    public int getId() {
        return id;
    }

    /**
     * @return PackageDeclaration package of the MethodMetric
     */
    public PackageDeclaration getPackageOfMethod() {
        return packageOfMethod;
    }
    /**
     * @return CallableDeclaration method or Constructor of the MethodMetric
     */
    public CallableDeclaration getMethod() {
        return method;
    }

    /**
     * @param metric {@link Metric}
     * @param value int
     * @throws NoSuchElementException  when a non existant metric enum is input
     * @see Metric
     */
    public void setMetric(Metric metric, int value) throws NoSuchElementException{
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
     * @param metric {@link Metric}
     * @return int
     * @throws NoSuchElementException when a non existant metric enum is input
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
     * <p>Receives a {@link List} and checks if if is an empty list first (returning "NA"), then if the object fails any of the rules (returning "False").
     * If the object doesn't fail any rule, then "True" is returned.</p>
     *
     * @param rules {@link List} Set of rules to be verified
     * @return string
     *
     */
    public String verifyRuleset(List<Rule> rules){
        if(rules.size()==0){
            return "NA";
        }
        for(Rule rule: rules) {
            if (!rule.passesRule(this)){
                return "False";
            }
        }
        return "True";
    }

    /**
     * <p>Returns {@link int} corresponding to the nom_class method</p>
     * @return int
     *
     */
    public int getNom_class() {
        return nom_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the nom_class method</p>
     *
     * @param nom_class integer value of the metric
     */
    public void setNom_class(int nom_class) {
        this.nom_class = nom_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the loc_class method</p>
     * @return int
     *
     */
    public int getLoc_class() {
        return loc_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the loc_class method</p>
     *
     * @param loc_class integer value of the metric
     */
    public void setLoc_class(int loc_class) {
        this.loc_class = loc_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the wmc_class method</p>
     * @return int
     *
     */
    public int getWmc_class() {
        return wmc_class;
    }

    /**
     * <p>Sets {@link int} corresponding to the wmc_class method</p>
     * @param wmc_class integer value of the metric
     */
    public void setWmc_class(int wmc_class) {
        this.wmc_class = wmc_class;
    }

    /**
     * <p>Returns {@link int} corresponding to the loc_method method</p>
     * @return int
     *
     */
    public int getLoc_method() {
        return loc_method;
    }

    /**
     * <p>Sets {@link int} corresponding to the loc_method method</p>
     * @param loc_method integer value of the metric
     */
    public void setLoc_method(int loc_method) {
        this.loc_method = loc_method;
    }

    /**
     * <p>Returns {@link int} corresponding to the cyclo_method method</p>
     * @return int
     */
    public int getCyclo_method() {
        return cyclo_method;
    }

    /**
     * <p>Sets {@link int} corresponding to the cyclo_method method</p>
     * @param cyclo_method integer value of the metric
     */
    public void setCyclo_method(int cyclo_method) {
        this.cyclo_method = cyclo_method;
    }
}
