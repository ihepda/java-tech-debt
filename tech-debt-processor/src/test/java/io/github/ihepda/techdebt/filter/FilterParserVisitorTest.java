package io.github.ihepda.techdebt.filter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.TechDebtElement;

class FilterParserVisitorTest {

	@Test
	void testSimpleEquals() {
		TechDebtElement techDebt = new TechDebtElement();
		techDebt.setSeverity(Severity.MAJOR);
		FilterOperation filterOperation = FilterParserVisitor.parse("severity = 'MAJOR'");
		boolean execute = filterOperation.execute(techDebt);
		
		assertTrue(execute);
		techDebt.setSeverity(Severity.MINOR);
		execute = filterOperation.execute(techDebt);
		
		assertFalse(execute);
		
	}

	@Test
	void testSimpleGreaterEquals() {
		TechDebtElement techDebt = new TechDebtElement();

		techDebt.setSeverity(Severity.MAJOR);
		FilterOperation filterOperation = FilterParserVisitor.parse("severity >= 'MAJOR'");
		boolean execute = filterOperation.execute(techDebt);
		assertTrue(execute);
		
		techDebt.setSeverity(Severity.MINOR);
		execute = filterOperation.execute(techDebt);
		assertFalse(execute);

		techDebt.setSeverity(Severity.CATASTROPHIC);
		execute = filterOperation.execute(techDebt);
		assertTrue(execute);
		
	}
	
	@Test
	void testLike() {
		TechDebtElement techDebt = new TechDebtElement();
		techDebt.setSeverity(Severity.MAJOR);
		techDebt.setComment("Test comment in order to test like");
		FilterOperation filterOperation = FilterParserVisitor.parse("comment like '%test%'");

		boolean execute = filterOperation.execute(techDebt);
		assertTrue(execute);
		
		techDebt.setComment("Test est like");
		execute = filterOperation.execute(techDebt);
		assertFalse(execute);

		techDebt.setComment("Test comment in order to test like");
		filterOperation = FilterParserVisitor.parse("comment like 'Test comment in order __ test like'");
		execute = filterOperation.execute(techDebt);
		assertTrue(execute);
		
	}

	@Test
	void testNotLike() {
		TechDebtElement techDebt = new TechDebtElement();
		techDebt.setSeverity(Severity.MAJOR);
		techDebt.setComment("Test comment in order to test like");
		FilterOperation filterOperation = FilterParserVisitor.parse("comment not like '%test%'");

		boolean execute = filterOperation.execute(techDebt);
		assertFalse(execute);
		
		
	}

	@Test
	void testLikeMultiLine() {
		TechDebtElement techDebt = new TechDebtElement();
		techDebt.setSeverity(Severity.MAJOR);
		techDebt.setComment("Test comment in order to test like\n\n with a multi line");
		FilterOperation filterOperation = FilterParserVisitor.parse("comment like '%test%'");

		boolean execute = filterOperation.execute(techDebt);
		assertTrue(execute);
		
		
	}

	@Test
	void testIn() {
		TechDebtElement techDebt = new TechDebtElement();
		techDebt.setSeverity(Severity.MAJOR);
		FilterOperation filterOperation = FilterParserVisitor.parse("severity in ('MAJOR', 'MINOR')");

		boolean execute = filterOperation.execute(techDebt);
		assertTrue(execute);

		techDebt.setSeverity(Severity.CATASTROPHIC);

		execute = filterOperation.execute(techDebt);
		assertFalse(execute);
	}

}
