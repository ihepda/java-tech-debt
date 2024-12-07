package io.github.ihepda.techdebt.filter;

import java.util.Objects;

public class NotEqualsOperation extends CompareOperation {

	@Override
	protected boolean compare(Comparable<?> techDebtValue, Comparable<?> value) {
		return !Objects.equals(techDebtValue, value);
	}

	@Override
	protected String operation() {
		return "!=";
	}
}
