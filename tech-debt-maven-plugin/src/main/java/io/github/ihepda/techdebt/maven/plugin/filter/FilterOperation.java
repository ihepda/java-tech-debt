package io.github.ihepda.techdebt.maven.plugin.filter;

public interface FilterOperation {

	boolean execute(CounterByType counterByType);
	
	void dump(StringBuilder sb);
}
