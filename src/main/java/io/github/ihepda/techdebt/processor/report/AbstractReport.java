
package io.github.ihepda.techdebt.processor.report;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import io.github.ihepda.techdebt.processor.TechDebtElement;

/**
 * Abstract class representing a report for technical debt. Implements the
 * TechDebtReport interface.
 */
public abstract class AbstractReport implements TechDebtReport {

	private boolean severityOrdered = false;

	/**
	 * Initializes the report with the given parameters.
	 * 
	 * @param parameters Properties containing initialization parameters.
	 */
	@Override
	public void init(Properties parameters) {
		if (parameters.containsKey(TechDebtReport.SEVERITY_ORDER_PARAMETER)) {
			severityOrdered = Boolean.parseBoolean(parameters.getProperty(TechDebtReport.SEVERITY_ORDER_PARAMETER));
		}
	}

	/**
	 * Returns a set of TechDebtElement objects ordered by severity if the
	 * severityOrdered flag is set.
	 * 
	 * @param elements Set of TechDebtElement objects to be ordered.
	 * @return Ordered set of TechDebtElement objects.
	 */
	protected Set<TechDebtElement> getOrderedSet(Set<TechDebtElement> elements) {
		if (severityOrdered) {
			TreeSet<TechDebtElement> ordered = new TreeSet<>((o1, o2) -> {
				int result = Integer.compare(o2.getSeverity().getPriority(), o1.getSeverity().getPriority());
				if (result != 0)
					return result;
				return o1.getFullName().compareTo(o2.getFullName());
			});
			ordered.addAll(elements);
			elements = ordered;
		}
		return elements;
	}

	/**
	 * Checks if the report is ordered by severity.
	 * 
	 * @return true if the report is ordered by severity, false otherwise.
	 */
	public boolean isSeverityOrdered() {
		return severityOrdered;
	}
}
