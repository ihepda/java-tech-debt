package io.github.ihepda.techdebt.maven.plugin.filter;

import java.util.EnumMap;
import java.util.Map;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public class CounterByType {
	private Map<Severity, Integer> bySeverity = new EnumMap<>(Severity.class);
	private Map<Type, Integer> byType= new EnumMap<>(Type.class);
	private int byAll = 0;
	
	public void add(Severity severity, Type type) {
		Integer severityCounter = bySeverity.computeIfAbsent(severity, k->0);
		bySeverity.put(severity, severityCounter+1);
		Integer typeCounter = byType.computeIfAbsent(type, k -> 0);
		byType.put(type, typeCounter + 1);
		byAll++;
	}
	
	public int getCount(Severity severity) {
		return bySeverity.get(severity);
	}
	
	public int getCount(Type type) {
		return byType.get(type);
	}
	
	public int getCount() {
		return byAll;
	}
	
}
