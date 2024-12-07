package io.github.ihepda.techdebt.filter;

import io.github.ihepda.techdebt.TechDebtElement;

public class FilterManager {


	private FilterOperation filterOperation;


	public FilterManager(String filterString) {
		if (filterString == null || filterString.isEmpty()) {
			this.filterOperation = new FilterOperation() {
				@Override
				public boolean execute(TechDebtElement techDebtResource) {
					return true;
				}

				@Override
				public void dump(StringBuilder sb) {
				}
			};
			return;
		}
		this.filterOperation = FilterParserVisitor.parse(filterString);
	}
	
	public boolean matches(TechDebtElement techDebtResource) {
		return this.filterOperation.execute(techDebtResource);
	}
	
}
