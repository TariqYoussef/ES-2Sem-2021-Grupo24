package metrics;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Name;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rules.Metric;
import rules.Rule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodMetricsTest {

	static MethodMetrics methodMetrics;
	static MethodDeclaration methodDeclaration;

	static MetricExtractor extractor;
	static ArrayList<Rule> rulesFalse = new ArrayList<>();
	static ArrayList<Rule> rulesTrue = new ArrayList<>();
	static Rule ruleChanged;
	static List<MethodMetrics> methods;

	@BeforeAll
	static void setUpBeforeClass() throws IOException {
		File testclass = new File("src/test/java/resources/QuickSort.java");
		extractor = new MetricExtractor();
		extractor.setSrcpath(testclass.toPath());
		methods = extractor.ExtractMetrics();
		//RUle é LOC_method > 10 AND CYCLO_METHOD >= 2 then is lONG_Method
		Rule rule1 = new Rule(
				Metric.LOC_method, Rule.Operation.BiggerThan, 10,
				Metric.CYCLO_method, Rule.Operation.BiggerThanEqual, 2,
				Rule.LogicOp.AND, Rule.Smell.Long_Method);

		//RUle é LOC_method == 10 OR CYCLO_METHOD != 0 then is lONG_Method
		Rule rule2 = new Rule(
				Metric.WMC_class, Rule.Operation.Equal, 10,
				Metric.CYCLO_method, Rule.Operation.Different, 0,
				Rule.LogicOp.OR, Rule.Smell.Long_Method);


		rulesFalse.add(rule1);
		rulesTrue.add(rule2);


		methodDeclaration = new MethodDeclaration();

		methodMetrics = new MethodMetrics(1,
				new PackageDeclaration(new Name("package")),
				new ClassOrInterfaceDeclaration(),
				methodDeclaration
				);

		methodMetrics.setCyclo_method(10);
		methodMetrics.setLoc_method(10);
		methodMetrics.setLoc_class(10);
		methodMetrics.setNom_class(10);
		methodMetrics.setWmc_class(10);

	}

	/**
	 * Test method for {@link MethodMetrics#verifyRuleset(List)}.
	 * @throws java.io.IOException
	 */
	@Test
	void verifyRulesetTrue() {
		assertEquals("True",methodMetrics.verifyRuleset(rulesTrue));
	}

	/**
	 * Test method for {@link MethodMetrics#verifyRuleset(List)}.
	 * @throws java.io.IOException
	 */
	@Test
	void verifyRulesetFalse() {
		assertEquals("False",methodMetrics.verifyRuleset(rulesFalse));
	}
	/**
	 * Test method for {@link MethodMetrics#verifyRuleset(List)}.
	 * @throws java.io.IOException
	 */
	@Test
	void verifyRulesetEmpty() {
		assertEquals("NA",methodMetrics.verifyRuleset(new ArrayList<Rule>()));
	}

	/**
	 * Test method for {@link MethodMetrics#getId()}.
	 * @throws java.io.IOException
	 */
	@Test
	void getId() {
		assertEquals(1,methodMetrics.getId());
	}

	/**
	 * Test method for {@link MethodMetrics#getMetricTest()}.
	 * @throws java.io.IOException
	 */
	@Test
	void getMetricTest() {


	}
}