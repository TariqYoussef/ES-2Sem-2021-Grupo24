package codeSmells;

public class MetricComparissonStats {
	/**
	 * <p>The comparisons arrays store all the values resulting from the execution of the methods in this class. Their positions are:</p>
	 * <ul>
	 *     <li>[0] = truePositiveNumber</li>
	 *     <li>[1] = trueNegativeNumber</li>
	 *     <li>[2] = falsePositiveNumber</li>
	 *     <li>[3] = falseNegativeNumber</li>
	 *     <li>[4] = invalidComparisonsNumber</li>
	 * </ul>
	 */
	private int[] comparisons = {0, 0, 0, 0, 0};

	public int[] getComparisons() {
		return comparisons;
	}

	/**
	 * <p>Method used to get the true positive number parameter</p>
	 *
	 * @return true positive number int.
	 */
	public int getTruePositiveNumber() {

		return comparisons[0];
	}

	/**
	 * <p>Method used to get the true negative number parameter</p>
	 *
	 * @return true negative number int.
	 */
	public int getTrueNegativeNumber() {

		return comparisons[1];
	}

	/**
	 * <p>Method used to get the false positive number parameter</p>
	 *
	 * @return false positive number int.
	 */
	public int getFalsePositiveNumber() {

		return comparisons[2];
	}

	/**
	 * <p>Method used to get the false negative number parameter .</p>
	 *
	 * @return false negative number int.
	 */
	public int getFalseNegativeNumber() {

		return comparisons[3];
	}

	/**
	 * <p>Method used to get the number of comparisons where at least one of the values in nonexistent or invalid.</p>
	 *
	 * @return false negative number int.
	 */
	public int getInvalidComparisonsNumber() {
		return comparisons[4];
	}
}