package rules;

/**
 *<p>A Metric represents a statistic value used to evaluate the quality of source code  </p>
 *<p>Metric is an Enum containg the following values: </p>
 * <ul>
 *     <li>NOM_class - Number of methodds inside a Class </li>
 *     <li>LOC_class - Lines of code in a Class</li>
 *     <li>WMC_class - </li>
 *
 *     <li>LOC_method - Lines of code in a Method </li>
 *     <li>CYCLO_method - Cyclomatic complexity in a Method </li>
 * </ul>
 */
public enum Metric {

    NOM_class,
    LOC_class,
    WMC_class,

    LOC_method,
    CYCLO_method,
}
