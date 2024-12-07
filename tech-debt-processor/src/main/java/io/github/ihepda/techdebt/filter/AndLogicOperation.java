package io.github.ihepda.techdebt.filter;

import io.github.ihepda.techdebt.TechDebtElement;

public class AndLogicOperation extends LogicOperation {

	@Override
	public boolean execute(TechDebtElement techDebt) {
		boolean execute = this.getLeft().execute(techDebt);
		if (!execute) {
			return false;
		}
		
		return this.getRight().execute(techDebt);
	}

	@Override
	protected String operation() {
		return "AND";
	}

}
