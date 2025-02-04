package io.github.ihepda.techdebt.maven.plugin.filter;

public class OrLogicOperation extends LogicOperation {

	@Override
	public boolean execute(CounterByData techDebt) {
		boolean execute = this.getLeft().execute(techDebt);
		if (execute) {
			return true;
		}
		
		return this.getRight().execute(techDebt);
	}

	@Override
	protected String operation() {
		return "OR";
	}

}
