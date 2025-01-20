package io.github.ihepda.techdebt.maven.plugin.filter;

import io.github.ihepda.techdebt.utils.InternalLogger;

public class FilterManager {


	private FilterOperation filterOperation;
	private InternalLogger logger;

	public FilterManager(String filterString, InternalLogger logger) {
		this.logger = logger;
		if (filterString == null || filterString.isEmpty()) {
			this.filterOperation = new FilterOperation() {
				@Override
				public boolean execute(CounterByType counterByType) {
					return true;
				}

				@Override
				public void dump(StringBuilder sb) {
					sb.append("No filter applied");
				}
			};
		} else {
			this.filterOperation = FilterParserVisitorImpl.parse(filterString);
		}
		StringBuilder sb = new StringBuilder();
		this.filterOperation.dump(sb);
		this.logger.info("Apply report filter: {}", sb);
	}
	
	public boolean matches(CounterByType techDebtResource) {
		boolean execute = this.filterOperation.execute(techDebtResource);
		this.logger.debug("TechDebtElement match result {} : {}", execute, techDebtResource);
		return execute;
	}
	
}
