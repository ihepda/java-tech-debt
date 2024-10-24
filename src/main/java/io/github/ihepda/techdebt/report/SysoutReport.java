package io.github.ihepda.techdebt.report;

import java.io.PrintStream;
import java.util.Set;

import io.github.ihepda.techdebt.TechDebtElement;
import io.github.ihepda.techdebt.TechDebt.Severity;

public class SysoutReport extends AbstractReport {


	@Override
	public boolean report(Set<TechDebtElement> elements) {
		elements = this.getOrderedSet(elements);
		Severity currentSeverity = null;
		PrintStream writer = System.out;
		writer.println("******* Starting to dump techdebt report *******");
		for (TechDebtElement techDebtElement : elements) {
			if(!this.isSeverityOrdered()) {
				writer.append(techDebtElement.getElementKind().toString()).append(" ==> ");
				writer.append(techDebtElement.getFullName()).append('\n');
				writer.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
			} else {
				if(currentSeverity != techDebtElement.getSeverity()) {
					currentSeverity = techDebtElement.getSeverity();
					writer.append("Severity = " ).append(techDebtElement.getSeverity().toString()).append('\n');
				}
				writer.append(techDebtElement.getElementKind().toString()).append(" ==> ");
				writer.append(techDebtElement.getFullName()).append('\n');
			}
			writer.append("Type = " ).append(techDebtElement.getType()).append('\n');
			writer.append("Comment = " ).append(techDebtElement.getComment()).append('\n');
			writer.append("Author = " ).append(techDebtElement.getAuthor()).append('\n');
			writer.append("Date = " ).append(techDebtElement.getDate()).append('\n');
			writer.append("Effort = " ).append(techDebtElement.getEffort()).append('\n');
		}
		writer.println("************************************************");
		return true;
	}

}
