package io.github.ihepda.techdebt.maven.plugin.filter;

public class AndLogicOperation extends LogicOperation {

	@Override
	public boolean execute(CounterByData techDebt) {
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
