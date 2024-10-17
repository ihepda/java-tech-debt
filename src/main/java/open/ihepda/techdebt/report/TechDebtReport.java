package open.ihepda.techdebt.report;

import java.util.Properties;
import java.util.Set;

import open.ihepda.techdebt.TechDebtElement;

public interface TechDebtReport {
	
	String REPORT_IMPLEMENTATION = "techdebt.report.class";
	String SEVERITY_ORDER_PARAMETER = "techdebt.report.severity.order";

	String OUTPUT_DIRECTORY_PARAMETER = "techdebt.report.output.dir";
	String OUTPUT_NAME_PARAMETER = "techdebt.report.output.name";
	void init(Properties parameters);
	
	boolean report(Set<TechDebtElement> elements);
}
