package io.github.ihepda.techdebt.processor.report;

import java.util.Properties;
import java.util.Set;

import io.github.ihepda.techdebt.processor.TechDebtElement;

/**
 * Interface representing a report for technical debt.
 */

public interface TechDebtReport {
	
	String REPORT_IMPLEMENTATION = "techdebt.report.class";
	String SEVERITY_ORDER_PARAMETER = "techdebt.report.severity.order";

	String OUTPUT_DIRECTORY_PARAMETER = "techdebt.report.output.dir";
	String OUTPUT_NAME_PARAMETER = "techdebt.report.output.name";

	/**
     * Initializes the report with the given parameters.
     * 
     * @param parameters Properties containing initialization parameters
     */
	void init(Properties parameters);
	
	/**
     * Reports the given set of TechDebtElement objects.
     * 
     * @param elements Set of TechDebtElement objects to be reported
     * @return true if the report was successful, false otherwise
     */
	boolean report(Set<TechDebtElement> elements);
}
