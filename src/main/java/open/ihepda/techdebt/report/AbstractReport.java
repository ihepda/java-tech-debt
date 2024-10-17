package open.ihepda.techdebt.report;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import open.ihepda.techdebt.TechDebtElement;

public abstract class AbstractReport implements TechDebtReport {

	private boolean severityOrdered = false;

	@Override
	public void init(Properties parameters) {
		if(parameters.containsKey(TechDebtReport.SEVERITY_ORDER_PARAMETER))
			severityOrdered = Boolean.parseBoolean(parameters.getProperty(TechDebtReport.SEVERITY_ORDER_PARAMETER));
	}

	protected Set<TechDebtElement> getOrderedSet(Set<TechDebtElement> elements) {
		if(severityOrdered) {
			TreeSet<TechDebtElement> ordered = new TreeSet<>((o1,o2) -> {
				int result = Integer.compare(o2.getSeverity().getPriority(),o1.getSeverity().getPriority());
				if(result != 0) return result;
				return o1.getFullName().compareTo(o2.getFullName());
			});
			ordered.addAll(elements);
			elements = ordered;
		}
		return elements;
	}

	public boolean isSeverityOrdered() {
		return severityOrdered;
	}
}
