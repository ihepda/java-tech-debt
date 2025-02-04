package io.github.ihepda.techdebt.maven.plugin.filter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public class CounterByData {
	private Map<Severity, Integer> bySeverity = new EnumMap<>(Severity.class);
	private Map<Type, Integer> byType= new EnumMap<>(Type.class);
	private int byAll = 0;
	
	public void add(Severity severity, Type type) {
		List<Severity> hierarchicalSeverities = this.getHierarchicalEnums(severity);
		for (Severity s : hierarchicalSeverities) {
			Integer severityCounter = bySeverity.computeIfAbsent(s, k -> 0);
			bySeverity.put(s, severityCounter + 1);
		}
		
		Integer typeCounter = byType.computeIfAbsent(type, k -> 0);
		byType.put(type, typeCounter + 1);
		byAll++;
	}
	
	public int getCount(Severity severity) {
		return bySeverity.getOrDefault(severity,0);
	}
	
	public int getCount(Type type) {
		return byType.getOrDefault(type, 0);
	}
	
	public int getCount() {
		return byAll;
	}
	
	
	private List<Severity> getHierarchicalEnums(Severity s) {
		Severity[] values = Severity.values();
		return Stream.of(values).filter(v-> v.getPriority() <= s.getPriority()).toList();
	}

}
