package io.github.ihepda.techdebt.processor.report;

import java.io.PrintStream;
import java.util.Set;

import io.github.ihepda.techdebt.TechDebt.Severity;
import io.github.ihepda.techdebt.processor.TechDebtElement;

public class SysoutReport extends AbstractReport {


	@Override
	public boolean report(Set<TechDebtElement> elements) {
		elements = this.getOrderedSet(elements);
		Severity currentSeverity = null;
		PrintStream writer = System.out;
		writer.println("******* Starting to dump techdebt report *******");
		for (TechDebtElement techDebtElement : elements) {
			if(!this.isSeverityOrdered()) {
				writer.append('*').append(techDebtElement.getElementKind().toString()).append(" ==> ");
				writer.append(techDebtElement.getFullName()).append('\n');
				writer.append('\t').append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
			} else {
				if(currentSeverity != techDebtElement.getSeverity()) {
					currentSeverity = techDebtElement.getSeverity();
					writer.append('*').append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
				}
				writer.append('\t').append(techDebtElement.getElementKind().toString()).append(" ==> ");
				writer.append(techDebtElement.getFullName()).append('\n');
			}
			writer.append('\t').append("Type = " ).append(techDebtElement.getType()).append('\n');
			writer.append('\t').append("Comment = " ).append(techDebtElement.getComment()).append('\n');
			writer.append('\t').append("Author = " ).append(techDebtElement.getAuthor()).append('\n');
			writer.append('\t').append("Date = " ).append(techDebtElement.getDate()).append('\n');
			writer.append('\t').append("Effort = " ).append(techDebtElement.getEffort()).append('\n');
		}
		writer.println("************************************************");
		return true;
	}

}
