package io.github.ihepda.techdebt.maven.plugin.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public class FilterParserVisitorImplTest {

	@Test
	public void testSimple() {
		CounterByData counterByType = new CounterByData();
		counterByType.add(Severity.MAJOR, Type.FIX);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("severity.major = 1");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		counterByType.add(Severity.MAJOR, Type.MAINTAINABILITY);
		execute = filterOperation.execute(counterByType);
		assertFalse(execute);

	}

	@Test
	public void testWithAnd() {
		CounterByData counterByType = new CounterByData();
		counterByType.add(Severity.MAJOR, Type.FIX);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("severity.major >= 1 and type.maintainability = 0");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		counterByType.add(Severity.MAJOR, Type.MAINTAINABILITY);
		execute = filterOperation.execute(counterByType);
		assertFalse(execute);

	}

	@Test
	public void testWithOr() {
		CounterByData counterByType = new CounterByData();
		counterByType.add(Severity.MAJOR, Type.FIX);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("severity.major >= 1 or type.maintainability = 0");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		counterByType.add(Severity.MAJOR, Type.MAINTAINABILITY);
		execute = filterOperation.execute(counterByType);
		assertTrue(execute);

	}

	@Test
	public void testAll() {
		CounterByData counterByType = new CounterByData();
		counterByType.add(Severity.MAJOR, Type.FIX);
		counterByType.add(Severity.MAJOR, Type.MAINTAINABILITY);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("all = 2");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		counterByType.add(Severity.MAJOR, Type.MAINTAINABILITY);
		execute = filterOperation.execute(counterByType);
		assertFalse(execute);
	}

	@Test
	public void testHierarchicalValue() {
		CounterByData counterByType = new CounterByData();
		counterByType.add(Severity.CATASTROPHIC, Type.FIX);
		counterByType.add(Severity.MINOR, Type.PERFORMANCE);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("severity.major >= 1");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		filterOperation = FilterParserVisitorImpl.parse("severity.major > 1");
		execute = filterOperation.execute(counterByType);
		assertFalse(execute);
		filterOperation = FilterParserVisitorImpl.parse("severity.major = 1");
		execute = filterOperation.execute(counterByType);
		assertTrue(execute);
		filterOperation = FilterParserVisitorImpl.parse("severity.minor > 1");
		execute = filterOperation.execute(counterByType);
		assertTrue(execute);

	}
}
