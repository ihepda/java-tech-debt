package io.github.ihepda.techdebt.filter;

public class GreaterThanOperation extends CompareOperation {

	@Override
	protected boolean compare(Comparable<?> techDebtValue, Comparable<?> value) {
		Integer compare = this.getElement().compare(techDebtValue, value);
		return compare > 0;
	}

	@Override
	protected String operation() {
		return ">";
	}
}
