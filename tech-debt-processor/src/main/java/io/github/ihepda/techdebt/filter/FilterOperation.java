package io.github.ihepda.techdebt.filter;

import io.github.ihepda.techdebt.TechDebtElement;

public interface FilterOperation {

	boolean execute(TechDebtElement techDebt);
	
	void dump(StringBuilder sb);
}
