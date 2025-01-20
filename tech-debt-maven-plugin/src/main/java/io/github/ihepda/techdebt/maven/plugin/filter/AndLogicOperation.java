package io.github.ihepda.techdebt.maven.plugin.filter;

public class AndLogicOperation extends LogicOperation {

	@Override
	public boolean execute(CounterByType techDebt) {
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
