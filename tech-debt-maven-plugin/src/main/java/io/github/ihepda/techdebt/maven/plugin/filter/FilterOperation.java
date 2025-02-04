package io.github.ihepda.techdebt.maven.plugin.filter;

public interface FilterOperation {

	boolean execute(CounterByData counterByType);
	
	void dump(StringBuilder sb);
}
