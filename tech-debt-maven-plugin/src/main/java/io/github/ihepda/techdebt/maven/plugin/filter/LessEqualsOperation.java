package io.github.ihepda.techdebt.maven.plugin.filter;

public class LessEqualsOperation extends CompareOperation {

	@Override
	protected boolean compare(int count, int value) {
		return count <= value;
	}

	@Override
	protected String operation() {
		return "<=";
	}
}
