package io.github.ihepda.techdebt.maven.plugin.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebt.Type;

public class FilterParserVisitorImplTest {

	@Test
	public void test() {
		CounterByType counterByType = new CounterByType();
		counterByType.add(Severity.MAJOR, Type.FIX);
		FilterOperation filterOperation = FilterParserVisitorImpl.parse("severity.major = 1");
		boolean execute = filterOperation.execute(counterByType);
		assertTrue(execute);

	}

}
